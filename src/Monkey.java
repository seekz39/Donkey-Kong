import java.util.ArrayList;
import bagel.Image;

/**
 * Abstract base class for all types of Monkey enemies.
 */
public abstract class Monkey extends GravityEntity implements Movable {
    private boolean faceRight;
    private ArrayList<Integer> route = new ArrayList<>();
    private double speed;
    private int routeIndex = 0;
    private int directionSign = 1;
    private double distanceTravel = 0;
    private boolean isAlive = true;
    private static final double DEFAULT_SPEED = 0.5;
    private final Image imageLeft;
    private final Image imageRight;
    private static final double MONKEY_GRAVITY = 0.4;
    private final Platform[] platforms;

    public Monkey(double x, double y, String direction, String routeStr, Image left, Image right,  Platform[] platforms) {
        super(left, x, y, MONKEY_GRAVITY);
        this.imageLeft  = left;
        this.imageRight = right;
        this.faceRight = direction.equals("right");
        this.directionSign = faceRight ? 1 : -1;
        this.speed = DEFAULT_SPEED;
        for (String part : routeStr.split(",")) {
            this.route.add(Integer.parseInt(part));
        }
        this.platforms = platforms;
    }


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


        Platform currentPlatform = getCurrentPlatform(platforms);
        if (currentPlatform != null) {
            double platformLeft = currentPlatform.getBoundingBox().left();
            double platformRight = currentPlatform.getBoundingBox().right();

            // let monkey walk only in the current platform
            if ((this.getBoundingBox().right() >= platformRight) || (this.getBoundingBox().left() <= platformLeft)) {
                flipDirection();
                directionSign *= -1;
                distanceTravel = 0;
                setX(getX() + directionSign * speed);
                return;
            }
        }

        setX(getX() + directionSign * speed);
        distanceTravel += speed;

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



    public boolean isAlive() {
        return isAlive;
    }

    public boolean isFacingRight() {
        return faceRight;
    }

    public ArrayList<Integer> getRoute() {
        return route;
    }

    public void flipDirection() {
        faceRight = !faceRight;
    }



    @Override
    public void update(Platform[] platforms) {
        super.update(platforms); // apply gravity
        move();
    }

    @Override
    public void draw() {
        if (faceRight) {
            this.imageRight.draw(getX(), getY());
        } else {
            this.imageLeft.draw(getX(), getY());
        }
    }


    private Platform getCurrentPlatform(Platform[] platforms) {
        for (Platform platform : platforms) {
            if (this.getBoundingBox().bottom() == platform.getBoundingBox().top()) {
                return platform;
            }
        }
        return null;
    }

    public static String joinPath(int[] path) {
        StringBuilder record = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            record.append(path[i]);
            if (i < path.length - 1) record.append(",");
        }
        return record.toString();
    }


    @Override
    public void changeState(GameEntity other) {
        if (!isAlive) {
            return;
        }
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


