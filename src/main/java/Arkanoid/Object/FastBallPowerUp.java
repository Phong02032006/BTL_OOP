package Arkanoid.Object;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class FastBallPowerUp extends PowerUp {
    private double originalSpeed;
    public FastBallPowerUp(double x, double y) {
        super(x, y, 20, 20, "FastBall", 5000);
    }
    @Override
    public void update(){
        y += 4;
    }
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        originalSpeed = ball.getSpeed();
        ball.setSpeed(originalSpeed * 1.5); // tăng tốc độ 50%
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(originalSpeed);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.PURPLE);
        gc.fillOval(x, y, width, height);
    }
}
