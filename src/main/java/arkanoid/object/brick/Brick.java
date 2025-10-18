package arkanoid.object.brick;

import arkanoid.object.GameObject;
import arkanoid.object.powerup.PowerUp;
import arkanoid.util.SpriteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Brick extends GameObject {
    protected int hitPoints;
    protected PowerUp powerUp;
    protected Image image;

    protected Brick(double x, double y, double width, double height, int hitPoints, String spritePath) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        if (spritePath != null) {
            this.image = SpriteManager.getImage(spritePath);
        }
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

    public boolean hasPowerUp() {
        return powerUp != null;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    @Override
    public void update() { /* gạch tĩnh, để trống */ }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        }
    }
}
