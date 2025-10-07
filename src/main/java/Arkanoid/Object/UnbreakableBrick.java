package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x,y,width, height, -1);
    }
    @Override
    public void render(GraphicsContext gc){
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(x,y,width,height);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(x+0.5, y+0.5, width-1, height -1 );
    }

    @Override
    public boolean takeHit() {return false;}
}
