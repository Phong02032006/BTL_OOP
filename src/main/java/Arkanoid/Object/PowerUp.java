package Arkanoid.Object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public abstract class PowerUp extends GameObject {
    protected String type;
    protected double duration;
    /*
        Duration tinh bang ms (vd 5000ms=5s).
     */
    public PowerUp(double x, double y, double width, double height, String type, double duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public void update() {
    }

    public abstract void applyEffect(Paddle paddle, Ball ball);

    public abstract void removeEffect(Paddle paddle, Ball ball);

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(x, y, width, height);
    }
}
