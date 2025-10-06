package Arkanoid.Object;

import Arkanoid.util.Constant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * PowerUp làm bóng di chuyển nhanh hơn 50% trong thời gian giới hạn.
 */
public class FastBallPowerUp extends PowerUp {
    private double originalSpeed;

    public FastBallPowerUp(double x, double y) {
        super(x, y,
                Constant.POWERUP_SIZE,
                Constant.POWERUP_SIZE,
                "FastBall",
                Constant.POWERUP_DURATION);
        this.color = Constant.POWERUP_FAST_COLOR; // 🎨 Màu riêng
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
        setActive(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (ball != null) {
            ball.setSpeed(originalSpeed);
        }
        setActive(false);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x, y, width, height);
    }
}
