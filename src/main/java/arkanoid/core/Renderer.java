package arkanoid.core;

import arkanoid.object.*;
import arkanoid.util.Constant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class Renderer {
    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * clear before new frame.
     */
    public void clear(double width, double height) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    public void draw(GameObject obj) {
        if (obj != null) {
            obj.render(gc);
        }
    }


    public void drawList(List<? extends GameObject> list) {
        if (list == null) return;
        for (GameObject obj : list) {
            if (obj != null) obj.render(gc);
        }
    }

    public void drawHUD(int score, int lives, String state, int level) {

        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        gc.setFill(Color.WHITE);          // text color
        gc.setStroke(Color.BLACK);      // border
        gc.setLineWidth(2);             // border thickness

        // Score
        gc.strokeText("Score: " + score, 30, 30);
        gc.fillText("Score: " + score, 30, 30);

        // Lives
        gc.strokeText("Lives: " + lives, 30, 60);
        gc.fillText("Lives: " + lives, 30, 60);

        String levelText = "Level: " + (level + 1);
        double levelTextX = Constant.SCREEN_WIDTH - 150;

        gc.strokeText(levelText, levelTextX, 30);
        gc.fillText(levelText, levelTextX, 30);

        // State
        if (state != null && !"RUNNING".equals(state)) {
            gc.setFill(Color.ORANGE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.strokeText("State: " + state, 20, 90);
            gc.fillText("State: " + state, 20, 90);
        }
    }

    public void renderAll(GameManager game, double width, double height) {
        clear(width, height);
        game.getBackground().render(gc, width, height);
        draw(game.getPaddle());
        drawList(game.getBalls());
        drawList(game.getBricks());
        drawList(game.getPowerUps());
        drawList(game.getLasers());
        drawHUD(game.getScore(), game.getLives(), game.getState(), game.getCurLevel());
    }
}
