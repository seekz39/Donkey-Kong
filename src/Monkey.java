import java.util.ArrayList;
import bagel.Image;

/**
 * Abstract base class for all types of Monkey enemies.
 *
 * Monkeys follow a predefined patrol path across platforms, are affected by gravity,
 * and can be eliminated by bullets or Mario collisions.
 * Implements {@link Movable} to advance its position each frame.
 */
public abstract class Monkey extends GravityEntity implements Movable {
    private static final double MOVE_SPEED = 0.5;
    private static final double MONKEY_GRAVITY = 0.4;
    private final Image imageLeft;
    private final Image imageRight;
    private final Platform[] platforms;

    private ArrayList<Integer> route = new ArrayList<>();
    private boolean faceRight;
    private double speed;
    private int routeIndex = 0;
    private int directionSign = 1;
    private double distanceTravel = 0;
    private boolean isAlive = true;


    public Monkey(double x, double y, String direction, String routeStr, Image left, Image right, Platform[] platforms) {
        super(left, x, y, MONKEY_GRAVITY);
        this.imageLeft  = left;
        this.imageRight = right;
        this.faceRight = direction.equals("right");
        this.directionSign = faceRight ? 1 : -1;
        this.speed = MOVE_SPEED;
        for (String part : routeStr.split(",")) {
            this.route.add(Integer.parseInt(part));
        }
        this.platforms = platforms;
    }

    /**
     * Advances the monkey one step along its patrol path, handling edge-of-window
     * flips and platform-edge flips.
     */
    @Override
    public void move() {
        if (route.isEmpty()) return;

        // when monkey reach the edge of the window, flip direction
        if (getX() <= 0 || this.getBoundingBox().right() >= ShadowDonkeyKong.getScreenWidth()) {
            flipDirection();
            directionSign *= -1;
            distanceTravel = 0;
            setX(getX() + directionSign * speed);
            return;
        }

        // Flip direction at the current platform edges.
        Platform currentPlatform = getCurrentPlatform(platforms);
        if (currentPlatform != null) {
            double platformLeft = currentPlatform.getBoundingBox().left();
            double platformRight = currentPlatform.getBoundingBox().right();

            if ((this.getBoundingBox().right() >= platformRight) || (this.getBoundingBox().left() <= platformLeft)) {
                flipDirection();
                directionSign *= -1;
                distanceTravel = 0;
                setX(getX() + directionSign * speed);
                return;
            }
        }

        // Move along the route and accumulate distance.
        setX(getX() + directionSign * speed);
        distanceTravel += speed;

        // When the current segment is complete, advance and flip.
        if (distanceTravel >= route.get(routeIndex)) {
            distanceTravel = 0;
            routeIndex++;

            if (routeIndex >= route.size()) {
                routeIndex = 0;
            }

            flipDirection();
            directionSign *= -1;
            faceRight = directionSign > 0;
        }
    }


    /**
     * @return {@code true} if this monkey is still alive; {@code false} if it has been eliminated.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * @return {@code true} if this monkey is facing right; {@code false} if facing left.
     */
    public boolean isFacingRight() {
        return faceRight;
    }

    /** Reverse the monkey's facing direction. */
    public void flipDirection() {
        faceRight = !faceRight;
    }

    /**
     * Updates the monkey each frame by applying gravity and then moving.
     *
     * @param platforms array of platforms for collision checks
     */
    @Override
    public void update(Platform[] platforms) {
        super.update(platforms); // apply gravity
        move();
    }

    /** Draws the monkey using the correct image based on its facing direction. */
    @Override
    public void draw() {
        if (faceRight) {
            this.imageRight.draw(getX(), getY());
        } else {
            this.imageLeft.draw(getX(), getY());
        }
    }

    /**
     * Checks whether this monkey's bounding box rests on top of any platform.
     *
     * @param platforms array of platforms to test against
     * @return the current platform if standing, otherwise {@code null}
     */
    private Platform getCurrentPlatform(Platform[] platforms) {
        for (Platform platform : platforms) {
            if (this.getBoundingBox().bottom() == platform.getBoundingBox().top()) {
                return platform;
            }
        }
        return null;
    }

    /**
     * Converts an integer array into a comma-separated path string.
     *
     * @param path array of distances
     * @return a comma-delimited string of the path values
     * */
    public static String joinPath(int[] path) {
        StringBuilder record = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            record.append(path[i]);
            if (i < path.length - 1) record.append(",");
        }
        return record.toString();
    }

    /**
     * Changes the state of this monkey when colliding with another entity.
     *
     *   If hit by a {@link Bullet}, it dies and disappears.
     *   If hit by {@link Mario}, it dies and triggers game over logic.
     *
     * @param other the entity that this monkey collided with
     */
    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Bullet && isAlive) {
            isAlive = false;
            System.out.println("Monkey hit by bullet and disappeared.");
        }else{
            if (other instanceof Mario && isAlive) {
                isAlive = false;
                System.out.println("Monkey hit by Mario and game over.");
            }
        }
    }
}


