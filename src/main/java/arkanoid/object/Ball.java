package arkanoid.object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import arkanoid.util.*;
import javafx.scene.image.Image;

public class Ball extends MovableObject {
    private double speed;
    private final Image image;
    private double prevX;
    private double prevY;

    public Ball(double x, double y, double radius, double speed, double dx, double dy) {
        super(x, y, radius, radius, dx, dy);
        this.speed = speed;
        this.image = SpriteManager.getImage("/images/ball.png");
    }

    /**
     * Check collision between this ball (circle) and another rectangular GameObject.
     */
    public boolean checkCollision(GameObject other) {
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double radius = width / 2;

        double closestX = Math.max(other.x, Math.min(ballCenterX, other.x + other.width));
        double closestY = Math.max(other.y, Math.min(ballCenterY, other.y + other.height));

        double dx = ballCenterX - closestX;
        double dy = ballCenterY - closestY;
        double distanceSquared = dx * dx + dy * dy;

        // Add small epsilon to avoid false negatives due to rounding
        return distanceSquared <= (radius * radius + 0.001);
    }

    /**
     * Sweep-based collision handling (smoother than normal bounce).
     */
    public boolean sweepBounceOff(GameObject other) {
        // Exit if there is no collision
        if (!checkCollision(other)) return false;

        double offset = 0.08;

        // Ball’s previous frame coordinates
        double prevLeft = prevX;
        double prevRight = prevX + width;
        double prevTop = prevY;
        double  prevBottom = prevY + height;

        // Colliding object’s coordinates
        double left = other.x;
        double right = other.x + other.width;
        double top = other.y;
        double bottom = other.y + other.height;

        // If colliding with Paddle, calculate bounce angle based on hit position
        if (other instanceof Paddle) {
            double ballCenterX = x + width / 2;
            double deltaX = ballCenterX - (other.x + other.width / 2);
            double paddleHalf = other.width / 2;
            double ratio = Math.max(-1, Math.min(1, deltaX / paddleHalf));
            double minAngle = Math.toRadians(15);
            double maxAngle = Math.toRadians(60);
            double angle = ratio * (maxAngle - minAngle);
            angle += (ratio >= 0) ? minAngle : -minAngle;

            dx = Math.sin(angle);
            dy = -Math.cos(angle);
            x += dx * 0.1;
            y = other.y - height - 0.1;
            return true;
        }

        // Determine collision direction based on previous frame
        if (prevBottom <= top) { // from top to bottom
            y = top - height - offset;
            dy = -Math.abs(dy);
            return true;
        }
        if (prevTop >= bottom) { // from bottom to top
            y = bottom + offset;
            dy = Math.abs(dy);
            return true;
        }
        if (prevRight <= left) { // from left to right
            x = left - width - offset;
            dx = -Math.abs(dx);
            return true;
        }
        if (prevLeft >= right) { // from right to left
            x = right + offset;
            dx = Math.abs(dx);
            return true;
        }

        // If unclear, resolve by choosing the smallest overlap direction
        double overlapLeft = (x + width) - left;
        double overlapRight = right - x;
        double overlapTop = (y + height) - top;
        double overlapBottom = bottom - y;

        double minHorizontal = Math.min(overlapLeft, overlapRight);
        double minVertical = Math.min(overlapTop, overlapBottom);

        if (minHorizontal < minVertical) { // horizontal collision
            if (overlapLeft < overlapRight) {
                x = left - width - offset;
                dx = -Math.abs(dx);
            } else {
                x = right + offset;
                dx = Math.abs(dx);
            }
        } else { // vertical collision
            if (overlapTop < overlapBottom) {
                y = top - height - offset;
                dy = -Math.abs(dy);
            } else {
                y = bottom + offset;
                dy = Math.abs(dy);
            }
        }
        return true;
    }

    /**
     * Update the ball’s position and handle wall collisions.
     */
    @Override
    public void move() {
        prevX = x;
        prevY = y;

        // Normalize direction vector to keep consistent speed
        double norm = Math.sqrt(dx * dx + dy * dy);
        if (norm != 0) {
            x += (dx / norm) * speed;
            y += (dy / norm) * speed;
        }

        double left = Constant.BORDER_LEFT;
        double right = Constant.BORDER_RIGHT;
        double top = Constant.BORDER_TOP;

        if (x <= left) {
            x = left;
            dx = Math.abs(dx);
            SoundManager.playSound("hit_wall.wav");
        }

        if (x + width >= right) {
            x = right - width;
            dx = -Math.abs(dx);
            SoundManager.playSound("hit_wall.wav");
        }

        if (y <= top) {
            y = top;
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