import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents a ladder in the game.
 * The ladder falls under gravity until it lands on a platform.
 */
public class Ladder extends GravityEntity {
    private static final Image LADDER_IMAGE = new Image("res/ladder.png");
    public static double width;
    public static double height;
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
