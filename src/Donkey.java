import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 */
public class Donkey extends GravityEntity {
    private static final Image DONKEY_IMAGE = new Image("res/donkey_kong.png");
    private int health;
    private final int MAX_HEALTH = 5;
    private GamePlayScreen gamePlayScreen;

    /**
     * Constructs a new Donkey at the specified starting position.
     *
     * @param x The initial x-coordinate of Donkey.
     * @param y The initial y-coordinate of Donkey.
     */
    public Donkey(double x, double y) {
        super(DONKEY_IMAGE, x, y);
        this.health = MAX_HEALTH;

    }


    public void setGamePlayScreen(GamePlayScreen screen) {
        this.gamePlayScreen = screen;
    }


    public int getHealth() {
        return health;
    }


//    public void renderHealth(int health){}

    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Bullet) {
            health--;
            System.out.println("Donkey hit by bullet! Health: " + health);

            if (health <= 0) {
                System.out.println("☠Donkey defeated!");

            }
        }
    }

}
