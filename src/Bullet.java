public class Bullet {
    private boolean isPicked;
    private final int totalBullets = 5;
    static private final double SPEED = 3.0;
    private double traveled = 0;
    private boolean isAlive = true;
    static private final int MAX_DISTANCE = 300;

    public Bullet(){
        super();
//        this.facingRight = facingRight;

    }

    public void update() {
        if(isAlive && traveled < MAX_DISTANCE){

        }
    }
}