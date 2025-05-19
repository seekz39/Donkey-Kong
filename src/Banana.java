import bagel.Image;

/**
 * Represents a banana projectile shot by IntelligentMonkey.
 */
public class Banana extends GameEntity implements Movable{
    private final static double SPEED = 1.8;
    private final static int TRAVEL_MAX = 300;
    private static final Image BANANA_IMAGE = new Image("res/banana.png");
    private double distanceTraveled = 0;
    private boolean active = true;
    private final boolean goingRight;

    public Banana(double x, double y, boolean goingRight) {
        super(BANANA_IMAGE, x, y);
        this.goingRight = goingRight;

    }

    public void move() {

        double absDistance = goingRight ? SPEED : -SPEED;
        setX(getX() + absDistance);
        distanceTraveled += Math.abs(absDistance);
        if (distanceTraveled >= TRAVEL_MAX) {
            active = false;
        }

    }
    /**
     * Updates banana position and status
     */
    public void update() {
        if (!active) {
            return;
        }
        move();
        draw();
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario & active) {
            active = false;
            System.out.println("Banana hit Mario, game over!");
        }
    }
}
