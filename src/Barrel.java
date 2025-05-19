import bagel.*;

/**
 * Represents a barrel in the game, affected by gravity and platform collisions.
 * The barrel can be destroyed, at which point it will no longer be drawn or interact with the environment.
 */
public class Barrel extends GravityEntity {
    private static final Image BARREL_IMAGE = new Image("res/barrel.png");
    private static final double BARREL_GRAVITY = 0.4;
    private boolean isDestroyed = false;

    /**
     * Constructs a new Barrel at the specified starting position.
     *
     * @param x The initial x-coordinate of the barrel.
     * @param y The initial y-coordinate of the barrel.
     */
    public Barrel(double x, double y) {
        super(BARREL_IMAGE, x, y, BARREL_GRAVITY);
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
            super.update(platforms);
        }
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


    /**
     * Handles collision with another game entity.
     *
     * If Mario holds a hammer and collide with barrel, the barrel is destroyed and removed from play.
     * Otherwise, Mario is hit by the barrel, triggering a game-over condition.
     *
     * @param other the {@link GameEntity} that collided with this barrel
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario) {
            Mario mario = (Mario) other;
            if (mario.holdHammer()) {
                isDestroyed = true;
                // Barrel disappears
                System.out.println("Mario smashed barrel with hammer!");
            } else {
                // trigger game over
                System.out.println("Mario hit by barrel! Game Over!");

            }
        }
    }



}
