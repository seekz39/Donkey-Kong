import java.util.ArrayList;

/**
 * Abstract base class for all types of Monkey enemies.
 */
public abstract class Monkey {
    protected double x;
    protected double y;
    protected boolean faceRight;
    protected ArrayList<Integer> route = new ArrayList<>();
    protected double speed;
    protected double moveThisStep = 0;
    protected double velocityY = 0;

    private static final double DEFAULT_SPEED = 0.5;

    public Monkey(double x, double y, String direction, String routeStr) {
        this.x = x;
        this.y = y;
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

    public ArrayList<Integer> getRoute() {
        return route;
    }

    public void flipDirection() {
        faceRight = !faceRight;
    }

    public abstract void update(Platform[] platforms);

    public abstract void draw();
}


