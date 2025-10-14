package Arkanoid;

import Arkanoid.Object.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import Arkanoid.util.Constant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance;
    private boolean ballLaunched;
    private int curLevel ;
    private final String[] LEVELS = {
            "level1.txt","level2.txt","level3.txt","level4.txt","level5.txt","level6.txt","level7.txt" };
            // danh sach cac level cho nguoi choi

    private GameManager() {
        // Private constructor để đảm bảo Singleton pattern
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Reset singleton instance (chỉ dùng khi cần thiết)
     */
    public static void resetInstance() {
        instance = null;
    }

    private Background background;
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private List<PowerUp> activePowerUps;

    private int score;
    private int lives;
    private String state;

    // Game states
    public static final String STATE_MENU = "MENU";
    public static final String STATE_RUNNING = "RUNNING";
    public static final String STATE_PAUSED = "PAUSED";
    public static final String STATE_GAME_OVER = "GAME_OVER";

    private static final int width = Constant.SCREEN_WIDTH;
    private static final int height = Constant.SCREEN_HEIGHT;

    /*
    .Khoi tao tro choi
     */

    private void initBricks(String levelFile) {
        bricks = Arkanoid.util.LevelLoader.loadLevel(
                levelFile,
                Constant.BRICK_WIDTH,
                Constant.BRICK_HEIGHT
        );
    }

    /**
     * khoi tao vi tri cac obj
     */
    public void start() {
        background = new Background("/images/background.png");
        paddle = new Paddle(width / 2 - 50, height - 30, Constant.PADDLE_WIDTH, Constant.PADDLE_HEIGHT, Constant.PADDLE_SPEED);
        double ballX = (width / 2) - (Constant.BALL_RADIUS / 2);
        curLevel = 0;
        double ballY = height - 30 - Constant.PADDLE_HEIGHT - Constant.BALL_RADIUS - 2;
        ball = new Ball(ballX, ballY, Constant.BALL_RADIUS, Constant.BALL_SPEED, 1, -1);
        bricks = Arkanoid.util.LevelLoader.loadLevel(LEVELS[curLevel],
                Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);
        powerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        state = STATE_RUNNING;
        ballLaunched = false;



    }

    /**
     * Reset game để chơi lại
     */
    public void restart() {
        start();
    }

    private boolean movingLeft = false;
    private boolean movingRight = false;

    // Nhận input
    public void onKeyPressed(String key) {
        switch (key) {
            case "LEFT":
                movingLeft = true;
                break;
            case "RIGHT":
                movingRight = true;
                break;
            case "SPACE":
                if (!ballLaunched) launchedBall();
                break;
        }
    }

    public void onKeyReleased(String key) {
        switch (key) {
            case "LEFT":
                movingLeft = false;
                break;
            case "RIGHT":
                movingRight = false;
                break;
        }
    }

    private void launchedBall() {
        ballLaunched = true;

        java.util.Random rand = new java.util.Random();
        //random hướng ngang: -0.8 -> + 0.8 tránh bay thẳng đứng
        double dirX = (rand.nextDouble() * 1.6 - 0.8);

        //copy sign se giu nguyen dau cua dirX Vidu : (0.3, -0.05) -> -0.3
        if (Math.abs(dirX) < 0.3) dirX = Math.copySign(0.3, dirX);

        ball.setDx(dirX);
        ball.setDirectionY(-1);
    }

    public void update() {
        if (!STATE_RUNNING.equals(state)) return;

        if (movingLeft) paddle.moveLeft(width);
        if (movingRight) paddle.moveRight(width);

        if (!ballLaunched) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - paddle.getHeight() / 2 - 2);
        } else {
            ball.update();

            // Kiểm tra va chạm với paddle (chỉ khi bóng đang rơi xuống)
            if (ball.checkCollision(paddle) && ball.getDy() > 0) {
                ball.bounceOff(paddle);
                // Đảm bảo bóng không bị dính vào paddle
                ball.setY(paddle.getY() - ball.getHeight() - 1);
            }

            // Kiểm tra va chạm với các viên gạch
            for (int i = bricks.size() - 1; i >= 0; i--) {
                Brick brick = bricks.get(i);
                if (ball.checkCollision(brick)) {
                    // 1) Phản xạ bóng trước
                    ball.bounceOff(brick);

                    // 2) Trừ máu gạch, nhận biết có bị phá không
                    boolean destroyed = brick.takeHit();

                    // 3) Nếu gạch vỡ: spawn powerup (nếu có), remove, cộng điểm
                    if (destroyed) {
                        if (brick.hasPowerUp()) {
                            powerUps.add(brick.getPowerUp());
                        }
                        bricks.remove(i);
                        score += 10;
                    }

                    // 4) Xử lý xong 1 viên trong frame này
                    break;
                }
            }

            if (ball.getY() + ball.getHeight() > Constant.SCREEN_HEIGHT) resetBall();
        }
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp pu = powerUps.get(i);
            pu.update();

            boolean removed = false;

            if (pu.getY() > Constant.SCREEN_HEIGHT) {
                powerUps.remove(i);
                removed = true;
            }

            if (!removed && pu.checkCollision(paddle)) {
                pu.applyEffect(paddle, ball);
                activePowerUps.add(pu);
                powerUps.remove(i);
            }
        }

        Iterator<PowerUp> activeIterator = activePowerUps.iterator();
        while (activeIterator.hasNext()) {
            PowerUp activePU = activeIterator.next();
            if (activePU.isExpired()) {
                activePU.removeEffect(paddle, ball);
                activeIterator.remove();
            }
        }

        if (lives <= 0) gameOver();
        checkLevelComplete();
    }
    private void nextLevel() {
        // tăng màn lên rồi mới check có trong phạm vi mảng LEVElS không
        if (++curLevel < LEVELS.length) {
            if (activePowerUps != null) {
                Iterator<PowerUp> it = activePowerUps.iterator();
                //dung iterator để xóa phàn tử một cách an toàn
                while (it.hasNext()) {
                    PowerUp ap = it.next();
                    try {
                        ap.removeEffect(paddle, ball);
                    } catch (Exception ignored) {}
                    it.remove();
                }
            }
            if (powerUps != null) powerUps.clear(); //xóa để tránh hiện tượng vừa sang màn có powerup rơi

            bricks = Arkanoid.util.LevelLoader.loadLevel(
                    LEVELS[curLevel],
                    Constant.BRICK_WIDTH,
                    Constant.BRICK_HEIGHT
            );
            // Tắt cờ di chuyển để tránh paddle tiếp tục trôi theo phím giữ ở frame trước
            movingLeft = false;
            movingRight = false;
            ballLaunched = false;

            try { paddle.setY(height - 30); } catch (Exception ignored) {}
            paddle.setX(width / 2 - Constant.PADDLE_WIDTH / 2);

            // set lại vị trí các object
            ball.setDx(0);
            ball.setDirectionY(-1);
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - ball.getHeight() - 2);

            state = STATE_RUNNING;
            System.out.println("➡️ Level " + (curLevel + 1) + " start!");
        } else {
            state = STATE_GAME_OVER;
            System.out.println("🎉 All levels cleared! Final Score: " + score);
        }
    }




    private void resetBall() {
        ballLaunched = false;
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() - 2);
        ball.setDirectionY(-1);
        lives -= 1;
    }

    private void gameOver() {
        state = STATE_GAME_OVER;
        System.out.println("Game Over! Final Score: " + score);
    }

    /**
     * Tạm dừng game
     */
    public void pause() {
        if (STATE_RUNNING.equals(state)) {
            state = STATE_PAUSED;
        }
    }

    /**
     * Tiếp tục game sau khi tạm dừng
     */
    public void resume() {
        if (STATE_PAUSED.equals(state)) {
            state = STATE_RUNNING;
        }
    }

    /**
     * Toggle pause/resume
     */
    public void togglePause() {
        if (STATE_RUNNING.equals(state)) {
            pause();
        } else if (STATE_PAUSED.equals(state)) {
            resume();
        }
    }

    /**
     * Chuyển về menu
     */
    public void returnToMenu() {
        state = STATE_MENU;
    }

    /**
     * Kiểm tra xem có còn gạch để phá không
     */
    public boolean hasRemainingBricks() {
        for (Brick brick : bricks) {
            if (!brick.isUnbreakable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra thắng cấp độx`
     */
    private void checkLevelComplete() {
        // Khi không còn bất kỳ viên gạch phá được nào
        if (!hasRemainingBricks()) {
            System.out.println("Level " + (curLevel + 1) + " Complete! Score: " + score);
            nextLevel(); // qua màn kế tiếp (hoặc kết thúc nếu đã hết LEVELS)
        }
    }


    public Background getBackground() {
        return background;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public String getState() {
        return state;
    }

    /**
     * Kiểm tra xem điểm hiện tại có phải high score không
     */
    public boolean isHighScore() {
        return score > 0; // Có thể tích hợp với HighScoreManager nếu cần
    }

    /**
     * Lấy điểm hiện tại để hiển thị trong menu
     */
    public int getCurrentScore() {
        return score;
    }
}
