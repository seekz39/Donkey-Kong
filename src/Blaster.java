public class Blaster {
    private boolean isPicked;
    private final int totalBullets = 5;

    public Blaster(){

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

}
