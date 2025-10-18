package arkanoid.object;

import arkanoid.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Paddle
 */
class PaddleTest {

    private Paddle paddle;

    @BeforeEach
    void setUp() {
        paddle = new Paddle(
                100,                     // x
                Constant.SCREEN_HEIGHT - 50, // y
                80,                      // width
                10,                      // height
                5                        // speed
        );
    }

    @Test
    void testMoveLeftWithinBounds() {
        paddle.moveLeft(Constant.SCREEN_WIDTH);
        assertTrue(paddle.getX() >= 0, "Paddle should not move beyond left edge");
    }

    @Test
    void testMoveLeftStopsAtEdge() {
        paddle.setX(1);
        paddle.moveLeft(Constant.SCREEN_WIDTH);
        assertEquals(16, paddle.getX(), 0.0001, "Paddle should stop exactly at left boundary");
    }

    @Test
    void testMoveRightWithinBounds() {
        paddle.moveRight(Constant.SCREEN_WIDTH);
        assertTrue(paddle.getX() + paddle.getWidth() <= Constant.SCREEN_WIDTH,
                "Paddle should not move beyond right edge");
    }

    @Test
    void testMoveRightStopsAtEdge() {
        paddle.setX(Constant.SCREEN_WIDTH - paddle.getWidth() - 1);
        paddle.moveRight(Constant.SCREEN_WIDTH);
        assertEquals(Constant.SCREEN_WIDTH - paddle.getWidth() - 16,
                paddle.getX(), 0.0001, "Paddle should stop exactly at right boundary");
    }

}
