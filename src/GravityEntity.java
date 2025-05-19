import bagel.Image;

/**
 * A super class for entities affected by gravity.
 *
 * Applies a constant downward acceleration each frame, enforces a terminal velocity,
 * and handles vertical collision with platforms.
 *
 */
public abstract class GravityEntity extends GameEntity {
    private double velocityY = 0;
    private final double gravity;
    public static final double TERMINAL_VELOCITY = 10.0;


    public GravityEntity(Image initialImage, double startX, double startY, double gravity) {
        super(initialImage, startX, startY);
        this.gravity = gravity;
    }

    /**
     * Updates the entity’s vertical motion for one frame:
     *
     *   Applies gravity to vertical velocity.
     *   Moves the entity down by the new velocity (capped at {@link #TERMINAL_VELOCITY}).
     *   Checks for collisions with each platform;
     *   if a collision is found, align the entity to stand on top of the platform and resets vertical velocity to zero.
     *   Draws the entity at its updated position.
     *
     * @param platforms an array of platforms to test for vertical collisions
     */
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
