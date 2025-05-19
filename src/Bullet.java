import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;


/**
 * A projectile fired by Mario when he has a blaster.
 *
 * Bullets travel in a straight line (left or right) at a fixed speed,
 * disappear after traveling a maximum distance or upon colliding with an enemy or platform,
 * and implement the {@link Movable} interface for integration into the game loop.
 *
 */
public class Bullet extends GameEntity implements Movable {
    static private final double SPEED = 3.0;
    private double traveled = 0;
    private boolean isAlive = true;
    static private final int MAX_DISTANCE = 300;
    private boolean isFacingRight;
    private static final Image BULLET_LEFT_IMAGE = new Image("res/bullet_left.png");
    private final Image BULLET_RIGHT_IMAGE = new Image("res/bullet_right.png");


    public Bullet(double x, double y, boolean isFacingRight) {
        super(BULLET_LEFT_IMAGE, x, y);
        this.isFacingRight = isFacingRight;
    }

    /**
     * Moves the bullet one step according to its speed and facing direction.
     * The bullet becomes inactive once it has traveled its maximum distance.
     */
    public void move() {
        if (isAlive && traveled < MAX_DISTANCE) {
            double dx = isFacingRight ? SPEED : -SPEED;
            setX(getX() + dx);
            traveled += Math.abs(dx);
        } else {
            isAlive = false;
        }

    }

    /**
     * Updates the bullet's position and renders it if still alive.
     * This method should be called once per frame.
     */
    public void update() {
        move();
        draw();
    }

    /**
     * Draws the bullet's current image at its position if it is still active.
     * Chooses the left- or right-facing sprite based on its direction.
     */
    public void draw() {
        if (isAlive) {
            if (isFacingRight) {
                BULLET_RIGHT_IMAGE.draw(getX(), getY());
            } else {
                BULLET_LEFT_IMAGE.draw(getX(), getY());
            }
        }
    }

    /**
     * Returns whether the bullet is still active (has not exceeded its range or hit something).
     * @return True if the bullet is alive; false otherwise.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Handles collisions by marking the bullet as inactive when it hits
     * a {@link Monkey} or a {@link Platform}
     *
     * @param other The entity this bullet has collided with.
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Monkey) {
            isAlive = false;
            System.out.println("Bullet hit Monkey and disappeared!");
        }else {
            if (other instanceof Platform){
                isAlive = false;
                System.out.println("Bullet hit Platform and disappeared!");
            }
        }
    }

}