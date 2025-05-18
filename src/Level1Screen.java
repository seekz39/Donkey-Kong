import bagel.Input;
import java.util.Properties;

/**
 * Represents the gameplay screen for Level 1.
 */
public class Level1Screen extends GamePlayScreen {

    private static final int LEVEL = 1;

    private Mario mario;
    private Barrel[] barrels;
    private Ladder[] ladders;
    private Hammer hammer;
    private Donkey donkey;
    private Platform[] platforms;
    private boolean isGameOver = false;

    private static final int BARREL_SCORE = 100;
    private static final int TIME_DISPLAY_DIFF_Y = 30;
    private static final int BARREL_CROSS_SCORE = 30;

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
                if (!mario.holdHammer()) {
                    isGameOver = true;
                } else {
                    barrel.destroy();
                    addScore(BARREL_SCORE);
                }
            }
            barrel.update(platforms);
        }

        // 4) Check game time and donkey status
        if (checkingGameTime()) {
            isGameOver = true;
        }
        donkey.update(platforms);

        // Mario touch Hammer
        if (mario.collidesWith(hammer)) {
            mario.changeState(hammer);
        }

        // 5) Draw hammer and donkey
        hammer.draw();
        donkey.draw();

        // 6) Update Mario
        mario.updateLevel1(input, ladders, platforms);

        // 7) Check if Mario reaches Donkey
        if (mario.collidesWith(donkey) && !mario.holdHammer()) {
            isGameOver = true;
        }

        // 8) Display score and time left
        displayInfo();

        // 9) Return game state
        return isGameOver || isPlayerWon();
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
        int barrelCount = Integer.parseInt(GAME_PROPS.getProperty("barrel.level1.count"));
        this.barrels = new Barrel[barrelCount];
        int barrelIndex = 0;
        for (int i = 1; i <= barrelCount; i++) {
            String barrelData = GAME_PROPS.getProperty("barrel.level1." + i);
            if (barrelData != null) {
                String[] coords = barrelData.split(",");
                if (coords.length < 2) {
                    System.out.println("Warning: Incomplete data for barrel." + i);
                    continue; // Skip invalid entries
                }
                double barrelX = Double.parseDouble(coords[0]);
                double barrelY = Double.parseDouble(coords[1]);
                if (barrelIndex < barrelCount) {
                    barrels[barrelIndex] = new Barrel(barrelX, barrelY);
                    barrelIndex++;
                }
            }
        }

        // 4) Create the Ladders array
        int ladderCount = Integer.parseInt(GAME_PROPS.getProperty("ladder.level1.count"));
        this.ladders = new Ladder[ladderCount];
        int ladderIndex = 0;
        for (int i = 1; i <= ladderCount; i++) {
            String ladderData = GAME_PROPS.getProperty("ladder.level1." + i);
            if (ladderData != null) {
                String[] coords = ladderData.split(",");
                if (coords.length < 2) {
                    System.out.println("Warning: Incomplete data for ladder." + i);
                    continue; // Skip invalid entries
                }
                double ladderX = Double.parseDouble(coords[0]);
                double ladderY = Double.parseDouble(coords[1]);
                if (ladderIndex < ladderCount) {
                    ladders[ladderIndex] = new Ladder(ladderX, ladderY);
                    ladderIndex++;
                }
            }
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

    }
