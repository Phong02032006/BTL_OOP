package arkanoid.object.powerup;

import arkanoid.object.Ball;
import arkanoid.object.Paddle;
import arkanoid.util.Constant;

/**
 * Effect induce speed of ball half as much normal.
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
            ball.setSpeed(originalSpeed * 1.5);
        }
        startTime = System.currentTimeMillis(); // start time apply effect
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
