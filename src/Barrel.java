import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

/**
 * Represents a barrel in the game, affected by gravity and platform collisions.
 * The barrel can be destroyed, at which point it will no longer be drawn or interact with the environment.
 */
public class Barrel extends GravityEntity {
    private static final Image BARREL_IMAGE = new Image("res/barrel.png");
//    private final double X; // constant because x does not change, only relying on falling
//    private double y;
//    private double velocityY = 0;
    private boolean isDestroyed = false;

    /**
     * Constructs a new Barrel at the specified starting position.
     *
     * @param x The initial x-coordinate of the barrel.
     * @param y The initial y-coordinate of the barrel.
     */
    public Barrel(double x, double y) {
        super(BARREL_IMAGE, x, y);
//        this.BARREL_IMAGE = new Image("res/barrel.png"); // Load barrel sprite
//        this.X = startX;
//        this.y = startY;
    }

    /**
     * Updates the barrel's position, applies gravity, checks for platform collisions,
     * and renders the barrel if it is not destroyed.
     *
     * @param platforms An array of platforms for collision detection.
     */
    @Override
    public void update(Platform[] platforms) {
        if (!isDestroyed) {
            super.update(platforms); //  from GravityEntity
            draw();
        }
    }

    /**
     * Draws the barrel on the screen if it is not destroyed.
     */

    @Override
    public void draw() {
        if (!isDestroyed) {
            BARREL_IMAGE.draw(getX(), getY());
//            drawBoundingBox(); // Uncomment for debugging
        }
    }


    /**
     * Creates and returns the barrel's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing the barrel's bounding box.
     *         If the barrel is destroyed, returns an off-screen bounding box.
     */
    @Override
    public Rectangle getBoundingBox() {
        if (isDestroyed) {
            return new Rectangle(-1000, -1000, 0, 0); // Off-screen if destroyed
        }
        return new Rectangle(
                getX() - (BARREL_IMAGE.getWidth() / 2),
                getY() - (BARREL_IMAGE.getHeight() / 2),
                BARREL_IMAGE.getWidth(),
                BARREL_IMAGE.getHeight()
        );
    }

    /**
     * Marks the barrel as destroyed, preventing it from being drawn or updated.
     */
    public void destroy() {
        isDestroyed = true;
        System.out.println("Barrel destroyed!");
    }

    /**
     * Checks if the barrel has been destroyed.
     *
     * @return {@code true} if the barrel is destroyed, {@code false} otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }


    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario) {
            Mario mario = (Mario) other;

            if (mario.holdHammer()) {
                isDestroyed = true; // Barrel disappears
                System.out.println("Mario smashed barrel with hammer!");
            } else {
                // trigger game over — this depends on how your game handles it
                System.out.println("Mario hit by barrel! Game Over!");

            }
        }
    }



}
