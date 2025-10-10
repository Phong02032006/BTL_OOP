package Arkanoid.Object;

import Arkanoid.util.Constant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp trừu tượng PowerUp mô tả các hiệu ứng đặc biệt trong game Arkanoid.
 * - Mỗi PowerUp có loại (type) và thời gian hiệu lực (duration).
 * - Khi va chạm với Paddle, PowerUp được kích hoạt và tạo hiệu ứng tạm thời.
 */
public abstract class PowerUp extends GameObject {
    protected String type;
    protected double duration;
    protected boolean active;
    protected Color color;
    protected long startTime;

    public PowerUp(double x, double y, double width, double height, String type, double duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
        this.active = false;
    }

    public String getType() {
        return type;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void update() {
        // PowerUp rơi xuống với tốc độ cố định
        y += Constant.POWERUP_SPEED;

        // Nếu rơi ra khỏi màn hình → có thể bị xoá (GameManager xử lý)
        if (y > Constant.SCREEN_HEIGHT) {
            // Ví dụ: active = false;
        }
    }

    public boolean checkCollision(GameObject other) {
        return this.x < other.getX() + other.getWidth() &&
                this.x + this.width > other.getX() &&
                this.y < other.getY() + other.getHeight() &&
                this.y + this.height > other.getY();
    }

    public boolean isExpired() {
        if (!active) return false;
        long elapsed = System.currentTimeMillis() - startTime;
        return elapsed >= duration;
    }


    public abstract void applyEffect(Paddle paddle, Ball ball);


    public abstract void removeEffect(Paddle paddle, Ball ball);


    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }
}
