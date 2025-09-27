package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;

public abstract class Brick extends GameObject {
    protected int hitPoints;

    protected Brick(double x, double y, double width, double height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
    }

    public boolean takeHit() {
        if (isUnbreakable()) return false;
        if (hitPoints > 0) hitPoints--;
        return isDestroyed();
    }

    public boolean isDestroyed() {
        return !isUnbreakable() && hitPoints == 0;
    }

    public boolean isUnbreakable() {
        return hitPoints < 0;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public void update() { /* gạch tĩnh, để trống */ }
    /**
     * Mỗi loại gạch tự vẽ
     */
    @Override
    public abstract void render(GraphicsContext gc);
}
