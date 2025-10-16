package Arkanoid.Object;

import Arkanoid.util.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image; // Thêm import
import javafx.scene.paint.Color;

public class Laser extends GameObject {

    private Image image;

    public Laser(double x, double y) {
        super(x, y, Constant.LASER_WIDTH, Constant.LASER_HEIGHT);
        // Load sprite ngay khi tạo đối tượng
        this.image = SpriteManager.getImage("/images/laser.png");
    }

    public void update() {
        y -= Constant.LASER_SPEED;
    }

    public boolean isOffScreen() {
        return y + height < 0;
    }

    public boolean checkCollision(GameObject other) {
        return this.x < other.getX() + other.getWidth() &&
                this.x + this.width > other.getX() &&
                this.y < other.getY() + other.getHeight() &&
                this.y + this.height > other.getY();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(x, y, width, height);
        }
    }
}