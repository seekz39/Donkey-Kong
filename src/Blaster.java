import bagel.Image;


/**
 * Represents a blaster collectible in the game.
 *
 * The {@code Blaster} can be picked up by Mario when he collides with it,
 * at which point it disappears from the screen and grants Mario the ability to shoot bullets.
 *
 * Implements the {@link Collectable} interface for a unified collection mechanism.
 */

public class Blaster extends GameEntity implements Collectable {
    private boolean isPicked;
    private static final Image BLASTER_IMAGE = new Image("res/blaster.png");
    private boolean isCollected = false;
    private boolean isFacingRight;

    public Blaster(double x, double y, boolean isFacingRight){
        super(BLASTER_IMAGE, x, y);
        this.isFacingRight = isFacingRight;
    }

    /**
     * Renders the blaster on screen if it has not yet been collected.
     *
     * This method should be called once per frame in the game loop.
     *
     */
    public void update(){
        if(!isCollected) {
            super.draw();
        }
    }

    /**
     * Marks this blaster as collected, causing it to disappear from the game.
     */
    public void collect() {
        isCollected = true;
    }

    /**
     * Checks whether this blaster has already been collected by Mario.
     *
     * @return {@code true} if the blaster has been collected; {@code false} otherwise.
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Handles collision events with other game entities.
     * When Mario collides with an uncollected blaster, this method marks
     *
     * @param other The other {@link GameEntity} involved in the collision.
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario && !isCollected) {
            isCollected = true;
            System.out.println("blaster collected by mario and disappeared.");
        }
    }
}
