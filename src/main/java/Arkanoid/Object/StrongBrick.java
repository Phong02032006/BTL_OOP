package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class StrongBrick extends Brick {
    public StrongBrick(double x, double y , double width, double height,int hp){
        super(x,y,width,height,Math.max(2,hp),"/images/brick_orange.bmp");
    }
}

