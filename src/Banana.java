import bagel.Image;
import bagel.util.Rectangle;

/**
 * Represents a banana projectile shot by IntelligentMonkey.
 */
public class Banana {
    private final static double SPEED = 1.8;
    private final static int TRAVEL_MAX = 300;
    private final Image bananaImg = new Image("res/banana.png");
    private double x, y;
    private double distanceTraveled = 0;
    private boolean active = true;
    private final boolean goingRight;

    public Banana(double x, double y, boolean goingRight) {
        this.x = x;
        this.y = y;
        this.goingRight = goingRight;
    }

    /**
     * Updates banana position and status
     */
    public void update() {
        if (!active) return;

        double dx = goingRight ? SPEED : -SPEED;
        x += dx;
        distanceTraveled += Math.abs(dx);

        if (distanceTraveled >= TRAVEL_MAX) {
            active = false;
        }
    }

    public void draw() {
        if (active) {
            bananaImg.draw(x, y);
        }
    }

    public boolean isActive() {
        return active;
    }

//    public Rectangle getBoundingBox() {
//        return bananaImg.getBoundingBoxAt(x, y);
//    }
}
