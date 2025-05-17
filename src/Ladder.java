import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents a ladder in the game.
 * The ladder falls under gravity until it lands on a platform.
 */
public class Ladder extends GravityEntity {
//    private final Image LADDER_IMAGE;
    private static final Image LADDER_IMAGE = new Image("res/ladder.png");
    public static double width;
    public static double height;
//    private int x, y;

    private double velocityY = 0; // Current vertical velocity due to gravity

    /**
     * Constructs a ladder at the specified position.
     *
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     */
    public Ladder(double x, double y) {
        super(LADDER_IMAGE, x, y);
        width = LADDER_IMAGE.getWidth();
        height = LADDER_IMAGE.getHeight();
    }

    /**
     * Draws the ladder on the screen.
     */

    /**
     * Updates the ladder's position by applying gravity and checking for platform collisions.
     * If a collision is detected, the ladder stops falling and rests on the platform.
     *
     * @param platforms An array of platforms in the game.
     */
    public void update(Platform[] platforms) {
        // 1) Apply gravity
        super.update(platforms);
        // 2) Draw the ladder after updating position
        draw();
    }

    /**
     * Returns the bounding box of the ladder for collision detection.
     *
     * @return A {@link Rectangle} representing the ladder's bounding box.
     */
//    public Rectangle getBoundingBox() {
//        return new Rectangle(
//                getX() - (LADDER_IMAGE.getWidth() / 2),
//                getY() - (LADDER_IMAGE.getHeight() / 2),
//                LADDER_IMAGE.getWidth(),
//                LADDER_IMAGE.getHeight()
//        );
//    }

    /**
     * Retrieves the ladder's image.
     *
     * @return An {@link Image} representing the barrel.
     */
    @Override
    public Image getImage() {
        return this.LADDER_IMAGE;
    }

    @Override
    public void draw() {
        super.draw();
    }


//    /**
//     * Gets the x-coordinate of the ladder.
//     *
//     * @return The current x-coordinate of the ladder.
//     */
//    public double getX() {
//        return x;
//    }

//    /**
//     * Gets the y-coordinate of the ladder.
//     *
//     * @return The current y-coordinate of the ladder.
//     */
//    public double getY() {
//        return y;
//    }

    /**
     * Gets the width of the ladder.
     *
     * @return The width of the ladder.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the ladder.
     *
     * @return The height of the ladder.
     */
    public double getHeight() {
        return height;
    }

    @Override
    public void changeState(GameEntity other) {
        // Ladders do not respond to collisions
    }
}
