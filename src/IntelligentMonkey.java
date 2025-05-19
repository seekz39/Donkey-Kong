import bagel.Image;

/**
 * Represents an intelligent monkey that follows a specified patrol path and shoots bananas.
 *
 * Inherits movement, gravity, and drawing logic from {@link Monkey}.
 * Adds banana-shooting behavior on a fixed frequency timer.
 *
 */
public class IntelligentMonkey extends Monkey {

    private int shootTimer = 0;
    private static final int SHOOT_FREQ = 5; // shooting interval in seconds

    public IntelligentMonkey(double x, double y, String direction, int[] patrolPath, Platform[] platforms) {
        super(
                x, y, direction, joinPath(patrolPath),
                new Image("res/intelli_monkey_left.png"),
                new Image("res/intelli_monkey_right.png"),
                platforms
        );
    }

    /**
     * Creates a new banana projectile at the monkey’s current location,
     * traveling in the direction the monkey is facing.
     *
     * @return a fresh {@link Banana} instance ready to be added to the game
     */
    public Banana shootBanana() {
        return new Banana(getX(), getY(), isFacingRight());
    }

    /**
     * Determines whether it is time for the monkey to shoot again.
     *
     * Internally increments a frame-based counter and resets it every
     * {@code SHOOT_FREQ} seconds (assuming 60 frames per second).
     *
     * @return {@code true} if the shoot interval has elapsed; {@code false} otherwise
     */
    public boolean shouldShoot() {
        shootTimer++;
        if (shootTimer / 60 >= SHOOT_FREQ) {
            shootTimer = 0;
            return true;
        }
        return false;
    }

}