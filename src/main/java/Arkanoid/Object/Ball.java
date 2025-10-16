package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import Arkanoid.util.*;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed;
    private final Image image;
    private double prevX;
    private double prevY;

    public Ball(double x, double y, double radius, double speed, int dx, int dy) {
        super(x, y, radius, radius, dx, dy);
        this.speed = speed;
        this.image = SpriteManager.getImage("/images/ball.png");
    }

    /**
     * kiểm tra va chạm giữa hình tròn (ball) và hình chữ nhật (brick/paddle)
     */
    public boolean checkCollision(GameObject other) {
        // tìm tâm của bóng
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double radius = width / 2;

        // tìm điểm gần nhất trên hình chữ nhật so với tâm bóng
        double closestX = Math.max(other.x, Math.min(ballCenterX, other.x + other.width));
        double closestY = Math.max(other.y, Math.min(ballCenterY, other.y + other.height));

        // tính khoảng cách từ điểm gần nhất đến tâm bóng
        double distanceX = ballCenterX - closestX;
        double distanceY = ballCenterY - closestY;
        double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);

        // va chạm nếu khoảng cách <= bán kính
        return distanceSquared <= (radius * radius);
    }

    /**
     * xử lý phản xạ khi bóng va chạm với paddle hoặc các đối tượng khác
     * nếu va vào Paddle phản xạ theo góc tùy thuộc vào vị trí va chạm (để tránh bóng đi thẳng đứng).
     * nếu va vào Brick hoặc tường: phản xạ theo hướng tiếp xúc và đặt lại vị trí bóng ra khỏi đối tượng để tránh bị kẹt.
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

    public boolean sweepBounceOff(GameObject other) {
        if (!checkCollision(other)) return false;

        double eps = 0.08;

        // biên khung trước của bóng
        double pL = prevX;
        double pR = prevX + width;
        double pT = prevY;
        double pB = prevY + height;

        // biên của other (gạch/paddle/tường)
        double oL = other.x;
        double oR = other.x + other.width;
        double oT = other.y;
        double oB = other.y + other.height;

        // ưu tiên trục Y nếu frame trước còn ở trên/dưới hoàn toàn
        if (pB <= oT) {                  // từ trên xuống
            y = oT - height - eps;
            dy = -Math.abs(dy);
            return true;
        }
        if (pT >= oB) {                  // từ dưới lên
            y = oB + eps;
            dy = Math.abs(dy);
            return true;
        }
        if (pR <= oL) {                  // từ trái sang
            x = oL - width - eps;
            dx = -Math.abs(dx);
            return true;
        }
        if (pL >= oR) {                  // từ phải sang
            x = oR + eps;
            dx = Math.abs(dx);
            return true;
        }

        // fallback để chọn trục có chèn nhỏ hơn
        double overlapL = (x + width) - oL;
        double overlapR = oR - x;
        double overlapT = (y + height) - oT;
        double overlapB = oB - y;

        double minX = Math.min(overlapL, overlapR);
        double minY = Math.min(overlapT, overlapB);

        if (minX < minY) { // va theo X
            if (overlapL < overlapR) {
                x = oL - width - eps;
                dx = -Math.abs(dx);
            } else {
                x = oR + eps;
                dx = Math.abs(dx);
            }
        } else {           // va theo Y
            if (overlapT < overlapB) {
                y = oT - height - eps;
                dy = -Math.abs(dy);
            } else {
                y = oB + eps;
                dy = Math.abs(dy);
            }
        }
        return true;
    }


    @Override
    public void move() {
        prevX = x;
        prevY = y;
        x += dx * speed;
        y += dy * speed;

        //left
        if (x <= 0) {
            x = 0;
            dx = Math.abs(dx);
            SoundManager.playSound("hit_wall.wav");
        }

        //right
        if (x + width >= Constant.SCREEN_WIDTH) {
            x = Constant.SCREEN_WIDTH - width;
            dx = -Math.abs(dx);
            SoundManager.playSound("hit_wall.wav");
        }

        //upper wall
        if (y <= 0) {
            y = 0;
            dy = Math.abs(dy);
            SoundManager.playSound("hit_wall.wav");
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
