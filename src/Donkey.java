import bagel.*;


/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 */
public class Donkey extends GravityEntity {
    private static final Image DONKEY_IMAGE = new Image("res/donkey_kong.png");
    private static final double DONKEY_GRAVITY = 0.4;
    private int health;
    private final int MAX_HEALTH = 5;

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

    public int getHealth() {
        return health;
    }

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
