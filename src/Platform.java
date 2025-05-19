import bagel.*;

/**
 * Represents a stationary platform in the game.
 * Platforms provide surfaces for Mario to walk on and interact with.
 */
public class Platform extends GameEntity{
    private static final Image PLATFORM_IMAGE = new Image("res/platform.png"); // Image representing the platform
    private final double WIDTH, HEIGHT;

    /**
     * Constructs a platform at the specified position.
     *
     * @param x The initial x-coordinate of the platform.
     * @param y The initial y-coordinate of the platform.
     */
    public Platform(double x, double y) {
        // Load platform sprite
        super(PLATFORM_IMAGE, x, y);
        this.WIDTH = PLATFORM_IMAGE.getWidth();
        this.HEIGHT = PLATFORM_IMAGE.getHeight();
    }

    public double getWidth() {
        return WIDTH;
    }

    public double getHeight() {
        return HEIGHT;
    }
}

