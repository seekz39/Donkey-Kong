import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class GravityEntity extends GameEntity {
    private double velocityY = 0;


    public GravityEntity(Image initialImage, double startX, double startY) {
        super(initialImage, startX, startY);
    }

    public void update(Platform[] platforms) {
        // Apply gravity
        velocityY += Physics.MONKEY_GRAVITY;
        setY(getY() + velocityY);

        if (velocityY > Physics.TERMINAL_VELOCITY) {
            velocityY = Physics.TERMINAL_VELOCITY;
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (getBoundingBox().intersects(platform.getBoundingBox())) {
                double newY = platform.getY()
                        - (platform.getHeight() / 2.0)
                        - (getImage().getHeight() / 2.0);
                setY(newY);
                velocityY = 0;
                break;
            }
        }

        draw();
    }

}
