import bagel.Image;

/**
 * Represents a standard monkey enemy that patrols along a predefined route.
 *
 * A NormalMonkey moves back and forth across its patrol path,
 * reversing direction when it reaches the end of a segment or a platform edge.
 *
 */
public class NormalMonkey extends Monkey {

    public NormalMonkey(double x, double y, String direction, int[] patrolPath, Platform[] platforms) {
        super(x, y, direction, joinPath(patrolPath),
                new Image("res/normal_monkey_left.png"),
                new Image("res/normal_monkey_right.png"),
                platforms
        );
    }

}