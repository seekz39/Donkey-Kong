import java.util.ArrayList;
import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Abstract base class for all types of Monkey enemies.
 */
public abstract class Monkey extends GravityEntity {
    protected Image imageLeft;
    protected Image imageRight;
    protected boolean faceRight;
    protected ArrayList<Integer> route = new ArrayList<>();
    protected double speed;
    protected double moveThisStep = 0;
    protected double velocityY = 0;

    private static final double DEFAULT_SPEED = 0.5;

    public Monkey(double x, double y, String direction, String routeStr) {
        this.x = x;
        this.y = y;
        this.imageLeft = new Image(getImagePathLeft());
        this.imageRight = new Image(getImagePathRight());
        this.faceRight = direction.equals("right");
        this.speed = DEFAULT_SPEED;
        for (String part : routeStr.split(",")) {
            this.route.add(Integer.parseInt(part));
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isFacingRight() {
        return faceRight;
    }
    protected abstract String getImagePathLeft();
    protected abstract String getImagePathRight();

    public ArrayList<Integer> getRoute() {
        return route;
    }

    public void flipDirection() {
        faceRight = !faceRight;
    }

    @Override
    public void update(Platform[] platforms) {
        super.update(platforms); // apply gravity

    }

    @Override
    public void draw() {
        if (faceRight) {
            this.imageRight.draw(x, y);
        } else {
            this.imageLeft.draw(x, y);
        }
    }

    @Override
    protected Image getImage() {
        return faceRight ? imageRight : imageLeft;
    }

    @Override
    public Rectangle getBoundingBox() {
        return getImage().getBoundingBoxAt(new Point(x, y));
    }


}


