import bagel.Image;
import java.util.ArrayList;

/**
 * Represents an intelligent monkey that follows a specified patrol path and shoots bananas.
 */
public class IntelligentMonkey extends Monkey {

    private int shootTimer = 0;
    private static final int SHOOT_FREQ = 5; // in seconds (adjust as needed)

    public IntelligentMonkey(double x, double y, String direction, int[] patrolPath) {
        super(
                x, y, direction, joinPath(patrolPath),
                new Image("res/intelli_monkey_left.png"),
                new Image("res/intelli_monkey_right.png")
        );

    }

    public Banana shootBanana() {
        return new Banana(getX(), getY(), isFacingRight());
    }


    public void update(Platform[] platforms) {
        super.update(platforms);
    }

    public boolean shouldShoot() {
        shootTimer++;
        if (shootTimer / 60 >= SHOOT_FREQ) {
            shootTimer = 0;
            return true;
        }
        return false;
    }

}