import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Rectangle;
import java.util.ArrayList;

public class Blaster {
    private boolean isPicked;
    private final Image BLASTER_IMAGE = new Image("res/blaster.png");
    private final int totalBullets = 5;
    private double x, y;
    private boolean isCollected = false;
    private final double WIDTH, HEIGHT;
    private boolean isFacingRight;

    public Blaster(double x, double y, boolean isFacingRight){
        this.x = x;
        this.y = y;
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

    public void draw() {
        if (!isCollected) {
            BLASTER_IMAGE.draw(x, y); // Bagel centers images automatically
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

    public Rectangle getBoundingBox() {
        if (isCollected) {
            return new Rectangle(-1000, -1000, 0, 0); // Move off-screen if collected
        }
        return new Rectangle(
                x - (WIDTH / 2),  // Center-based positioning
                y - (HEIGHT / 2),
                WIDTH,
                HEIGHT
        );
    }

}
