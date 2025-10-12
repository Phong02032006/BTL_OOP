package Arkanoid.util;

import javafx.scene.paint.Color;

/**
 * Chứa các hằng số dùng chung trong toàn bộ game Arkanoid.
 */
public class Constant {

    //  Kích thước màn hình
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    //  Paddle
    public static final double PADDLE_WIDTH = 128;
    public static final double PADDLE_HEIGHT = 28;
    public static final double PADDLE_SPEED = 6;

    //  Ball
    public static final double BALL_RADIUS = 20;
    public static final double BALL_SPEED = 3;

    //  Brick
    public static final double BRICK_WIDTH = 64;
    public static final double BRICK_HEIGHT = 32;

    //  PowerUp
    public static final double POWERUP_SIZE = 15;
    public static final double POWERUP_SPEED = 1;
    public static final double POWERUP_DURATION = 2000;
    public static final Color POWERUP_EXPAND_COLOR = Color.LIMEGREEN;
    public static final Color POWERUP_FAST_COLOR = Color.MEDIUMPURPLE;
    // 2s


    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color BALL_COLOR = Color.YELLOW;
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final Color BRICK_COLOR = Color.CORNFLOWERBLUE;
}
