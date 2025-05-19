/**
 * Defines a contract for game entities that can move according to their own rules.
 *
 * Any class implementing this interface must provide its own logic for advancing
 * one movement “step,” whether governed by physics, AI patrol routes, gravity, or
 * any other mechanism.
 */
public interface Movable {

    void move();

}
