import bagel.Image;
import bagel.util.Rectangle;
import java.util.ArrayList;

public class Blaster extends GameEntity {
    private boolean isPicked;
    private static final Image BLASTER_IMAGE = new Image("res/blaster.png");
    private final int totalBullets = 5;
    private boolean isCollected = false;
    private final double WIDTH, HEIGHT;
    private boolean isFacingRight;

    public Blaster(double x, double y, boolean isFacingRight){
        super(BLASTER_IMAGE, x, y);
        this.isFacingRight = isFacingRight;
        this.WIDTH = BLASTER_IMAGE.getWidth();
        this.HEIGHT = BLASTER_IMAGE.getHeight();
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    public void update(){
    }

    public int getTotalBullets() {
        return totalBullets;
    }

    @Override
    public void draw() {
        if (!isCollected) {
            BLASTER_IMAGE.draw(getX(), getY()); // Bagel centers images automatically
//            drawBoundingBox(); // Uncomment for debugging
        }
    }

    public void collect() {
        isCollected = true;
    }

    /**
     * Checks if the hammer has been collected.
     *
     * @return {@code true} if the hammer is collected, {@code false} otherwise.
     */
    public boolean isCollected() {
        return isCollected;
    }

    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Mario && !isPicked) {
            isPicked = true;
            System.out.println("blaster collected by mario and disappeared.");
        }

    }

}
