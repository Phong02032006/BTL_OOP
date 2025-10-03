package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class StrongBrick extends Brick {
    public StrongBrick(double x, double y , double width, double height,int hp){
        super(x,y,width,height,Math.max(2,hp),"Strong");
    }

    @Override
    public void render(GraphicsContext gc) {
        // tính factor đẻ màu nhạt dần khi hitpoints giảm
        double factor = Math.max(0.4, Math.min(1.0, 0.6+ 0.2* hitPoints));
        Color fill= Color.RED.deriveColor(0, 1,factor, 1);
        gc.setFill(Color.RED);
        gc.fillRect(x,y,width,height);

        gc.setStroke(Color.BLACK);
        gc.strokeLine(x+0.5, y+0.5, width-1, height -1 );

        // debug hitPoints (xoa khi nop)
        gc.setFill(Color.BLACK);
        gc.fillText("1", x + width / 2 - 3, y + height / 2 + 4);
    }

}

