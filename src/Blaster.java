import bagel.Image;

public class Blaster extends GameEntity implements Equippable {
    private boolean isPicked;
    private static final Image BLASTER_IMAGE = new Image("res/blaster.png");
    private boolean isCollected = false;
    private boolean isFacingRight;

    public Blaster(double x, double y, boolean isFacingRight){
        super(BLASTER_IMAGE, x, y);
        this.isFacingRight = isFacingRight;
    }

    public void update(){
        if(!isCollected) {
            super.draw();
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
        if (other instanceof Mario && !isCollected) {
            isCollected = true;
            System.out.println("blaster collected by mario and disappeared.");
        }
    }
}
