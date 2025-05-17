import bagel.*;
import java.util.*;
import bagel.util.*;


public class Bullet extends GameEntity {
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


    public void update() {
        if (isAlive && traveled < MAX_DISTANCE) {
            double dx = isFacingRight ? SPEED : -SPEED;
            setX(getX() + dx);
            traveled += Math.abs(dx);
        } else {
            isAlive = false;
        }
    }

    public void draw() {
        if (isAlive) {
            if (isFacingRight) {
                BULLET_RIGHT_IMAGE.draw(getX(), getY());
            } else {
                BULLET_LEFT_IMAGE.draw(getX(), getY());
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Rectangle getBoundingBox() {
        Image img = isFacingRight ? BULLET_RIGHT_IMAGE : BULLET_LEFT_IMAGE;
        return img.getBoundingBoxAt(new Point(getX(), getY()));

    }

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