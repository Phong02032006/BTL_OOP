package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background {
    private final Image image;

    public Background(String path) {
        this.image = new Image(getClass().getResourceAsStream(path));
    }

    public void render(GraphicsContext gc, double width, double height) {
        gc.drawImage(image, 0, 0, width, height);
    }

    public Image getImage() {
        return image;
    }
}
