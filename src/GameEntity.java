import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class GameEntity {
    private Image image;
    private double x, y;

//    public abstract Rectangle getBoundingBox();

    public GameEntity(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the bounding box of the ENTITY for collision detection.
     * If the ENTITY has been collected, it returns an off-screen bounding box.
     *
     * @return A {@link Rectangle} representing the hammer's bounding box.
     */

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(new Point(x, y));
    }

    public boolean collidesWith(GameEntity other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }
    public abstract void changeState(GameEntity collider);

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public void draw(){
        image.draw(getX(),getY());
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}

