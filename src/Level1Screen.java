import bagel.Input;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents the gameplay screen for Level 1.
 */
public class Level1Screen extends GamePlayScreen {

    private static final int LEVEL = 1;
    private static final int BARREL_SCORE = 100;
    private static final int BARREL_CROSS_SCORE = 30;
    private Mario mario;
    private ArrayList<Ladder> ladders = new ArrayList<>();
    private ArrayList<Barrel> barrels = new ArrayList<>();
    private Hammer hammer;
    private Donkey donkey;
    private Platform[] platforms;
    private boolean isGameOver = false;


    public Level1Screen(Properties gameProps) {
        super(gameProps);
        initializeGameObjects();
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }

    @Override
    public boolean update(Input input) {
//        currFrame++;
        setCurrFrame(getCurrFrame() + 1);

        // Draw background
        getBackground().drawFromTopLeft(0, 0);

        // 1) Draw and update platforms
        for (Platform platform : platforms) {
            if (platform != null) {
                platform.draw();
            }
        }

        // 2) Update ladders
        for (Ladder ladder : ladders) {
            if (ladder != null) {
                ladder.update(platforms);
            }
        }

        // 3) Update barrels
        for (Barrel barrel : barrels) {
            if (barrel == null) continue;
            if (mario.jumpOver(barrel)) {
                addScore(BARREL_CROSS_SCORE);
            }
            if (!barrel.isDestroyed() && mario.collidesWith(barrel)) {
                if (mario.holdHammer()) {
                    barrel.changeState(mario);
                    addScore(BARREL_SCORE);
                }
            }
            barrel.update(platforms);
        }

        // Mario touch Hammer
        if (mario.collidesWith(hammer)) {
            mario.changeState(hammer);
        }

        // 5) Draw hammer and donkey
        hammer.draw();
        donkey.update(platforms);

        // 6) Update Mario
        mario.updateLevel1(input, ladders, platforms);

        // 8) Display score and time left
        displayInfo();

        // 9) Return game state
        return isGameOver() || isPlayerWon();
    }

    @Override
    public void initializeGameObjects() {
        // 1) Create Mario
        String[] marioPos = GAME_PROPS.getProperty("mario.level1").split(",");
        double marioX = Double.parseDouble(marioPos[0]);
        double marioY = Double.parseDouble(marioPos[1]);
        this.mario = new Mario(marioX, marioY);

        // 2) Create Donkey Kong
        String[] donkeyPos = GAME_PROPS.getProperty("donkey.level1").split(",");
        double donkeyX = Double.parseDouble(donkeyPos[0]);
        double donkeyY = Double.parseDouble(donkeyPos[1]);
        this.donkey = new Donkey(donkeyX, donkeyY);

        // 3) Create the Barrels array
        int barrelCount = Integer.parseInt(GAME_PROPS.getProperty("barrel.level2.count"));
        for (int i = 1; i <= barrelCount; i++) {
            String data = GAME_PROPS.getProperty("barrel.level2." + i);
            if (data == null) continue;
            String[] coord = data.split(",");
            double x = Double.parseDouble(coord[0]);
            double y = Double.parseDouble(coord[1]);
            barrels.add(new Barrel(x, y));
        }

        // 4) Create the Ladders array
        int ladderCount = Integer.parseInt(GAME_PROPS.getProperty("ladder.level2.count"));
        for (int i = 1; i <= ladderCount; i++) {
            String data = GAME_PROPS.getProperty("ladder.level2." + i);
            if (data == null) continue;
            String[] coord = data.split(",");
            double x = Double.parseDouble(coord[0]);
            double y = Double.parseDouble(coord[1]);
            ladders.add(new Ladder(x, y));
        }

        // 5) Create the Platforms array
        String platformData = GAME_PROPS.getProperty("platforms.level1");
        if (platformData != null && !platformData.isEmpty()) {
            String[] platformEntries = platformData.split(";");
            this.platforms = new Platform[platformEntries.length];
            int pIndex = 0;
            for (String entry : platformEntries) {
                String[] coords = entry.trim().split(",");
                if (coords.length < 2) {
                    System.out.println("Warning: Invalid platform entry -> " + entry);
                    continue; // Skip invalid entries
                }
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                if (pIndex < platformEntries.length) {
                    platforms[pIndex] = new Platform(x, y);
                    pIndex++;
                }
            }
        } else {
            this.platforms = new Platform[0]; // No platform data
        }

        // 6) Create Hammer
        String[] hammerCoords = GAME_PROPS.getProperty("hammer.level1.1").split(",");
        double hammerX = Double.parseDouble(hammerCoords[0]);
        double hammerY = Double.parseDouble(hammerCoords[1]);
        this.hammer = new Hammer(hammerX, hammerY);
    }

    @Override
    public boolean isPlayerWon() {
        return mario.collidesWith(donkey) && mario.holdHammer();
    }

    @Override
    public boolean isGameOver() {

        // 1) Mario and Donkey collision
        if (mario.collidesWith(donkey) && !mario.holdHammer()) {
            System.out.println("Mario killed by donkey.");
            return true;
        }

        // 2) Mario and Barrel collision
        for (Barrel barrel : barrels) {
            if (barrel == null) continue;

            if (!barrel.isDestroyed() && mario.collidesWith(barrel) && !mario.holdHammer()) {
                System.out.println("Mario killed by barrel.");
                return true;
            }
        }

        // time end
        if (checkingGameTime()) {
            System.out.println("Mario killed by time.");
            return true;
        }
        return false;
    }

}
