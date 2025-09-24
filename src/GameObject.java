import java.awt.Graphics;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    /**
     * Khởi tạo GameObject với vị trí và kích thước.
     *
     * @param x hoành độ (cách cạnh bên trái màn hình x pixel)
     * @param y tung độ (cách cạnh bên trên màn hình y pixel)
     * @param width chiều rộng (pixel)
     * @param height chiều cao (pixel)
     */

    GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public abstract void render(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
