package Arkanoid.Object;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class Paddle extends MovableObject {
    private double speed;
    private PowerUp currentPowerUp;

    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, 0, 0);
        this.speed = speed;
    }
    /*
        Speed ở đây là khoảng mà paddle sẽ dịch chuyển mỗi khi hàm được gọi.
        Ví dụ moveLeft thì paddle sẽ "dịch chuyển" 10 pixel tương tự như hàm moveRight.
     */
    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }
    public void applyPowerUp(PowerUp powerUp) {
        this.currentPowerUp = powerUp;
        if (powerUp != null) {
            powerUp.applyEffect(this, null); // Ball sẽ được truyền từ GameManager
        }
    }
    
    public PowerUp getCurrentPowerUp() {
        return currentPowerUp;
    }
    
    public void removeCurrentPowerUp() {
        if (currentPowerUp != null) {
            currentPowerUp.removeEffect(this, null);
            currentPowerUp = null;
        }
    }
    @Override
    public void update() {
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y, width, height);
    }
}
