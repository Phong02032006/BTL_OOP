package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;

public abstract class Brick extends GameObject {
    protected int hitPoints;
    protected String type;

    protected Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void update() { /* gạch tĩnh, để trống */ }
    /**
     * Mỗi loại gạch tự vẽ
     */
    @Override
    public abstract void render(GraphicsContext gc);
}
