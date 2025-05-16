import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 */
public class Donkey extends GravityEntity {
    private final Image DONKEY_IMAGE;
//    private final double X; // constant because x does not change, only relying on falling
    private int health;
    private final int MAX_HEALTH = 5;

    /**
     * Constructs a new Donkey at the specified starting position.
     *
     * @param startX The initial x-coordinate of Donkey.
     * @param startY The initial y-coordinate of Donkey.
     */
    public Donkey(double startX, double startY) {
        this.DONKEY_IMAGE = new Image("res/donkey_kong.png"); // Load Donkey Kong sprite
        this.x = startX;
        this.y = startY;
    }

    /**
     * Updates Donkey's position by applying gravity and checking for platform collisions.
     * If Donkey lands on a platform, the velocity is reset to zero.
     *
     * @param platforms An array of platforms Donkey can land on.
     */
    public void update(Platform[] platforms) {
        super.update(platforms); // 使用 GravityEntity 的 gravity + collision
//        draw();
    }

    /**
     * Checks if Donkey is colliding with a given platform.
     *
     * @param platform The platform to check for collision.
     * @return {@code true} if Donkey is touching the platform, {@code false} otherwise.
     */
//    private boolean isTouchingPlatform(Platform platform) {
//        Rectangle donkeyBounds = getBoundingBox();
//        return donkeyBounds.intersects(platform.getBoundingBox());
//    }

    /**
     * Draws Donkey on the screen.
     */
//    @Override
//    public void draw() {
//        DONKEY_IMAGE.draw(X, y);
////        drawBoundingBox(); // Uncomment for debugging
//    }


    /**
     * Retrieves the barrel's image.
     *
     * @return An {@link Image} representing the barrel.
     */
    @Override
    protected Image getImage() {
        return this.DONKEY_IMAGE;
    }

    /**
     * Returns Donkey's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing Donkey's bounding box.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                x - (DONKEY_IMAGE.getWidth() / 2),
                y - (DONKEY_IMAGE.getHeight() / 2),
                DONKEY_IMAGE.getWidth(),
                DONKEY_IMAGE.getHeight()
        );
    }


    /**
     * Gets the x-coordinate of the DONKEY.
     *
     * @return The current x-coordinate of the Donkey.
     */
    public double getX() { return x; }

    /**
     * Gets the y-coordinate of the donkey.
     *
     * @return The current y-coordinate of the Donkey.
     */
    public double getY() { return y; }


    public void renderHealth(int health){}

    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Bullet) {
            health--;
            System.out.println("Donkey hit by bullet! Health: " + health);

            if (health <= 0) {
                System.out.println("☠Donkey defeated!");
                //game over logic
            }
        }
    }

}
