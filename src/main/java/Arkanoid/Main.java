package Arkanoid;

import Arkanoid.util.Constant;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        //kich thuoc khung game
        final int width = Constant.SCREEN_WIDTH;
        final int height = Constant.SCREEN_HEIGHT;

        //tao vcan vas va graphics context
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //tao game manager va renderer
        GameManager gm = new GameManager();
        gm.start();
        Renderer renderer = new Renderer(gc);

        //tao scene va gan canvas
        Scene scene = new Scene(new StackPane(canvas), width, height);

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case LEFT -> gm.onKeyPressed("LEFT");
                case RIGHT -> gm.onKeyPressed("RIGHT");
                case SPACE -> gm.onKeyPressed("SPACE");
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case LEFT -> gm.onKeyReleased("LEFT");
                case RIGHT -> gm.onKeyReleased("RIGHT");
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gm.update(); // cập nhật vị trí bóng, paddle, v.v.
                renderer.renderAll(gm, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
            }
        }.start();

        stage.setTitle("Arkanoid");
        stage.setScene(scene);
        stage.show();

        canvas.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
