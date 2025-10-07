package Arkanoid;

import Arkanoid.Object.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Renderer - Lớp chịu trách nhiệm vẽ các đối tượng để hiển thị trên màn hình
 * Phương thức: draw(GameObject obj), renderGame(GameManager gameManager)
 */
public class Renderer {
    private GraphicsContext gc;
    
    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }
    
    /**
     * Vẽ một GameObject
     */
    public void draw(GameObject obj) {
        obj.render(gc);
    }
    
    /**
     * Render toàn bộ game
     */
    public void renderGame(GameManager gameManager) {
        // Xóa màn hình
        clearScreen(gameManager);
        
        // Vẽ background
        drawBackground(gameManager);
        
        // Vẽ game objects
        if (gameManager.getGameState() == GameManager.GameState.PLAYING) {
            // Vẽ paddle
            draw(gameManager.getPaddle());
            
            // Vẽ ball
            draw(gameManager.getBall());
            
            // Vẽ bricks
            for (Brick brick : gameManager.getBricks()) {
                draw(brick);
            }
            
            // Vẽ powerUps
            for (PowerUp powerUp : gameManager.getPowerUps()) {
                draw(powerUp);
            }
        }
        
        // Vẽ UI
        drawUI(gameManager);
        
        // Vẽ game state messages
        drawGameStateMessages(gameManager);
    }
    
    /**
     * Xóa màn hình
     */
    private void clearScreen(GameManager gameManager) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameManager.getGameWidth(), gameManager.getGameHeight());
    }
    
    /**
     * Vẽ background
     */
    private void drawBackground(GameManager gameManager) {
        // Vẽ border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(1, 1, gameManager.getGameWidth() - 2, gameManager.getGameHeight() - 2);
    }
    
    /**
     * Vẽ UI (score, lives, etc.)
     */
    private void drawUI(GameManager gameManager) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.setTextAlign(TextAlignment.LEFT);
        
        // Vẽ score
        gc.fillText("Score: " + gameManager.getScore(), 10, 25);
        
        // Vẽ lives
        gc.fillText("Lives: " + gameManager.getLives(), 10, 45);
        
        // Vẽ powerUp status
        if (gameManager.getPaddle().getCurrentPowerUp() != null) {
            PowerUp currentPowerUp = gameManager.getPaddle().getCurrentPowerUp();
            gc.fillText("PowerUp: " + currentPowerUp.getType(), 10, 65);
        }
    }
    
    /**
     * Vẽ thông báo trạng thái game
     */
    private void drawGameStateMessages(GameManager gameManager) {
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("Arial", 24));
        gc.setTextAlign(TextAlignment.CENTER);
        
        int centerX = gameManager.getGameWidth() / 2;
        int centerY = gameManager.getGameHeight() / 2;
        
        switch (gameManager.getGameState()) {
            case MENU:
                gc.fillText("ARKANOID", centerX, centerY - 50);
                gc.setFont(new Font("Arial", 16));
                gc.fillText("Press SPACE to start", centerX, centerY);
                break;
                
            case PAUSED:
                gc.fillText("PAUSED", centerX, centerY);
                gc.setFont(new Font("Arial", 16));
                gc.fillText("Press SPACE to continue", centerX, centerY + 30);
                break;
                
            case GAME_OVER:
                gc.fillText("GAME OVER", centerX, centerY - 30);
                gc.setFont(new Font("Arial", 16));
                gc.fillText("Final Score: " + gameManager.getScore(), centerX, centerY);
                gc.fillText("Press SPACE to restart", centerX, centerY + 30);
                break;
                
            case VICTORY:
                gc.fillText("VICTORY!", centerX, centerY - 30);
                gc.setFont(new Font("Arial", 16));
                gc.fillText("Final Score: " + gameManager.getScore(), centerX, centerY);
                gc.fillText("Press SPACE to play again", centerX, centerY + 30);
                break;
        }
    }
    
    /**
     * Vẽ debug info (nếu cần)
     */
    public void drawDebugInfo(GameManager gameManager) {
        if (gameManager.getGameState() != GameManager.GameState.PLAYING) return;
        
        gc.setFill(Color.CYAN);
        gc.setFont(new Font("Arial", 12));
        gc.setTextAlign(TextAlignment.LEFT);
        
        // Ball position
        Ball ball = gameManager.getBall();
        gc.fillText("Ball: (" + (int)ball.getX() + ", " + (int)ball.getY() + ")", 
                    gameManager.getGameWidth() - 150, 25);
        
        // Ball velocity
        gc.fillText("Velocity: (" + ball.getDx() + ", " + ball.getDy() + ")", 
                    gameManager.getGameWidth() - 150, 45);
        
        // Bricks count
        gc.fillText("Bricks: " + gameManager.getBricks().size(), 
                    gameManager.getGameWidth() - 150, 65);
    }
}
