package Arkanoid.Object;

import Arkanoid.util.Constant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp Ball mô tả quả bóng trong game Arkanoid.
 * - Kế thừa MovableObject (có x, y, width, height, dx, dy)
 * - Tự di chuyển và phản xạ khi va chạm với Paddle, Brick, tường hoặc trần.
 */
public class Ball extends MovableObject {
    private double speed;
    private double directionX;
    private double directionY;

    public Ball(double x, double y, double radius, double speed, int dx, int dy) {
        super(x, y, radius, radius, dx, dy);
        this.speed = speed;
        this.directionX = dx;
        this.directionY = dy;
    }

    /**
     * Tính vector từ tâm bóng đến điểm gần nhất trên hình chữ nhật khác.
     * Ý tưởng:
     * - Lấy tâm hình tròn C(cx, cy), bán kính r.
     * - "Ép" (clamp) C vào biên của hình chữ nhật để tìm điểm gần nhất P(px, py).
     * - Trả về mảng {dx, dy, r}, trong đó:
     * dx = cx - px → khoảng cách theo trục X
     * dy = cy - py → khoảng cách theo trục Y
     * r = bán kính bóng
     */
    private double[] getCollisionVector(GameObject other) {
        double cx = x + width / 2;
        double cy = y + height / 2;
        double r = width / 2;

        double nearX = Math.max(other.x, Math.min(cx, other.x + other.width));
        double nearY = Math.max(other.y, Math.min(cy, other.y + other.height));

        double dx = cx - nearX;
        double dy = cy - nearY;
        return new double[]{dx, dy, r};
    }

    /**
     * Kiểm tra va chạm giữa hình tròn (ball) và hình chữ nhật (other).
     */
    public boolean checkCollision(GameObject other) {
        double[] v = getCollisionVector(other);
        double dx = v[0], dy = v[1], r = v[2];
        return (dx * dx + dy * dy) <= (r * r);
    }

    /**
     * Nếu va với Paddle → phản xạ theo góc dựa trên vị trí chạm.
     * Nếu va với Brick hoặc vật thể khác → phản xạ đơn giản theo cạnh va chạm.
     * Không xử lý tường, trần, đáy (do hàm update() lo).
     */
    public void bounceOff(GameObject other) {
        double[] v = getCollisionVector(other);
        double dxCenter = v[0];
        double dyCenter = v[1];

        //  Va chạm Paddle → phản xạ theo góc
        if (other instanceof Paddle) {
            double circleCenterX = x + width / 2;
            double paddleCenterX = other.x + other.width / 2;
            double distanceFromCenter = circleCenterX - paddleCenterX;

            double normalized = distanceFromCenter / (other.width / 2);
            normalized = Math.max(-1, Math.min(1, normalized));

            double minAngle = Math.toRadians(30);
            double maxAngle = Math.toRadians(150);
            double angle = ((normalized + 1) / 2) * (maxAngle - minAngle) + minAngle;

            double currentSpeed = Math.sqrt(this.dx * this.dx + this.dy * this.dy);
            this.dx = currentSpeed * Math.cos(angle - Math.PI / 2);
            this.dy = -currentSpeed * Math.sin(angle - Math.PI / 2);
            return;
        }

        //  Va chạm Brick hoặc vật thể khác → phản xạ đơn giản
        if (Math.abs(dxCenter) > Math.abs(dyCenter)) {
            this.dx = -this.dx;
            if (dxCenter > 0) x = other.x + other.width;
            else x = other.x - width;
        } else {
            this.dy = -this.dy;
            if (dyCenter > 0) y = other.y + other.height;
            else y = other.y - height;
        }
    }

    /**
     * Cập nhật vị trí bóng mỗi frame.
     * Xử lý phản xạ với tường và trần.
     */
    @Override
    public void update() {
        move();

        //  Tường trái/phải
        if (x <= 0) {
            x = 0;
            dx = -dx;
        } else if (x + width >= Constant.SCREEN_WIDTH) {
            x = Constant.SCREEN_WIDTH - width;
            dx = -dx;
        }

        //  Trần
        if (y <= 0) {
            y = 0;
            dy = -dy;
        }

        // Rơi khỏi màn hình (dưới đáy)
        if (y > Constant.SCREEN_HEIGHT) {
            // GameManager sẽ xử lý (trừ mạng, reset bóng...)
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Constant.BALL_COLOR);
        gc.fillOval(x, y, width, height);
    }

    // Getter / Setter
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }
}
