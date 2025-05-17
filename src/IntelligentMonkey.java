import bagel.Image;
import java.util.ArrayList;

/**
 * Represents an intelligent monkey that follows a specified patrol path and shoots bananas.
 */
public class IntelligentMonkey extends Monkey {
//    private final ArrayList<Banana> bananas = new ArrayList<>();

    private int shootTimer = 0;
    private static final int SHOOT_FREQ = 5; // in seconds (adjust as needed)

    public IntelligentMonkey(double x, double y, String direction, int[] patrolPath) {
        super(
                x, y, direction, joinPath(patrolPath),
                new Image("res/intelli_monkey_left.png"),
                new Image("res/intelli_monkey_right.png")
        );

    }

    private static String joinPath(int[] path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            sb.append(path[i]);
            if (i < path.length - 1) sb.append(",");
        }
        return sb.toString();
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