package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
    /*
    - check conllision phai sua lai de tro thanh check su va cham giua hinh tron va hinh chu nhat.
    - ham check collision hien tai chi check va cham giua 2 hinh chu nhat thoi nen neu chay se xay ra loi.
     */
    public boolean checkCollision(GameObject other) {
        return (x < other.x + other.width &&    // mép trái bóng < mép phải đối tượng
                x + width > other.x &&          // mép phải bóng > mép trái đối tượng
                y < other.y + other.height &&   // mép trên bóng < mép dưới đối tượng
                y + height > other.y);          // mép dưới bóng > mép trên đối tượng
    }

    public void bounceOff(GameObject other) {
        dy = -dy;
    }

    @Override
    public void update() {
        move();
        if (x <= 0 || x + width >= 800) {
            dx = -dx;
        }
        if (y <= 0) {
            dy = -dy;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillOval(x, y, width, height);
    }

    public double getSpeed() { return speed; }

    public void setSpeed(double speed) { this.speed = speed;}
    
    public double getDirectionX() { return directionX; }
    
    public void setDirectionX(double directionX) { this.directionX = directionX; }
    
    public double getDirectionY() { return directionY; }
    
    public void setDirectionY(double directionY) { this.directionY = directionY; }

}
