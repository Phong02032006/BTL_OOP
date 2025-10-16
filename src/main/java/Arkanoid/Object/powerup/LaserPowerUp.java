package Arkanoid.Object.powerup;

import Arkanoid.Object.Laser;
import Arkanoid.Object.Ball;
import Arkanoid.Object.Paddle;
import Arkanoid.Object.powerup.PowerUp;
import Arkanoid.util.Constant;

public class LaserPowerUp extends PowerUp {

    public LaserPowerUp(double x, double y) {
        super(
                x, y,
                Constant.POWERUP_SIZE,
                Constant.POWERUP_SIZE,
                "Laser",
                Constant.POWERUP_DURATION * 1.2,
                "/images/laser_power_up.png"
        );
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        startTime = System.currentTimeMillis();
        setActive(true);
        paddle.setLaserEquipped(true);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        setActive(false);
        paddle.setLaserEquipped(false);
    }
}