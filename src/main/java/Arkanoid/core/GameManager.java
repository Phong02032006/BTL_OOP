package Arkanoid.core;

import Arkanoid.Object.*;
import Arkanoid.Object.brick.Brick;
import Arkanoid.Object.powerup.PowerUp;
import Arkanoid.util.Constant;
import Arkanoid.util.LevelLoader;
import Arkanoid.util.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager {
    private static GameManager instance;
    private boolean ballLaunched;
    private int curLevel;
    private final String[] LEVELS = {"level1.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt", "level6.txt", "level7.txt"};
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
    // HỢP NHẤT: Chỉ dùng một danh sách cho tất cả bóng
    private final List<Ball> balls = new ArrayList<>();
    private List<Brick> bricks;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<PowerUp> activePowerUps = new ArrayList<>();

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

    private boolean movingLeft = false;
    private boolean movingRight = false;

    public void start() {
        SoundManager.playBackground();
        background = new Background("/images/background.png");
        paddle = new Paddle(width / 2.0 - 50, height - 30, Constant.PADDLE_WIDTH, Constant.PADDLE_HEIGHT, Constant.PADDLE_SPEED);
        curLevel = 0;

        bricks = LevelLoader.loadLevel(LEVELS[curLevel], Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);
        powerUps.clear();
        activePowerUps.clear();

        score = 0;
        lives = 3;
        state = STATE_RUNNING;

        // Bắt đầu với một quả bóng duy nhất
        addNewBallOnPaddle();
    }

    public void restart() {
        SoundManager.stopBackground();
        start();
        movingLeft = false;
        movingRight = false;
        SoundManager.playBackground();
        System.out.println("🔁 Game restarted!");
    }

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
                    laserIterator.remove(); // Xóa viên đạn
                    SoundManager.playSound("hit_brick.wav");

                    if (brick.takeHit()) { // Gạch nhận sát thương
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
        if (!STATE_RUNNING.equals(state)) return;

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

                // Va chạm với paddle
                if (ball.checkCollision(paddle) && ball.getDy() > 0) {
                    ball.bounceOff(paddle);
                    ball.setY(paddle.getY() - ball.getHeight() - 1); // Tránh bị kẹt
                    SoundManager.playSound("hit_paddle.wav");
                }

                // Va chạm với gạch
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
                        break; // Mỗi quả bóng chỉ va chạm 1 viên gạch mỗi frame
                    }
                }

                // Nếu bóng rơi ra ngoài
                if (ball.getY() + ball.getHeight() > Constant.SCREEN_HEIGHT) {
                    ballIterator.remove();
                }
            }

            // Nếu không còn quả bóng nào trên màn hình -> mất mạng
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
        // Cập nhật power-up đang rơi
        Iterator<PowerUp> puIterator = powerUps.iterator();
        while (puIterator.hasNext()) {
            PowerUp pu = puIterator.next();
            pu.update();

            if (pu.getY() > Constant.SCREEN_HEIGHT) {
                puIterator.remove();
            } else if (pu.checkCollision(paddle)) {
                // Chỉ lấy bóng đầu tiên trong list để áp dụng hiệu ứng (nếu cần)
                pu.applyEffect(paddle, balls.isEmpty() ? null : balls.get(0));
                SoundManager.playSound("power_up.wav");
                activePowerUps.add(pu);
                puIterator.remove();
            }
        }

        // Cập nhật power-up đang kích hoạt
        Iterator<PowerUp> activeIterator = activePowerUps.iterator();
        while (activeIterator.hasNext()) {
            PowerUp activePU = activeIterator.next();
            if (activePU.isExpired()) {
                // Chỉ lấy bóng đầu tiên trong list để gỡ hiệu ứng (nếu cần)
                activePU.removeEffect(paddle, balls.isEmpty() ? null : balls.get(0));
                activeIterator.remove();
            }
        }
    }

    private void nextLevel() {
        curLevel++;
        if (curLevel < LEVELS.length) {
            // Xóa hết các hiệu ứng và power-up cũ
            activePowerUps.forEach(pu -> pu.removeEffect(paddle, balls.isEmpty() ? null : balls.get(0)));
            activePowerUps.clear();
            powerUps.clear();

            // Tải màn mới
            bricks = LevelLoader.loadLevel(LEVELS[curLevel], Constant.BRICK_WIDTH, Constant.BRICK_HEIGHT);

            // Reset paddle và bóng
            paddle.setX(width / 2.0 - Constant.PADDLE_WIDTH / 2.0);
            paddle.setY(height - 30);

            addNewBallOnPaddle();

            System.out.println("Level " + (curLevel + 1) + " start!");
        } else {
            state = STATE_GAME_OVER;
            System.out.println("All levels cleared! Final Score: " + score);
        }
    }

    /**
     * Xử lý khi người chơi mất một mạng
     */
    private void loseLife() {
        lives--;
        SoundManager.playSound("lose_life.wav");
        if (lives > 0) {
            // Xóa hết các hiệu ứng
            activePowerUps.forEach(pu -> pu.removeEffect(paddle, null));
            activePowerUps.clear();
            // Tạo lại một quả bóng mới
            addNewBallOnPaddle();
        }
    }



    private void fireLasers() {
        // Tạo 2 viên đạn từ 2 bên của paddle
        double laserX1 = paddle.getX() + 7; // Vị trí nòng trái
        double laserX2 = paddle.getX() + paddle.getWidth() - 11; // Vị trí nòng phải
        double laserY = paddle.getY();

        lasers.add(new Laser(laserX1, laserY));
        lasers.add(new Laser(laserX2, laserY));

        SoundManager.playSound("laser_shoot.wav"); // Bạn cần có file âm thanh này
    }

    /**
     * Tạo một quả bóng mới trên thanh đỡ và reset trạng thái phóng.
     */
    private void addNewBallOnPaddle() {
        balls.clear(); // Xóa hết bóng cũ
        ballLaunched = false;
        double ballX = paddle.getX() + paddle.getWidth() / 2 - Constant.BALL_RADIUS;
        double ballY = paddle.getY() - Constant.BALL_RADIUS * 2 - 2;
        Ball newBall = new Ball(ballX, ballY, Constant.BALL_RADIUS, Constant.BALL_SPEED, 0, -1);
        balls.add(newBall);
    }

    private void gameOver() {
        state = STATE_GAME_OVER;
        SoundManager.playSound("end_game.wav");
        System.out.println("Game Over! Final Score: " + score);
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

    /**
     * Tạo thêm bóng phụ (power-up Multi-ball)
     */
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

    /**
     * Xóa tất cả bóng phụ, chỉ giữ lại một quả.
     */
    public void removeExtraBalls() {
        if (balls.size() > 1) {
            Ball firstBall = balls.get(0);
            balls.clear();
            balls.add(firstBall);
        }
    }

    // --- Getters ---
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

    // --- Các phương thức quản lý trạng thái game ---
    public void togglePause() {
        if (STATE_RUNNING.equals(state)) state = STATE_PAUSED;
        else if (STATE_PAUSED.equals(state)) state = STATE_RUNNING;
    }

    public void returnToMenu() {
        state = STATE_MENU;
    }
}