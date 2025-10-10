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

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
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

    private static final int width = Constant.SCREEN_WIDTH;
    private static final int height = Constant.SCREEN_HEIGHT;

    /*
    .Khoi tao tro choi
     */

    private void initBricks() {
        bricks = new ArrayList<>();
        double offsetX = (width - Constant.BRICK_COLUMNS * Constant.BRICK_WIDTH) / 2; //khoang cach tu mep
        double offsetY = 60;

        for (int row = 0; row < Constant.BRICK_ROWS; row++) {
            for (int col = 0; col < Constant.BRICK_COLUMNS; col++) {
                double x = offsetX + col * Constant.BRICK_WIDTH; // vi tri cua tung vien gach
                double y = offsetY + row * Constant.BRICK_HEIGHT;
                Brick brick;

                if (row == 0) {
                    brick = new StrongBrick(x, y, Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT, 2);
                } else if (row == 1) {
                    brick = new NormalBrick(x, y, Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);
                } else if (row == 2) {
                    // brick = new BonusBrick(x,y,Constant.BRICK_WIDTH,Constant.BRICK_HEIGHT, 1);
                    brick = new NormalBrick(x, y, Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);

                } else if (row == 3) {
                    brick = new UnbreakableBrick(x, y, Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);
                } else {
                    brick = new NormalBrick(x, y, Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);
                }
                bricks.add(brick);
                if (Math.random() < 0.2) { // 20% viên gạch có powerup
                    double px = x + Constant.BRICK_WIDTH / 2 - 10; // đặt powerup giữa viên gạch
                    double py = y + Constant.BRICK_HEIGHT / 2 - 10;

                    PowerUp powerUp;
                    if (Math.random() < 0.5) {
                        powerUp = new ExpandedPaddlePowerUp(px, py);
                    } else {
                        powerUp = new FastBallPowerUp(px, py);
                    }

                    brick.setPowerUp(powerUp);
                }
            }
        }
    }

    /**
     * khoi tao vi tri cac obj
     */
    public void start() {
        background = new Background("/images/background.png");
        paddle = new Paddle(width / 2 - 50, height - 30, Constant.PADDLE_WIDTH, Constant.PADDLE_HEIGHT, Constant.PADDLE_SPEED);
        double ballX = (width / 2) - (Constant.BALL_RADIUS / 2);
        double ballY = height - 30 - Constant.PADDLE_HEIGHT - Constant.BALL_RADIUS - 2;
        ball = new Ball(ballX, ballY, Constant.BALL_RADIUS, Constant.BALL_SPEED, 1, -1);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        state = "RUNNING";

        // khoi tao bricks
        initBricks();
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

    private void launchedBall(){
        ballLaunched = true;

        java.util.Random rand = new java.util.Random();
        //random hướng ngang: -0.8 -> + 0.8 tránh bay thẳng đứng
        double dirX= (rand.nextDouble() * 1.6 - 0.8);

        //copy sign se giu nguyen dau cua dirX Vidu : (0.3, -0.05) -> -0.3
        if(Math.abs(dirX) < 0.3) dirX = Math.copySign(0.3, dirX);

        ball.setDx(dirX);
        ball.setDirectionY(-1);
    }

    public void update() {
        if (!"RUNNING".equals(state)) return;

        if (movingLeft)  paddle.moveLeft(width);
        if (movingRight) paddle.moveRight(width);

        if (!ballLaunched) {
            ball.setX(paddle.getX() + paddle.getWidth()/2 - ball.getWidth()/2);
            ball.setY(paddle.getY() - paddle.getHeight()/2 - 2);
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
    }

    private void resetBall() {
        ballLaunched = false;
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() - 2);
        ball.setDirectionY(-1);
        lives -=1;
    }

    private void gameOver() {
        state = "GAME_OVER";
        System.out.println("Game Over! Final Score: " + score);

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
}
