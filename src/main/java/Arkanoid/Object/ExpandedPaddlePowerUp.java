package Arkanoid.Object;

import Arkanoid.util.*;

/**
 * PowerUp mở rộng Paddle tạm thời trong 5 giây.
 * Khi Paddle chạm, kích thước sẽ tăng 1.5 lần, sau đó trở lại bình thường.
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
        // PowerUp rơi xuống
        y += Constant.POWERUP_SPEED;
    }


}
