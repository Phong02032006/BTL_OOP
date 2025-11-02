package arkanoid.core;

import arkanoid.object.powerup.DoubleBall;
import arkanoid.util.GameMode;

import arkanoid.object.*;
import arkanoid.object.brick.Brick;
import arkanoid.object.powerup.PowerUp;
import arkanoid.util.Constant;
import arkanoid.util.LevelLoader;
import arkanoid.util.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager {
    private GameMode gameMode = GameMode.NORMAL;
    private static GameManager instance;
    private boolean ballLaunched;
    private int curLevel;
    private final String[] LEVELS = {"level1.txt", "level2.txt", "level3.txt"};
    private final String[] FUNNY_LEVELS = {"level4.txt", "level5.txt", "level6.txt", "level7.txt"};
    private final List<Laser> lasers = new ArrayList<>();
    private static final long LASER_COOLDOWN = 300;
    private long lastLaserFireTime = 0;

    private GameManager() {
      }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private Background background;
    private Paddle paddle;
    private final List<Ball> balls = new ArrayList<>();
    private List<Brick> bricks;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<PowerUp> activePowerUps = new ArrayList<>();

    private int score;
    private int lives;
    private String state;

    private static final int width = Constant.SCREEN_WIDTH;
    private static final int height = Constant.SCREEN_HEIGHT;

    private boolean movingLeft = false;
    private boolean movingRight = false;

    public void start(GameMode mode) {
        this.gameMode = mode;

        String[] levelsToUse = (mode == GameMode.FUNNY) ? FUNNY_LEVELS : LEVELS;

        try {
            SoundManager.stopBackground();
        } catch (Exception ignored) {
        }
        SoundManager.playBackground();

        // Reset
        lasers.clear();
        lastLaserFireTime = 0;
        ballLaunched = false;
        movingLeft = false;
        movingRight = false;
        curLevel = 0;

        background = new Background("/images/background.png");
        paddle = new Paddle(
                width / 2.0 - 50, height - 30,
                Constant.PADDLE_WIDTH, Constant.PADDLE_HEIGHT, Constant.PADDLE_SPEED
        );

        bricks = LevelLoader.loadLevel(levelsToUse[curLevel], Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT, gameMode);

        powerUps.clear();
        activePowerUps.clear();
        balls.clear();

        score = 0;
        lives = 3;
        state = Constant.STATE_RUNNING;

        addNewBallOnPaddle();
    }

    public void start() {
        start(GameMode.NORMAL);
    }

    public void restart() {
        start(gameMode);
        System.out.println("🔁 Game restarted!");
    }

    /**
     * increase ball speed in funny mode
      */
    private void tuneBall(Ball ball) {
        if (gameMode == GameMode.FUNNY) {
            ball.setSpeed(Constant.BALL_SPEED * 1.15);
        }
    }

    public void onKeyPressed(String key) {
        switch (key) {
            case "LEFT":
                movingLeft = true;
                break;
            case "RIGHT":
                movingRight = true;
                break;
            case "SPACE":
                if (!ballLaunched) {
                    launchBall();
                }
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

    private void launchBall() {
        if (!balls.isEmpty()) {
            ballLaunched = true;
            Random rand = new Random();
            double dirX = (rand.nextDouble() * 1.6 - 0.8);
            if (Math.abs(dirX) < 0.3) {
                dirX = Math.copySign(0.3, dirX);
            }

            Ball mainBall = balls.get(0);
            mainBall.setDx(dirX);
            mainBall.setDirectionY(-1);
        }
    }

    private void updateLasers() {
        Iterator<Laser> laserIterator = lasers.iterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            laser.update();
            if (laser.isOffScreen()) {
                laserIterator.remove();
                continue;
            }


            for (int i = bricks.size() - 1; i >= 0; i--) {
                Brick brick = bricks.get(i);
                if (laser.checkCollision(brick)) {
                    laserIterator.remove();
                    SoundManager.playSound("hit_brick.wav");

                    if (brick.takeHit()) {
                        if (brick.hasPowerUp()) {
                            powerUps.add(brick.getPowerUp());
                        }
                        bricks.remove(i);
                        score += 10;
                    }
                    break;
                }
            }
        }
    }

    public void update() {
        if (!Constant.STATE_RUNNING.equals(state)) return;

        if (movingLeft) paddle.moveLeft(width);
        if (movingRight) paddle.moveRight(width);

        if (!ballLaunched) {

            if (!balls.isEmpty()) {
                Ball ball = balls.get(0);
                ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
                ball.setY(paddle.getY() - ball.getHeight() - 2);
            }
        } else {
            if (paddle.isLaserEquipped()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastLaserFireTime > LASER_COOLDOWN) {
                    fireLasers();
                    lastLaserFireTime = currentTime;
                }
            }

            Iterator<Ball> ballIterator = balls.iterator();
            while (ballIterator.hasNext()) {
                Ball ball = ballIterator.next();
                ball.update();

                // paddle collide
                if (ball.checkCollision(paddle) && ball.getDy() > 0) {
                    ball.sweepBounceOff(paddle);
                    ball.setY(paddle.getY() - ball.getHeight() - 1); // Tránh bị kẹt
                    SoundManager.playSound("hit_paddle.wav");
                }

                // brick collide
                for (int i = bricks.size() - 1; i >= 0; i--) {
                    Brick brick = bricks.get(i);
                    if (ball.checkCollision(brick)) {
                        ball.sweepBounceOff(brick);
                        SoundManager.playSound("hit_brick.wav");

                        if (brick.takeHit()) {
                            if (brick.hasPowerUp()) {
                                powerUps.add(brick.getPowerUp());
                            }
                            bricks.remove(i);
                            score += 10;
                        }
                        break; // one ball collide with one brick per frame.
                    }
                }

                // ball out of screen.
                if (ball.getY() + ball.getHeight() > Constant.SCREEN_HEIGHT) {
                    ballIterator.remove();
                }
            }

            if (balls.isEmpty()) {
                loseLife();
            }
            updateLasers();
        }

        updatePowerUps();

        if (lives <= 0) {
            gameOver();
        }
        checkLevelComplete();
    }

    private void updatePowerUps() {
        // powerup list when it is falling.
        Iterator<PowerUp> puIterator = powerUps.iterator();
        while (puIterator.hasNext()) {
            PowerUp pu = puIterator.next();
            pu.update();

            if (pu.getY() > Constant.SCREEN_HEIGHT) {
                puIterator.remove();
            } else if (pu.checkCollision(paddle)) {
                // choose only the first ball in list if it's needed.
                pu.applyEffect(paddle, balls.isEmpty() ? null : balls.get(0));
                SoundManager.playSound("power_up.wav");
                activePowerUps.add(pu);
                puIterator.remove();
            }
        }

        // active powerup check.
        Iterator<PowerUp> activeIterator = activePowerUps.iterator();
        while (activeIterator.hasNext()) {
            PowerUp activePU = activeIterator.next();
            if (activePU.isExpired()) {
                // only remove the effect of the first ball when the double ball is active.
                activePU.removeEffect(paddle, balls.isEmpty() ? null : balls.get(0));
                activeIterator.remove();
            }
        }
    }

    private void nextLevel() {
        String[] levelsToUse;
        if (gameMode == GameMode.FUNNY) {
            levelsToUse = FUNNY_LEVELS;
        } else {
            levelsToUse = LEVELS;
        }

        curLevel++;

        if (curLevel < levelsToUse.length) {
            activePowerUps.forEach(pu -> pu.removeEffect(paddle, balls.isEmpty() ? null : balls.get(0)));
            activePowerUps.clear();
            powerUps.clear();
            lasers.clear();

            bricks = LevelLoader.loadLevel(levelsToUse[curLevel], Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT, gameMode);

            // Reset location of paddle and ball.
            paddle.setX(width / 2.0 - Constant.PADDLE_WIDTH / 2.0);
            paddle.setY(height - 30);
            addNewBallOnPaddle();

            System.out.println("Level " + (curLevel + 1) + " start!");

        } else {
            state = Constant.STATE_GAME_OVER;
            System.out.println("All levels for " + gameMode + " mode cleared! Final Score: " + score);
        }
    }

    /**
     * Lose life.
     */
    private void loseLife() {
        lives--;
        SoundManager.playSound("lose_life.wav");
        if (lives > 0) {
            // remove all the PU
            activePowerUps.forEach(pu -> pu.removeEffect(paddle, null));
            activePowerUps.clear();
            lasers.clear();
            lastLaserFireTime = 0;
            addNewBallOnPaddle();
        }
    }

    private void fireLasers() {
        double laserX1 = paddle.getX() + 7;
        double laserX2 = paddle.getX() + paddle.getWidth() - 11;
        double laserY = paddle.getY();

        lasers.add(new Laser(laserX1, laserY));
        lasers.add(new Laser(laserX2, laserY));

        SoundManager.playSound("laser_shoot.wav");
    }

    /**
     * Create new ball on the paddle, reset tune ball.
     */
    private void addNewBallOnPaddle() {
        balls.clear(); // clear old ball.
        ballLaunched = false;
        double ballX = paddle.getX() + paddle.getWidth() / 2 - Constant.BALL_RADIUS;
        double ballY = paddle.getY() - Constant.BALL_RADIUS * 2 - 2;
        Ball newBall = new Ball(ballX, ballY, Constant.BALL_RADIUS, Constant.BALL_SPEED, 0, -1);
        tuneBall(newBall);
        balls.add(newBall);
    }

    private void gameOver() {
        state = Constant.STATE_GAME_OVER;
        SoundManager.playSound("end_game.wav");
        System.out.println("Game Over! Final Score: " + score);
    }

    public void pause() {
        if (Constant.STATE_RUNNING.equals(state)) {
            state = Constant.STATE_PAUSED;
        }
    }

    public void resume() {
        if (Constant.STATE_PAUSED.equals(state)) {
            state = Constant.STATE_RUNNING;
        }
    }

    private void checkLevelComplete() {
        if (!hasRemainingBricks()) {
            System.out.println("Level " + (curLevel + 1) + " Complete! Score: " + score);
            nextLevel();
        }
    }

    public boolean hasRemainingBricks() {
        for (Brick brick : bricks) {
            if (!brick.isUnbreakable()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveMultiBallPowerUp() {
        for (PowerUp pu : activePowerUps) {
            // check powerup is double ball or not.
            if (pu instanceof DoubleBall) {
                return true;
            }
        }
        return false;
    }

    public void spawnExtraBalls() {
        if (balls.isEmpty()) return;
        int currentBallCount = balls.size();
        for (int i = 0; i < currentBallCount; i++) {
            Ball sourceBall = balls.get(i);

            Ball newBall = new Ball(
                    sourceBall.getX(),
                    sourceBall.getY(),
                    Constant.BALL_RADIUS,
                    Constant.BALL_SPEED,
                    -sourceBall.getDx(),
                    sourceBall.getDy()
            );

            balls.add(newBall);
        }
    }

    public void removeExtraBalls() {
        if (balls.size() > 1) {
            Ball firstBall = balls.get(0);
            balls.clear();
            balls.add(firstBall);
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public Background getBackground() {
        return background;
    }

    public Paddle getPaddle() {
        return paddle;
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

    public List<Laser> getLasers() {
        return lasers;
    }

    public void returnToMenu() {
        state = Constant.STATE_MENU;
    }

    public int getCurLevel() {
        return this.curLevel;
    }
}