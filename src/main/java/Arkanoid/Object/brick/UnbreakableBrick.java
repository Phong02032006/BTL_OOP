package Arkanoid.Object.brick;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, -1, "/images/block.png");
    }

    @Override
    public boolean takeHit() {
        return false;
    }
}
