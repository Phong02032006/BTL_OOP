package arkanoid.util;

import javafx.scene.paint.Color;

/**
 * Constant of the game.
 */
public class Constant {

    // Screen
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final double BORDER_LEFT = 16;

    //State
    public static final String STATE_MENU = "MENU";
    public static final String STATE_RUNNING = "RUNNING";
    public static final String STATE_PAUSED = "PAUSED";
    public static final String STATE_GAME_OVER = "GAME_OVER";

    // Border
    public static final double BORDER_RIGHT = Constant.SCREEN_WIDTH - 16;
    public static final double BORDER_TOP = 16;
    public static final double BORDER_BOTTOM = Constant.SCREEN_HEIGHT - 16;

    //  Paddle
    public static final double PADDLE_WIDTH = 128;
    public static final double PADDLE_HEIGHT = 28;
    public static final double PADDLE_SPEED = 6;

    //  Ball
    public static final double BALL_RADIUS = 20;
    public static final double BALL_SPEED = 3;

    //Laser
    public static final double LASER_WIDTH = 8;
    public static final double LASER_HEIGHT = 16;
    public static final double LASER_SPEED = 7.0;

    //  Brick
    public static final double BRICK_WIDTH = 64;
    public static final double BRICK_HEIGHT = 32;

    //  PowerUp
    public static final double POWERUP_SIZE = 15;
    public static final double POWERUP_SPEED = 1;
    public static final double POWERUP_DURATION = 2000;
    public static final Color POWERUP_EXPAND_COLOR = Color.LIMEGREEN;
    public static final Color POWERUP_FAST_COLOR = Color.MEDIUMPURPLE;
    public static final Color POWERUP_DOUBLE_COLOR = Color.MEDIUMPURPLE;

    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color BALL_COLOR = Color.YELLOW;
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final Color BRICK_COLOR = Color.CORNFLOWERBLUE;
}
