import bagel.*;

/**
 * Represents a ladder in the game.
 * The ladder falls under gravity until it lands on a platform.
 */
public class Ladder extends GravityEntity {
    private static final Image LADDER_IMAGE = new Image("res/ladder.png");
    private static final double LADDER_GRAVITY = 0.25;

    /**
     * Constructs a ladder at the specified position.
     *
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     */
    public Ladder(double x, double y) {
        super(LADDER_IMAGE, x, y, LADDER_GRAVITY);
    }

    @Override
    public void changeState(GameEntity other) {
        // Ladders do not respond to collisions
    }

}
