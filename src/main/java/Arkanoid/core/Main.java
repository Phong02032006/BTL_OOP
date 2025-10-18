package Arkanoid.core;

import Arkanoid.UI.*;
import Arkanoid.util.Constant;
import Arkanoid.util.GameMode;
import Arkanoid.util.HighScoreManager;
import Arkanoid.util.SoundManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene scene;
    private StackPane root;
    private Canvas canvas;
    private GraphicsContext gc;

    private GameManager gm;
    private Renderer renderer;
    private AnimationTimer gameLoop;

    // UI Screens
    private SimpleMenuScreen menuScreen;
    private SimplePauseMenu pauseMenu;
    private SimpleGameOverScreen gameOverScreen;
    private SettingsScreen settingsScreen;
    private HighScoreScreen highScoreScreen;
    private PlayerNameInputScreen playerNameInputScreen;
    private ModeSelectionScreen modeSelectionScreen;

    private String currentScreen = "MENU"; // MENU, GAME, PAUSE, GAMEOVER, SETTINGS, HIGHSCORES, NAME_INPUT

    @Override
    public void start(Stage stage) {
        final int width = Constant.SCREEN_WIDTH;
        final int height = Constant.SCREEN_HEIGHT;

        // Tạo root container
        root = new StackPane();

        // Tạo canvas và graphics context
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // Khởi tạo các managers
        SoundManager.initialize();
        HighScoreManager.initialize();

        // Tạo game manager và renderer
        gm = GameManager.getInstance();
        renderer = new Renderer(gc);

        // Khởi tạo các màn hình
        initScreens();

        // Tạo scene
        scene = new Scene(root, width, height);

        // Xử lý phím
        scene.setOnKeyPressed(event -> handleKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> handleKeyReleased(event.getCode()));

        // Tạo game loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if ("GAME".equals(currentScreen) && GameManager.STATE_RUNNING.equals(gm.getState())) {
                    gm.update();
                    renderer.renderAll(gm, width, height);
                } else if ("GAME".equals(currentScreen) && GameManager.STATE_GAME_OVER.equals(gm.getState())) {
                    // Tự động chuyển sang màn hình Game Over
                    showGameOverScreen();
                }
            }
        };
        gameLoop.start();

        // Hiển thị menu ban đầu
        showMenuScreen();

        stage.setTitle("Arkanoid");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            gameLoop.stop();
            Platform.exit();
        });
        stage.show();
    }

    /**
     * Khởi tạo tất cả các màn hình UI
     */
    private void initScreens() {
        // Menu Screen
        menuScreen = new SimpleMenuScreen();
        menuScreen.setOnStart(() -> showModeSelectionScreen());
        menuScreen.setOnHighScores(() -> showHighScoreScreen());
        menuScreen.setOnSettings(() -> showSettingsScreen());
        menuScreen.setOnExit(() -> Platform.exit());

        // Pause Menu
        pauseMenu = new SimplePauseMenu();
        pauseMenu.setOnResume(() -> resumeGame());
        pauseMenu.setOnRestart(() -> restartGame());
        pauseMenu.setOnMainMenu(() -> returnToMenu());

        // Game Over Screen
        gameOverScreen = new SimpleGameOverScreen();
        gameOverScreen.setOnRestart(() -> restartGame());
        gameOverScreen.setOnMainMenu(() -> returnToMenu());

        // Settings Screen
        settingsScreen = new SettingsScreen();
        settingsScreen.setOnBack(() -> returnToMenu());

        // High Score Screen
        highScoreScreen = new HighScoreScreen();
        highScoreScreen.setOnBack(() -> returnToMenu());

        modeSelectionScreen = new ModeSelectionScreen();
        modeSelectionScreen.setOnNormalModeSelected(() -> startGame(GameMode.NORMAL));
        modeSelectionScreen.setOnFunnyModeSelected(() -> startGame(GameMode.FUNNY));
        modeSelectionScreen.setOnBackSelected(() -> showMenuScreen());
    }

    /**
     * Xử lý phím nhấn
     */
    private void handleKeyPressed(KeyCode code) {
        if ("GAME".equals(currentScreen)) {
            if (GameManager.STATE_RUNNING.equals(gm.getState())) {
                switch (code) {
                    case LEFT:
                        gm.onKeyPressed("LEFT");
                        break;
                    case RIGHT:
                        gm.onKeyPressed("RIGHT");
                        break;
                    case SPACE:
                        gm.onKeyPressed("SPACE");
                        break;
                    case ESCAPE:
                    case P:
                        pauseGame();
                        break;
                }
            } else if (GameManager.STATE_PAUSED.equals(gm.getState())) {
                if (code == KeyCode.ESCAPE || code == KeyCode.P) {
                    resumeGame();
                }
            }
        }
    }

    /**
     * Xử lý phím thả
     */
    private void handleKeyReleased(KeyCode code) {
        if ("GAME".equals(currentScreen) && GameManager.STATE_RUNNING.equals(gm.getState())) {
            switch (code) {
                case LEFT:
                    gm.onKeyReleased("LEFT");
                    break;
                case RIGHT:
                    gm.onKeyReleased("RIGHT");
                    break;
            }
        }
    }

    /**
     * Hiển thị màn hình menu chính
     */
    private void showMenuScreen() {
        currentScreen = "MENU";
        root.getChildren().clear();
        root.getChildren().add(menuScreen);
    }

    /**
     * Bắt đầu game mới
     */
    private void startGame(GameMode mode) {
        currentScreen = "GAME";
        gm.start(mode);
        root.getChildren().clear();
        root.getChildren().add(canvas);
        canvas.requestFocus();
    }

    /**
     * Tạm dừng game
     */
    private void pauseGame() {
        if (GameManager.STATE_RUNNING.equals(gm.getState())) {
            gm.pause();
            currentScreen = "PAUSE";
            root.getChildren().add(pauseMenu); // Overlay trên canvas
        }
    }

    /**
     * Tiếp tục game
     */
    private void resumeGame() {
        if (GameManager.STATE_PAUSED.equals(gm.getState())) {
            gm.resume();
            currentScreen = "GAME";
            root.getChildren().remove(pauseMenu);
            canvas.requestFocus();
        }
    }

    /**
     * Chơi lại game
     */
    private void restartGame() {
        currentScreen = "GAME";
        gm.restart();
        root.getChildren().clear();
        root.getChildren().add(canvas);
        canvas.requestFocus();
    }

    /**
     * Quay về menu chính
     */
    private void returnToMenu() {
        gm.returnToMenu();
        showMenuScreen();
        // Cập nhật điểm cao nhất trên menu
        menuScreen.updateHighScore(HighScoreManager.getHighestScore());
    }

    /**
     * Hiển thị màn hình game over
     */
    private void showGameOverScreen() {
        // Kiểm tra xem có phải điểm cao không
        if (HighScoreManager.isHighScore(gm.getScore())) {
            // Nếu là điểm cao, hiển thị màn hình nhập tên trực tiếp
            showPlayerNameInputScreen();
        } else {
            // Nếu không phải điểm cao, hiển thị màn hình game over bình thường
            currentScreen = "GAMEOVER";
            gameOverScreen.setScore(gm.getScore());
            root.getChildren().clear();
            root.getChildren().add(gameOverScreen);
        }
    }

    /**
     * Hiển thị màn hình chọn chế độ chơi
     */
    private void showModeSelectionScreen() {
        currentScreen = "MODE_SELECTION";
        root.getChildren().clear();
        root.getChildren().add(modeSelectionScreen);
    }

    /**
     * Hiển thị màn hình Settings
     */
    private void showSettingsScreen() {
        currentScreen = "SETTINGS";
        root.getChildren().clear();
        root.getChildren().add(settingsScreen);
    }

    /**
     * Hiển thị màn hình High Score
     */
    private void showHighScoreScreen() {
        currentScreen = "HIGHSCORES";
        highScoreScreen.refreshHighScores();
        root.getChildren().clear();
        root.getChildren().add(highScoreScreen);
    }

    /**
     * Hiển thị màn hình nhập tên người chơi
     */
    private void showPlayerNameInputScreen() {
        currentScreen = "NAME_INPUT";
        playerNameInputScreen = new PlayerNameInputScreen(gm.getScore());
        playerNameInputScreen.setOnSubmit(() -> {
            // Sau khi submit tên, quay về màn hình menu chính
            returnToMenu();
        });
        playerNameInputScreen.setOnSkip(() -> {
            // Sau khi skip, quay về màn hình menu chính
            returnToMenu();
        });
        root.getChildren().add(playerNameInputScreen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
