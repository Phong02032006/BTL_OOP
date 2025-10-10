package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x,y,width, height, -1,"/images/brick_green.png");
    }

    @Override
    public boolean takeHit() {return false;}
}
