package Arkanoid;

import Arkanoid.Object.*;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance;

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

    private static final int width = 800;
    private static final int height = 600;

    /*
    .Khoi tao tro choi
     */
    public void start() {
        paddle = new Paddle(width / 2 - 50, height - 30, 100, 15, 8);
        ball = new Ball(width / 2 - 6, height / 2, 12, 4, 1, -1);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        lives = 3;
        state = "RUNNING";
        //initBricks();
    }

    /*private void initBricks() {
      TO DO hoan thanh luoi gach
    }*/
    public void handleInput(String command) {
        if ("LEFT".equals(command)) paddle.moveLeft(width);
        if ("RIGHT".equals(command)) paddle.moveRight(width);
    }

    private void resetBall() {
        ball.setX(width / 2 - 6);
        ball.setY(height / 2);
        ball.setDirectionY(-1);
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
