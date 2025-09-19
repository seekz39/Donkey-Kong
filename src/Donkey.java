import bagel.*;

/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 */
public class Donkey extends GravityEntity {
    private static final Image DONKEY_IMAGE = new Image("res/donkey_kong.png");
    private static final double DONKEY_GRAVITY = 0.4;
    private final int MAX_HEALTH = 5;
    private int health;


    /**
     * Constructs a new Donkey at the specified starting position.
     *
     * @param x The initial x-coordinate of Donkey.
     * @param y The initial y-coordinate of Donkey.
     */
    public Donkey(double x, double y) {
        super(DONKEY_IMAGE, x, y, DONKEY_GRAVITY);
        this.health = MAX_HEALTH;
    }

    /**
     * Returns Donkey Kong’s current health.
     * @return the remaining health points
     */
    public int getHealth() {
        return health;
    }


    /**
     * Handles collisions with other entities.
     * When hit by a bullet, Donkey Kong loses one health point and logs the event.
     * If health drops to zero or below, logs that Donkey Kong has been defeated.
     *
     * @param other the GameEntity that collided with Donkey Kong
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Bullet) {
            health--;
            System.out.println("Donkey hit by bullet! Health: " + health);

            if (health <= 0) {
                System.out.println("Donkey defeated!");
            }
        }
    }
}
