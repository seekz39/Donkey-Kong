import bagel.util.Rectangle;

public abstract class GameEntity {
    public abstract Rectangle getBoundingBox();
    public abstract void update();
    public abstract void draw();

    public boolean collidesWith(GameEntity other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }

    public abstract void changeState(GameEntity collider);
}

