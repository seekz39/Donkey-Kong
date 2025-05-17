import bagel.Image;
import bagel.util.Rectangle;

/**
 * Represents a Hammer collectible in the game.
 * The hammer can be collected by the player, at which point it disappears from the screen.
 */
public class Hammer extends GameEntity{
    private final double WIDTH, HEIGHT;
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
        this.WIDTH = HAMMER_IMAGE.getWidth();
        this.HEIGHT = HAMMER_IMAGE.getHeight();
    }

    /**
     * Returns the bounding box of the hammer for collision detection.
     * If the hammer has been collected, it returns an off-screen bounding box.
     *
     * @return A {@link Rectangle} representing the hammer's bounding box.
     */
//    public Rectangle getBoundingBox() {
//        if (isCollected) {
//            return new Rectangle(-1000, -1000, 0, 0); // Move off-screen if collected
//        }
//        return new Rectangle(
//                getX() - (WIDTH / 2),  // Center-based positioning
//                getY() - (HEIGHT / 2),
//                WIDTH,
//                HEIGHT
//        );
//    }

    /**
     * Draws the hammer on the screen if it has not been collected.
     */
    public void draw() {
        if (!isCollected) {
            HAMMER_IMAGE.draw(getX(), getY()); // Bagel centers images automatically
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

    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario && !isCollected) {
            isCollected = true;
            System.out.println("hammer collected by mario and disappeared.");
        }
    }

}
