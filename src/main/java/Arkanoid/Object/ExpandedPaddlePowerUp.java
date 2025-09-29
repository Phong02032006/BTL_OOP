package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ExpandedPaddlePowerUp extends PowerUp {
    private double originalWidth;

    public ExpandedPaddlePowerUp(double x, double y) {
        super(x, y, 20, 20, "ExpandPaddle", 5000);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * 1.5);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.setWidth(originalWidth);
    }

    @Override
    public void update() {
        y += 2;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillOval(x, y, width, height);
    }
}
