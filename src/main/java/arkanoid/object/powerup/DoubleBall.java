package arkanoid.object.powerup;

import arkanoid.core.GameManager;
import arkanoid.object.Ball;
import arkanoid.object.Paddle;
import arkanoid.util.Constant;

/**
 * PowerUp nhân đôi bóng (Double Ball)
 * Khi Paddle chạm, sẽ tạo thêm 1 bóng phụ di chuyển ngược hướng với bóng chính.
 * Hiệu ứng kéo dài trong một khoảng thời gian, sau đó bóng phụ biến mất.
 */
public class DoubleBall extends PowerUp {

    private Ball extraBall;  // lưu bóng phụ để xoá sau

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
        GameManager.getInstance().removeExtraBalls();
    }


    @Override
    public void update() {
        y += Constant.POWERUP_SPEED;
    }
}
