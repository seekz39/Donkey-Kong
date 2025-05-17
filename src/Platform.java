import bagel.*;

/**
 * Represents a stationary platform in the game.
 * Platforms provide surfaces for Mario to walk on and interact with.
 */
public class Platform extends GameEntity{
    private static final Image PLATFORM_IMAGE = new Image("res/platform.png"); // Image representing the platform
    private final double WIDTH, HEIGHT; // Dimensions of the platform

    /**
     * Constructs a platform at the specified position.
     *
     * @param x The initial x-coordinate of the platform.
     * @param y The initial y-coordinate of the platform.
     */
    public Platform(double x, double y) {
        // Load platform sprite
        super(PLATFORM_IMAGE, x, y);

        // Set platform dimensions based on the image size
        this.WIDTH = PLATFORM_IMAGE.getWidth();
        this.HEIGHT = PLATFORM_IMAGE.getHeight();
    }

    /**
     * Retrieves the width of the platform.
     *
     * @return The width of the platform.
     */
    public double getWidth() {
        return WIDTH;
    }

    /**
     * Retrieves the height of the platform.
     *
     * @return The height of the platform.
     */
    public double getHeight() {
        return HEIGHT;
    }


    @Override
    public void changeState(GameEntity other) {
        // Platforms do not respond to collisions
    }
}

