import bagel.Image;

/**
 * Represents a banana projectile shot by an IntelligentMonkey.
 *
 * Bananas travel horizontally at a fixed speed until they reach a maximum range
 * or collide with Mario, at which point they become inactive and disappear.
 * Implements {@link Movable} for integration into the game’s movement loop.
 *
 */
public class Banana extends GameEntity implements Movable{
    private static final Image BANANA_IMAGE = new Image("res/banana.png");
    private final static double SPEED = 1.8;
    private final static int TRAVEL_MAX = 300;

    private double distanceTraveled = 0;
    private boolean active = true;
    private final boolean goingRight;

    public Banana(double x, double y, boolean goingRight) {
        super(BANANA_IMAGE, x, y);
        this.goingRight = goingRight;

    }

    /**
     * Moves the banana one step according to its speed and direction.
     * Marks the banana inactive if it has reached its maximum travel distance.
     */
    public void move() {
        double absDistance = goingRight ? SPEED : -SPEED;
        setX(getX() + absDistance);
        distanceTraveled += Math.abs(absDistance);
        if (distanceTraveled >= TRAVEL_MAX) {
            active = false;
        }

    }

    /**
     * Updates the banana by moving it and rendering it if still active.
     */
    public void update() {
        if (!active) {
            return;
        }
        move();
        draw();
    }

    /**
     * Indicates whether this banana is still active (has not expired or collided).
     *
     * @return true if the banana is active; false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Handles collision with another game entity.
     * If the collider is Mario and the banana is active, the banana becomes inactive
     *
     * @param other the GameEntity that collided with this banana
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario & active) {
            active = false;
            System.out.println("Banana hit Mario, game over!");
        }
    }
}
