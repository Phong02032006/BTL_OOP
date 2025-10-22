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

        // A small buffer to prevent objects from sticking together after a collision.
        double collisionOffset = 0.08;

        // Coordinates of the ball's edges in the previous frame.
        double previousBallLeft = prevX;
        double previousBallRight = prevX + width;
        double previousBallTop = prevY;
        double previousBallBottom = prevY + height;

        // Coordinates of the colliding object's edges.
        double otherObjectLeft = other.x;
        double otherObjectRight = other.x + other.width;
        double otherObjectTop = other.y;
        double otherObjectBottom = other.y + other.height;

        // Prioritize handling based on the previous frame's position to determine the collision direction.
        // Check for collision from top to bottom.
        if (previousBallBottom <= otherObjectTop) {
            y = otherObjectTop - height - collisionOffset;
            dy = -Math.abs(dy);
            return true;
        }
        // Check for collision from bottom to top.
        if (previousBallTop >= otherObjectBottom) {
            y = otherObjectBottom + collisionOffset;
            dy = Math.abs(dy);
            return true;
        }
        // Check for collision from left to right.
        if (previousBallRight <= otherObjectLeft) {
            x = otherObjectLeft - width - collisionOffset;
            dx = -Math.abs(dx);
            return true;
        }
        // Check for collision from right to left.
        if (previousBallLeft >= otherObjectRight) {
            x = otherObjectRight + collisionOffset;
            dx = Math.abs(dx);
            return true;
        }

        // Fallback: if the direction cannot be determined from the previous position, choose the direction with the smallest overlap.
        double overlapLeft = (x + width) - otherObjectLeft;
        double overlapRight = otherObjectRight - x;
        double overlapTop = (y + height) - otherObjectTop;
        double overlapBottom = otherObjectBottom - y;

        double minHorizontalOverlap = Math.min(overlapLeft, overlapRight);
        double minVerticalOverlap = Math.min(overlapTop, overlapBottom);

        if (minHorizontalOverlap < minVerticalOverlap) { // Handle horizontal collision
            if (overlapLeft < overlapRight) {
                x = otherObjectLeft - width - collisionOffset;
                dx = -Math.abs(dx);
            } else {
                x = otherObjectRight + collisionOffset;
                dx = Math.abs(dx);
            }
        } else { // Handle vertical collision
            if (overlapTop < overlapBottom) {
                y = otherObjectTop - height - collisionOffset;
                dy = -Math.abs(dy);
            } else {
                y = otherObjectBottom + collisionOffset;
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