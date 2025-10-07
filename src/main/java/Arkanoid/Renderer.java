package Arkanoid;

import Arkanoid.Object.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class Renderer {
    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * xoa het khung truoc khi ve frame moi.
     */
    public void clear(double width, double height) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    public void draw(GameObject obj) {
        if(obj != null){
            obj.render(gc);
        }
    }

    public void drawList(List<? extends GameObject> list) {
        if (list == null) return;
        for (GameObject obj : list) {
            if (obj != null) obj.render(gc);
        }
    }

    public void drawHUD(int score, int lives, String state) {
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 20, 20);
        gc.fillText("Lives: " + lives, 20, 40);
        if (state != null && !"RUNNING".equals(state)) {
            gc.fillText("State: " + state, 20, 60);
        }
    }

    public void renderAll(GameManager game, double width, double height) {
        clear(width, height);
        draw(game.getPaddle());
        draw(game.getBall());
        drawList(game.getBricks());
        drawList(game.getPowerUps());
        drawHUD(game.getScore(), game.getLives(), game.getState());
    }
}
