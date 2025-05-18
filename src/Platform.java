import bagel.*;

/**
 * Represents a stationary platform in the game.
 * Platforms provide surfaces for Mario to walk on and interact with.
 */
public class Platform extends GameEntity{
    private static final Image PLATFORM_IMAGE = new Image("res/platform.png"); // Image representing the platform

    /**
     * Constructs a platform at the specified position.
     *
     * @param x The initial x-coordinate of the platform.
     * @param y The initial y-coordinate of the platform.
     */
    public Platform(double x, double y) {
        // Load platform sprite
        super(PLATFORM_IMAGE, x, y);
    }

    @Override
    public void changeState(GameEntity other) {
        // Platforms do not respond to collisions
    }
}

