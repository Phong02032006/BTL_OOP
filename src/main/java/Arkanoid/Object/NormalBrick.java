package Arkanoid.Object;

import Arkanoid.util.Constant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class NormalBrick extends Brick {
    public NormalBrick(double x, double y , double width, double height){
        super(x,y,width,height,1,"/images/brick_blue.png");
    }
}
