import bagel.Image;

/**
 * Represents an intelligent monkey that follows a specified patrol path.
 */
public class NormalMonkey extends Monkey {

    public NormalMonkey(double x, double y, String direction, int[] patrolPath) {
        super(x, y, direction, joinPath(patrolPath),
                new Image("res/normal_monkey_left.png"),
                new Image("res/normal_monkey_right.png"));
    }

    private static String joinPath(int[] path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            sb.append(path[i]);
            if (i < path.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}