import bagel.Image;

/**
 * Represents a Hammer collectible in the game.
 * The hammer can be collected by the player, at which point it disappears from the screen.
 */
public class Hammer extends GameEntity implements Collectable{
    private boolean isCollected = false;
    private static final Image HAMMER_IMAGE = new Image("res/hammer.png");

    /**
     * Constructs a Hammer at the specified position.
     *
     * @param x The initial x-coordinate of the hammer.
     * @param y The initial y-coordinate of the hammer.
     */
    public Hammer(double x, double y) {
        super(HAMMER_IMAGE, x, y);
    }

    /**
     * Draws the hammer on the screen if it has not been collected.
     */
    public void update() {
        if (!isCollected) {
            super.draw(); // Bagel centers images automatically
//            drawBoundingBox(); // Uncomment for debugging
        }
    }

    /**
     * Marks the hammer as collected, removing it from the screen.
     */
    public void collect() {
        isCollected = true;
    }

    /**
     * Checks if the hammer has been collected.
     *
     * @return {@code true} if the hammer is collected, {@code false} otherwise.
     */
    public boolean isCollected() {
        return isCollected;
    }


    /**
     * Handles state changes when this Hammer collides with another entity.
     *
     * If the other entity is a {@link Mario} and the hammer has not yet been collected,
     * this method marks the hammer as collected and logs the event.
     *
     * @param other the entity that collided with this hammer
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario && !isCollected) {
            isCollected = true;
            System.out.println("hammer collected by mario and disappeared.");
        }
    }

}
