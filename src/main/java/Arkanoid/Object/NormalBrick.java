package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class NormalBrick extends Brick {
    public NormalBrick(double x, double y , double width, double height){
        super(x,y,width,height,1);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(x,y,width,height);

        gc.setStroke(Color.BLACK);
        gc.strokeLine(x+0.5, y+0.5, width-1, height -1 );

        // debug hitPoints (xoa khi nop)
        gc.setFill(Color.BLACK);
        gc.fillText("1", x + width / 2 - 3, y + height / 2 + 4);
    }

}
