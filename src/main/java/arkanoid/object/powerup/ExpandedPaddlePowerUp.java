package arkanoid.object.powerup;

import arkanoid.object.Ball;
import arkanoid.object.Paddle;
import arkanoid.util.*;

/**
 * Expand paddle in 5 seconds.
 * When it hits paddle, expand a half more than normal.
 */
public class ExpandedPaddlePowerUp extends PowerUp {
    private double originalWidth;

    public ExpandedPaddlePowerUp(double x, double y) {
        super(x, y,
                Constant.POWERUP_SIZE,
                Constant.POWERUP_SIZE,
                "ExpandPaddle",
                Constant.POWERUP_DURATION,
                "/images/expanded.png");
        this.color = Constant.POWERUP_EXPAND_COLOR;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * 1.2);
        startTime = System.currentTimeMillis();
        setActive(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(originalWidth);
        setActive(false);
    }

    @Override
    public void update() {
        y += Constant.POWERUP_SPEED;
    }


}