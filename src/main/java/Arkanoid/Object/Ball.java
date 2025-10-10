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
        this.image = SpriteManager.getImage("/images/ball_yellow.bmp");
    }
    /*
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

    public void bounceOff(GameObject other) {
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double otherCenterX = other.x + other.width / 2;
        double otherCenterY = other.y + other.height / 2;

        double deltaX = ballCenterX - otherCenterX;
        double deltaY = ballCenterY - otherCenterY;

        if (other instanceof Paddle) {
            // 1. Đưa bóng ra khỏi paddle
            y = other.y - height;

            // 2. offset [-1, 1]
            double offset = (ballCenterX - otherCenterX) / (other.width / 2);
            offset = Math.max(-1, Math.min(1, offset));

            // 3. Giới hạn góc lệch
            double maxAngle = Math.toRadians(60); // max lệch 60°
            double angle = offset * maxAngle;

            // 4. Tính dx, dy từ góc
            dx = Math.sin(angle);
            dy = -Math.cos(angle);

            // 5. Bảo toàn vận tốc
            double speedLength = speed;
            x += dx * 0.1; // đẩy nhẹ ra khỏi paddle để tránh dính
            y += dy * 0.1;
        }
        else {
            // brick / tường
            if (Math.abs(deltaY) > Math.abs(deltaX)) {
                dy = -dy;
            } else {
                dx = -dx;
            }
        }
    }

    @Override
    public void move(){
        x += dx *  speed;
        y += dy * speed;

        //BẬT NGANG
        if(x<=0){
            x=0 + width;
            dx= -dx;
        }
        if(x+ width>=800){
            x=800 - width;
            dx = -dx;
        }
        //BẬT DỌC
        if(y<=0){
            y=0;
            dy= -dy;
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
        }
        gc.setFill(Color.YELLOW);
        gc.fillOval(x, y, width, height);
    }

    public double getSpeed() { return speed; }

    public void setSpeed(double speed) { this.speed = speed;}

    public void setDirectionY(int dy){
        this.dy = dy;
    }

}
