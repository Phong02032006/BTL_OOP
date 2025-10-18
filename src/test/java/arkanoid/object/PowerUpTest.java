package arkanoid.object;

import arkanoid.object.powerup.LaserPowerUp;
import arkanoid.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {

    private Paddle paddle;
    private LaserPowerUp laserPowerUp;

    @BeforeEach
    void setUp() {
        paddle = new Paddle(100, 400, Constant.PADDLE_WIDTH, Constant.PADDLE_HEIGHT, Constant.PADDLE_SPEED);
        laserPowerUp = new LaserPowerUp(100, 200);
    }

    @Test
    void testLaserPowerUp_ApplyEffect_ShouldEquipLaser() {
        assertFalse(paddle.isLaserEquipped(), "No laser equipped");
        laserPowerUp.applyEffect(paddle, null);
        assertTrue(paddle.isLaserEquipped(), "Laser equipped");
    }

    @Test
    void testLaserPowerUp_RemoveEffect_ShouldUnequipLaser() {
        laserPowerUp.applyEffect(paddle, null);
        assertTrue(paddle.isLaserEquipped(), "Paddle first has laser equipped");
        laserPowerUp.removeEffect(paddle, null);
        assertFalse(paddle.isLaserEquipped(), "Laser should be removed now");
    }
}
