import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 */
public class Donkey extends GravityEntity {
    private static final Image DONKEY_IMAGE = new Image("res/donkey_kong.png");
//    private final double X; // constant because x does not change, only relying on falling
    private int health;
    private final int MAX_HEALTH = 5;
    private GamePlayScreen gamePlayScreen;
//    private int x, y;

    /**
     * Constructs a new Donkey at the specified starting position.
     *
     * @param x The initial x-coordinate of Donkey.
     * @param y The initial y-coordinate of Donkey.
     */
    public Donkey(double x, double y) {
        super(DONKEY_IMAGE, x, y);
//        this.DONKEY_IMAGE = new Image("res/donkey_kong.png"); // Load Donkey Kong sprite
//        this.x = startX;
//        this.y = startY;
        this.health = MAX_HEALTH;

    }


    public void setGamePlayScreen(GamePlayScreen screen) {
        this.gamePlayScreen = screen;
    }

    /**
     * Updates Donkey's position by applying gravity and checking for platform collisions.
     * If Donkey lands on a platform, the velocity is reset to zero.
     *
     * @param platforms An array of platforms Donkey can land on.
     */
    public void update(Platform[] platforms) {
        super.update(platforms);
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
