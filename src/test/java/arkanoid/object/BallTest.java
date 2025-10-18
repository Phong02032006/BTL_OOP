package arkanoid.object;

import arkanoid.object.brick.NormalBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    private Ball ball;
    private NormalBrick brick;

    @BeforeEach
    void setUp() {
        ball = new Ball(100, 100, 10, 5, 0, 1); // x, y, radius, speed, dx, dy (đang bay xuống)
        brick = new NormalBrick(95, 110, 20, 10); // x, y, width, height
    }

    @Test
    void testCollisionDetection_ShouldReturnTrue_WhenOverlapping() {
        assertTrue(ball.checkCollision(brick), "Ball must collide with brick");
    }

    @Test
    void testCollisionDetection_ShouldReturnFalse_WhenNotOverlapping() {
        brick.setX(500);
        assertFalse(ball.checkCollision(brick), "Ball must not collide with brick");
    }

    @Test
    void testBounceLogic_ShouldReverseVerticalDirection_WhenHittingFromTop() {
        assertTrue(ball.getDy() > 0, "Ball must run dow");
        ball.sweepBounceOff(brick);
        assertTrue(ball.getDy() < 0, "Change dy");
    }
}