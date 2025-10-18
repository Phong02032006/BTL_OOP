package arkanoid.object;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp cơ sở cho các đối tượng có thể di chuyển.
 * dx tốc độ theo trục x(ngang)
 * dy tốc dộ theo trục y(dọc)
 */
public abstract class MovableObject extends GameObject {
    protected double dx;
    protected double dy;

    public MovableObject(double x, double y, double width, double height, double dx, double dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(GraphicsContext gc);
}
