import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class GameEntity {
    private Image image;
    private double x, y;

    public GameEntity(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns this entity’s bounding box for collision detection.
     * Uses the entity’s current image and position to compute the rectangle.
     *
     * @return a Rectangle representing the entity’s bounds
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(new Point(x, y));
    }

    /**
     * Tests whether this entity collides with another entity.
     *
     * @param other the other GameEntity to test against
     * @return {@code true} if this entity’s bounding box intersects the other’s; {@code false} otherwise
     */
    public boolean collidesWith(GameEntity other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }

    /**
     * Handles a collision with another entity. Subclasses may override
     * to implement custom collision responses (e.g., pickups, damage).
     *
     * @param collider the entity that collided with this one
     */
    public void changeState(GameEntity collider){}

    /**
     * Returns the current x-coordinate of this entity.
     * @return the x-coordinate
     */
    public double getX() { return x; }

    /**
     * Sets the x-coordinate of this entity.
     * @param x the new x-coordinate
     */
    public void setX(double x) { this.x = x; }

    /**
     * Returns the current y-coordinate of this entity.
     * @return the y-coordinate
     */
    public double getY() { return y; }

    /**
     * Sets the y-coordinate of this entity.
     * @param y the new y-coordinate
     */
    public void setY(double y) { this.y = y; }

    /**
     * Renders the entity’s image at its current position.
     */
    public void draw(){
        image.draw(getX(),getY());
    }

    /**
     * Returns the image used to represent this entity.
     * @return the Image object
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets a new image for this entity.
     * @param image the Image to use for rendering
     */
    public void setImage(Image image) {
        this.image = image;
    }

}

