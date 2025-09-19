import bagel.Image;

/**
 * A super class for entities affected by gravity.
 *
 * Applies a constant downward acceleration each frame, enforces a terminal velocity,
 * and handles vertical collision with platforms.
 *
 */
public abstract class GravityEntity extends GameEntity {
    /**
     * The maximum downward speed an entity can reach due to gravity (pixels per frame).
     */
    public static final double TERMINAL_VELOCITY = 10.0;
    private double velocityY = 0;
    private final double GRAVITY;

    public GravityEntity(Image initialImage, double startX, double startY, double gravity) {
        super(initialImage, startX, startY);
        this.GRAVITY = gravity;
    }

    /**
     * Updates the entity’s vertical motion for one frame:
     *
     * @param platforms an array of platforms to test for vertical collisions
     */
    public void update(Platform[] platforms) {
        applyGravity(platforms);
        draw();
    }

    /**
     * Applies gravity to vertical velocity.
     * Moves the entity down by the new velocity (capped at {@link #TERMINAL_VELOCITY}).
     * Checks for collisions with each platform;
     * if a collision is found, align the entity to stand on top of the platform and resets vertical velocity to zero.
     *
     * @param platforms an array of platforms to test for vertical collisions
     */
    private void applyGravity(Platform[] platforms) {
        // 1) Apply gravity to vertical velocity (capped at terminal velocity)
        velocityY = Math.min(velocityY + GRAVITY, TERMINAL_VELOCITY);

        // 2) Move vertically
        setY(getY() + velocityY);

        // 3) If we hit a platform, snap to its top and zero out velocity
        for (Platform platform : platforms) {
            if (getBoundingBox().intersects(platform.getBoundingBox())) {
                double newY = platform.getBoundingBox().top()
                        - getImage().getHeight() / 2.0;
                setY(newY);
                velocityY = 0;
                break;
            }
        }
    }
}
