package arkanoid.core;

import arkanoid.ui.*;
import arkanoid.util.Constant;
import arkanoid.util.GameMode;
import arkanoid.util.HighScoreManager;
import arkanoid.util.SoundManager;
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

    private String currentScreen = "MENU";
    private String previousScreen = "MENU"; // MENU, GAME, PAUSE, GAMEOVER, SETTINGS, HIGHSCORES, NAME_INPUT

    @Override
    public void start(Stage stage) {
        final int width = Constant.SCREEN_WIDTH;
        final int height = Constant.SCREEN_HEIGHT;

        // root container
        root = new StackPane();

        //  canvas and graphics context
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // managers
        SoundManager.initialize();
        HighScoreManager.initialize();

        // manager and renderer
        gm = GameManager.getInstance();
        renderer = new Renderer(gc);

        initScreens();

        // create scene
        scene = new Scene(root, width, height);

        // IO
        scene.setOnKeyPressed(event -> handleKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> handleKeyReleased(event.getCode()));

        // Loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if ("GAME".equals(currentScreen) && Constant.STATE_RUNNING.equals(gm.getState())) {
                    gm.update();
                    renderer.renderAll(gm, width, height);
                } else if ("GAME".equals(currentScreen) && Constant.STATE_GAME_OVER.equals(gm.getState())) {
                    showGameOverScreen();
                }
            }
        };
        gameLoop.start();

        showMenuScreen();

        stage.setTitle("arkanoid");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            gameLoop.stop();
            Platform.exit();
        });
        stage.show();
    }

    /**
     * UI initial.
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
        pauseMenu.setOnSettings(() -> showSettingsScreen());

        // Game Over Screen
        gameOverScreen = new SimpleGameOverScreen();
        gameOverScreen.setOnRestart(() -> restartGame());
        gameOverScreen.setOnMainMenu(() -> returnToMenu());

        // Settings Screen
        settingsScreen = new SettingsScreen();
        settingsScreen.setOnBack(() -> goBackFromSettings());

        // High Score Screen
        highScoreScreen = new HighScoreScreen();
        highScoreScreen.setOnBack(() -> returnToMenu());

        modeSelectionScreen = new ModeSelectionScreen();
        modeSelectionScreen.setOnNormalModeSelected(() -> startGame(GameMode.NORMAL));
        modeSelectionScreen.setOnFunnyModeSelected(() -> startGame(GameMode.FUNNY));
        modeSelectionScreen.setOnBackSelected(() -> showMenuScreen());
    }

    /**
     * hande IO.
     */
    private void handleKeyPressed(KeyCode code) {
        if ("GAME".equals(currentScreen)) {
            if (Constant.STATE_RUNNING.equals(gm.getState())) {
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
            } else if (Constant.STATE_PAUSED.equals(gm.getState())) {
                if (code == KeyCode.ESCAPE || code == KeyCode.P) {
                    resumeGame();
                }
            }
        }
    }

    private void handleKeyReleased(KeyCode code) {
        if ("GAME".equals(currentScreen) && Constant.STATE_RUNNING.equals(gm.getState())) {
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
     * main menu.
     */
    private void showMenuScreen() {
        currentScreen = "MENU";
        root.getChildren().clear();
        root.getChildren().add(menuScreen);
    }

    /**
     * new game
     */
    private void startGame(GameMode mode) {
        currentScreen = "GAME";
        gm.start(mode);
        root.getChildren().clear();
        root.getChildren().add(canvas);
        canvas.requestFocus();
    }

    /**
     * pause game.
     */
    private void pauseGame() {
        if (Constant.STATE_RUNNING.equals(gm.getState())) {
            gm.pause();
            showPauseMenu();
        }
    }

    /**
     * continue.game
     */
    private void resumeGame() {
        if (Constant.STATE_PAUSED.equals(gm.getState())) {
            gm.resume();
            currentScreen = "GAME";
            root.getChildren().remove(pauseMenu);
            canvas.requestFocus();
        }
    }

    /**
     * restart.
     */
    private void restartGame() {
        currentScreen = "GAME";
        gm.restart();
        root.getChildren().clear();
        root.getChildren().add(canvas);
        canvas.requestFocus();
    }

    /**
     * back to main.
     */
    private void returnToMenu() {
        gm.returnToMenu();
        showMenuScreen();
        menuScreen.updateHighScore(HighScoreManager.getHighestScore());
    }

    private void showPauseMenu() {
        currentScreen = "PAUSE";
        root.getChildren().remove(settingsScreen);
        if (!root.getChildren().contains(pauseMenu)) {
            root.getChildren().add(pauseMenu);
        }
    }

    /**
     * gameover.
     */
    private void showGameOverScreen() {
        if (HighScoreManager.isHighScore(gm.getScore())) {
            showPlayerNameInputScreen();
        } else {
            currentScreen = "GAMEOVER";
            gameOverScreen.setScore(gm.getScore());
            root.getChildren().clear();
            root.getChildren().add(gameOverScreen);
        }
    }

    /**
     * Mode show.
     */
    private void showModeSelectionScreen() {
        currentScreen = "MODE_SELECTION";
        root.getChildren().clear();
        root.getChildren().add(modeSelectionScreen);
    }

    /**
     * Settings.
     */
    private void showSettingsScreen() {
        previousScreen = currentScreen;
        currentScreen = "SETTINGS";
        root.getChildren().remove(pauseMenu);
        root.getChildren().remove(menuScreen);
        root.getChildren().add(settingsScreen);
    }

    private void goBackFromSettings() {
        if ("PAUSE".equals(previousScreen)) {
            showPauseMenu();
        } else {
            showMenuScreen();
        }
    }

    /**
     * High Score.
     */
    private void showHighScoreScreen() {
        currentScreen = "HIGHSCORES";
        highScoreScreen.refreshHighScores();
        root.getChildren().clear();
        root.getChildren().add(highScoreScreen);
    }

    /**
     * Name input.
     */
    private void showPlayerNameInputScreen() {
        currentScreen = "NAME_INPUT";
        playerNameInputScreen = new PlayerNameInputScreen(gm.getScore());
        playerNameInputScreen.setOnSubmit(() -> {
            // after input name, return to main menu.
            returnToMenu();
        });
        playerNameInputScreen.setOnSkip(() -> {
            // after skip return to main menu.
            returnToMenu();
        });
        root.getChildren().add(playerNameInputScreen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
