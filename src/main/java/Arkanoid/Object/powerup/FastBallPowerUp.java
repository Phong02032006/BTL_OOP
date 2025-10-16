package Arkanoid.Object.powerup;

import Arkanoid.Object.Ball;
import Arkanoid.Object.Paddle;
import Arkanoid.util.Constant;

/**
 * PowerUp làm bóng di chuyển nhanh hơn 50% trong thời gian giới hạn.
 */
public class FastBallPowerUp extends PowerUp {
    private double originalSpeed;

    public FastBallPowerUp(double x, double y) {
        super(x, y,
                Constant.POWERUP_SIZE,
                Constant.POWERUP_SIZE,
                "ExpandPaddle",
                Constant.POWERUP_DURATION,
                "/images/fast.png");
        this.color = Constant.POWERUP_FAST_COLOR;
    }

    @Override
    public void update() {
        y += Constant.POWERUP_SPEED;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if (ball != null) {
            originalSpeed = ball.getSpeed();
            ball.setSpeed(originalSpeed * 1.5); // tăng tốc 50%
        }
        startTime = System.currentTimeMillis(); // Bắt đầu tính thời gian hiệu lực
        setActive(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (ball != null) {
            ball.setSpeed(originalSpeed);
        }
        setActive(false);
    }

}
