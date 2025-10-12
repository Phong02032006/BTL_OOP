package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import Arkanoid.util.*;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed;
    private final Image image;

    public Ball(double x, double y, double radius, double speed, int dx, int dy) {
        super(x, y, radius, radius, dx, dy);
        this.speed = speed;
        this.image = SpriteManager.getImage("/images/ball.png");
    }

    /**
     * Kiểm tra va chạm giữa hình tròn (ball) và hình chữ nhật (brick/paddle)
     */
    public boolean checkCollision(GameObject other) {
        // Tìm tâm của bóng (hình tròn)
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double radius = width / 2;

        // Tìm điểm gần nhất trên hình chữ nhật so với tâm bóng
        double closestX = Math.max(other.x, Math.min(ballCenterX, other.x + other.width));
        double closestY = Math.max(other.y, Math.min(ballCenterY, other.y + other.height));

        // Tính khoảng cách từ điểm gần nhất đến tâm bóng
        double distanceX = ballCenterX - closestX;
        double distanceY = ballCenterY - closestY;
        double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);

        // Va chạm nếu khoảng cách <= bán kính
        return distanceSquared <= (radius * radius);
    }

    /**
     * Xử lý phản xạ khi bóng va chạm với paddle hoặc các đối tượng khác (brick, tường...).
     * Nếu va vào Paddle phản xạ theo góc tùy thuộc vào vị trí va chạm (để tránh bóng đi thẳng đứng).
     * Nếu va vào Brick hoặc tường: phản xạ theo hướng tiếp xúc và reposition bóng ra khỏi đối tượng để tránh bị kẹt.
     *
     * @param other Đối tượng mà bóng va chạm.
     */
    public void bounceOff(GameObject other) {
        // Tâm bóng và tâm đối tượng va chạm
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double otherCenterX = other.x + other.width / 2;
        double otherCenterY = other.y + other.height / 2;

        double deltaX = ballCenterX - otherCenterX;
        double deltaY = ballCenterY - otherCenterY;

        if (other instanceof Paddle) {
            // Đưa bóng ra khỏi paddle (tránh dính)
            y = other.y - height;

            // offset nằm trong [-1, 1], thể hiện vị trí va chạm trên paddle
            double offset = deltaX / (other.width / 2);
            offset = Math.max(-1, Math.min(1, offset));

            // Giới hạn góc lệch tối thiểu để tránh bóng bay thẳng đứng
            double minAngle = Math.toRadians(15);
            double maxAngle = Math.toRadians(60);

            double angle = offset * (maxAngle - minAngle);
            if (offset >= 0) {
                angle += minAngle;
            } else {
                angle -= minAngle;
            }

            // Tính hướng phản xạ mới
            dx = Math.sin(angle);
            dy = -Math.cos(angle);

            // Đẩy nhẹ bóng để tránh dính paddle
            x += dx * 0.1;
            y += dy * 0.1;

        } else {
            // Xử lý va chạm với brick hoặc tường
            double absDX = Math.abs(deltaX);
            double absDY = Math.abs(deltaY);
            double epsilon = 0.1; // khoảng cách nhỏ để tránh dính

            if (absDY > absDX) {
                if (deltaY > 0) {
                    // Bóng va từ dưới lên
                    y = other.y + other.height + epsilon;
                    dy = Math.abs(dy); // bật xuống
                } else {
                    // Bóng va từ trên xuống
                    y = other.y - height - epsilon;
                    dy = -Math.abs(dy); // bật lên
                }
            } else {
                // Va theo chiều ngang
                if (deltaX > 0) {
                    // Bóng va từ phải sang trái
                    x = other.x + other.width + epsilon;
                    dx = Math.abs(dx); // bật sang phải
                } else {
                    // Bóng va từ trái sang phải
                    x = other.x - width - epsilon;
                    dx = -Math.abs(dx); // bật sang trái
                }
            }
        }
    }


    @Override
    public void move() {
        x += dx * speed;
        y += dy * speed;

        double radius = width / 2;

        //  Bật trái
        if (x <= 0) {
            x = 0;             // 👈 đặt sát mép
            dx = Math.abs(dx); // bật qua phải
        }

        //  Bật phải
        if (x + width >= Constant.SCREEN_WIDTH) {
            x = Constant.SCREEN_WIDTH - width;  // đặt sát mép phải
            dx = -Math.abs(dx);                 // bật qua trái
        }

        //  Bật trên
        if (y <= 0) {
            y = 0;
            dy = Math.abs(dy);
        }
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            gc.setFill(Color.YELLOW);
            gc.fillOval(x, y, width, height);
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirectionY(int dy) {
        this.dy = dy;
    }

}
