package arkanoid.object.powerup;

import arkanoid.core.GameManager;
import arkanoid.object.Ball;
import arkanoid.object.Paddle;
import arkanoid.util.Constant;

/**
 * powerup double ball.
 * when hit paddle, extra ball is spawn in another direction.
 */
public class DoubleBall extends PowerUp {

    private Ball extraBall;  // save extra ball

    public DoubleBall(double x, double y) {
        super(
                x, y,
                Constant.POWERUP_SIZE,
                Constant.POWERUP_SIZE,
                "DoubleBall",
                Constant.POWERUP_DURATION * 5,
                "/images/double.png"
        );
        this.color = Constant.POWERUP_DOUBLE_COLOR;
    }

    @Override
    public void applyEffect(Paddle paddle, Ball mainBall) {
        startTime = System.currentTimeMillis();
        setActive(true);
        GameManager.getInstance().spawnExtraBalls();
    }

    @Override
    public void removeEffect(Paddle paddle, Ball mainBall) {
        setActive(false);
        if (!GameManager.getInstance().hasActiveMultiBallPowerUp()) {
            GameManager.getInstance().removeExtraBalls();
        }
    }


    @Override
    public void update() {
        y += Constant.POWERUP_SPEED;
    }
}
