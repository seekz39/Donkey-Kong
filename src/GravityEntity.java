import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class GravityEntity {
    protected double x, y;
    protected double velocityY = 0;

    // Subclasses must implement this
    protected abstract Image getImage();
    protected abstract void draw();

    public void update(Platform[] platforms) {
        // Apply gravity
        velocityY += Physics.MONKEY_GRAVITY;
        y += velocityY;

        if (velocityY > Physics.TERMINAL_VELOCITY) {
            velocityY = Physics.TERMINAL_VELOCITY;
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (getBoundingBox().intersects(platform.getBoundingBox())) {
                y = platform.getY()
                        - (platform.getHeight() / 2.0)
                        - (getImage().getHeight() / 2.0);
                velocityY = 0;
                break;
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Rectangle getBoundingBox() {
        return getImage().getBoundingBoxAt(new Point(x, y));
    }


}
