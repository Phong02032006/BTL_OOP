package arkanoid.object;

import arkanoid.object.powerup.PowerUp;
import arkanoid.util.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Paddle extends MovableObject {
    private double speed;
    private PowerUp currentPowerUp;
    private Color color;
    private final Image image;
    private boolean laserEquipped = false;


    private List<Ball> tempBalls = new ArrayList<>();
    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, 0, 0);
        this.speed = speed;
        this.color = Constant.PADDLE_COLOR;
        this.image = SpriteManager.getImage("/images/paddle.png");
    }

    /**
     * Speed ở đây là khoảng mà paddle sẽ dịch chuyển mỗi khi hàm được gọi.
     * Ví dụ moveLeft thì paddle sẽ "dịch chuyển" 10 pixel tương tự như hàm moveRight.
     */
    public void moveLeft(double screenWidth) {
        x -= speed;
        if (x < Constant.BORDER_LEFT) {
            x = Constant.BORDER_LEFT;
        }
    }

    public void moveRight(double screenWidth) {
        x += speed;
        if (x + width > Constant.BORDER_RIGHT) {
            x = Constant.BORDER_RIGHT - width;
        }
    }

    /**
     * Áp dụng hiệu ứng PowerUp (nếu có)
     */
    public void applyPowerUp(PowerUp powerUp) {
        this.currentPowerUp = powerUp;
        if (powerUp != null) {
            powerUp.applyEffect(this, null);
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
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            // fallback khi ảnh chưa load (hoặc lỗi)
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        }
    }

    //  Getter / Setter bổ sung
    public double getSpeed() {
        return speed;
    }

    public boolean isLaserEquipped() {
        return laserEquipped;
    }

    public void setLaserEquipped(boolean laserEquipped) {
        this.laserEquipped = laserEquipped;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
