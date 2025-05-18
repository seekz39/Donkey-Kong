import bagel.Image;

public abstract class GravityEntity extends GameEntity {
    private double velocityY = 0;
    private final double gravity;
    public static final double TERMINAL_VELOCITY = 10.0;


    public GravityEntity(Image initialImage, double startX, double startY, double gravity) {
        super(initialImage, startX, startY);
        this.gravity = gravity;
    }

    public void update(Platform[] platforms) {
        // Apply gravity
        velocityY += this.gravity;
        setY(getY() + velocityY);

        if (velocityY > TERMINAL_VELOCITY) {
            velocityY = TERMINAL_VELOCITY;
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
