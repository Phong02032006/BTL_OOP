package Arkanoid;

import Arkanoid.UI.SimpleGameOverScreen;
import Arkanoid.UI.SimpleMenuScreen;
import Arkanoid.UI.SimplePauseMenu;
import Arkanoid.util.Constant;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main class đơn giản - chỉ có menu system, không tích hợp high score vào game flow
 */
public class SimpleMain extends Application {
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
    
    private String currentScreen = "MENU"; // MENU, GAME, PAUSE, GAMEOVER
    
    @Override
    public void start(Stage stage) {
        final int width = Constant.SCREEN_WIDTH;
        final int height = Constant.SCREEN_HEIGHT;
        
        // Tạo root container
        root = new StackPane();
        
        // Tạo canvas và graphics context
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        
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
        stage.setOnCloseRequest(e -> {
            gameLoop.stop();
            Platform.exit();
        });
        stage.show();
    }
    
    /**
     * Khởi tạo tất cả các màn hình UI
     */
    private void initScreens() {
        // Menu Screen (đơn giản)
        menuScreen = new SimpleMenuScreen();
        menuScreen.setOnStart(() -> startGame());
        menuScreen.setOnHighScores(() -> {
            // Không có chức năng high score trong phiên bản simple
            System.out.println("High Score feature not available in simple version");
        });
        menuScreen.setOnExit(() -> Platform.exit());
        
        // Pause Menu
        pauseMenu = new SimplePauseMenu();
        pauseMenu.setOnResume(() -> resumeGame());
        pauseMenu.setOnRestart(() -> restartGame());
        pauseMenu.setOnMainMenu(() -> returnToMenu());
        
        // Game Over Screen (đơn giản - không có high score)
        gameOverScreen = new SimpleGameOverScreen();
        gameOverScreen.setOnRestart(() -> restartGame());
        gameOverScreen.setOnMainMenu(() -> returnToMenu());
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
    private void startGame() {
        currentScreen = "GAME";
        gm.start();
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
    }
    
    /**
     * Hiển thị màn hình game over
     */
    private void showGameOverScreen() {
        currentScreen = "GAMEOVER";
        gameOverScreen.setScore(gm.getScore());
        root.getChildren().clear();
        root.getChildren().add(gameOverScreen);
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}

