package Arkanoid;

import Arkanoid.Object.*;

import java.util.ArrayList;
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

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

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
            }
        }
    }

    /**
     * khoi tao vi tri cac obj
     */
    public void start() {
        paddle = new Paddle(width / 2 - 50, height - 30, Constant.PADDLE_WIDTH, Constant.PADDLE_HEIGHT, Constant.PADDLE_SPEED);
        double ballX = (width / 2) - (Constant.BALL_RADIUS / 2);
        double ballY = height - 30 - Constant.PADDLE_HEIGHT - Constant.BALL_RADIUS - 2;
        ball = new Ball(ballX, ballY, Constant.BALL_RADIUS, Constant.BALL_SPEED, 1, -1);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
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
            case "LEFT" -> movingLeft = true;
            case "RIGHT" -> movingRight = true;
            case "SPACE" -> {
                if (!ballLaunched) launchedBall();
            }
        }
    }

    public void onKeyReleased(String key) {
        switch (key) {
            case "LEFT" -> movingLeft = false;
            case "RIGHT" -> movingRight = false;
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
            if (ball.getY() + ball.getHeight() > Constant.SCREEN_HEIGHT) resetBall();
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
