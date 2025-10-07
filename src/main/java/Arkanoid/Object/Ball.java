package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private double speed;

    public Ball(double x, double y, double radius, double speed, int dx, int dy) {
        super(x, y, radius, radius, dx, dy);
        this.speed = speed;
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
        gc.setFill(Color.YELLOW);
        gc.fillOval(x, y, width, height);
    }

    public double getSpeed() { return speed; }

    public void setSpeed(double speed) { this.speed = speed;}

    public void setDirectionY(int dy){
        this.dy = dy;
    }

}
