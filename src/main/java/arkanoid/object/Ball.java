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
     * Handle bounce behavior when colliding with Paddle or Brick.
     */
    public void bounceOff(GameObject other) {
        double ballCenterX = x + width / 2;
        double ballCenterY = y + height / 2;
        double otherCenterX = other.x + other.width / 2;
        double otherCenterY = other.y + other.height / 2;

        double deltaX = ballCenterX - otherCenterX;
        double deltaY = ballCenterY - otherCenterY;

        if (other instanceof Paddle) {
            // Bounce with angle depending on hit position
            y = other.y - height;
            double offset = Math.max(-1, Math.min(1, deltaX / (other.width / 2)));

            double minAngle = Math.toRadians(15);
            double maxAngle = Math.toRadians(60);
            double angle = offset * (maxAngle - minAngle);
            angle += (offset >= 0) ? minAngle : -minAngle;

            dx = Math.sin(angle);
            dy = -Math.cos(angle);

            // Slightly move ball away to prevent sticking
            x += dx * 0.1;
            y += dy * 0.1;
        } else {
            // Bounce off bricks or walls
            double absDX = Math.abs(deltaX);
            double absDY = Math.abs(deltaY);
            double eps = 0.1;

            if (absDY > absDX) { // vertical collision
                if (deltaY > 0) {
                    y = other.y + other.height + eps;
                    dy = Math.abs(dy);
                } else {
                    y = other.y - height - eps;
                    dy = -Math.abs(dy);
                }
            } else { // horizontal collision
                if (deltaX > 0) {
                    x = other.x + other.width + eps;
                    dx = Math.abs(dx);
                } else {
                    x = other.x - width - eps;
                    dx = -Math.abs(dx);
                }
            }
        }
    }

    /**
     * Sweep-based collision handling (smoother than normal bounce).
     */
    @SuppressWarnings("DuplicateCode")
    public boolean sweepBounceOff(GameObject other) {
        if (!checkCollision(other)) {
            return false;
        }

        double eps = 0.08;
        double pL = prevX, pR = prevX + width, pT = prevY, pB = prevY + height;
        double oL = other.x, oR = other.x + other.width, oT = other.y, oB = other.y + other.height;

        // Priority by last-frame vertical position
        if (pB <= oT) {
            y = oT - height - eps;
            dy = -Math.abs(dy);
            return true;
        }
        if (pT >= oB) {
            y = oB + eps;
            dy = Math.abs(dy);
            return true;
        }
        if (pR <= oL) {
            x = oL - width - eps;
            dx = -Math.abs(dx);
            return true;
        }
        if (pL >= oR) {
            x = oR + eps;
            dx = Math.abs(dx);
            return true;
        }

        // Fallback: choose smaller overlap
        double overlapL = (x + width) - oL;
        double overlapR = oR - x;
        double overlapT = (y + height) - oT;
        double overlapB = oB - y;
        double minX = Math.min(overlapL, overlapR);
        double minY = Math.min(overlapT, overlapB);

        if (minX < minY) {
            if (overlapL < overlapR) {
                x = oL - width - eps;
                dx = -Math.abs(dx);
            } else {
                x = oR + eps;
                dx = Math.abs(dx);
            }
        } else {
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

    // --- Getters & Setters ---
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setDirectionY(int dy) { this.dy = dy; }
}