package Arkanoid.Object.brick;

import Arkanoid.util.SpriteManager;
import javafx.scene.image.Image;

public class StrongBrick extends Brick {
    private final Image crackedImage;

    public StrongBrick(double x, double y, double width, double height, int hp) {
        super(x, y, width, height, Math.max(2, hp), "/images/strong_brick.png");
        this.crackedImage = SpriteManager.getImage("/images/strong_brick_damaged.png");
    }

    @Override
    public boolean takeHit() {
        if (isUnbreakable()) return false;

        hitPoints--;
        if (hitPoints == 1) {
            // ⚡ đổi ảnh khi còn 1 máu
            image = crackedImage;
        }
        return isDestroyed();
    }
}
