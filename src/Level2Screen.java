import bagel.Input;
import java.util.Properties;
import java.util.ArrayList;

/**
 * Represents the gameplay screen for Level 2.
 */
public class Level2Screen extends GamePlayScreen {

    //private static final int LEVEL = 1;

    private Mario mario;
    private Barrel[] barrels;
    private Ladder[] ladders;
    private Hammer hammer;
    private Donkey donkey;
    private Platform[] platforms;
    private GamePlayScreen gamePlayScreen;

    private ArrayList<Banana> bananas = new ArrayList<>();
    private Blaster[] blasters;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Monkey> monkeys = new ArrayList<>();
    private ArrayList<IntelligentMonkey> intelligentMonkeys = new ArrayList<>();
    private ArrayList<NormalMonkey> normalMonkeys = new ArrayList<>();
//    private int currFrame = 0;
//    private int score = 0;
    private boolean isGameOver = false;
    private boolean playerWon = false;
    private double healthX;
    private double healthY;
    private double bulletCountX;
    private double bulletCountY;
    private static final int BARREL_SCORE = 100;
    private static final int TIME_DISPLAY_DIFF_Y = 30;
    private static final int BARREL_CROSS_SCORE = 30;

    @Override
    public boolean isLevelCompleted() {
        // Win if Mario reaches Donkey with the hammer…
        boolean reachedWithHammer = mario.hasReached(donkey) && mario.holdHammer();
        // …or if Donkey’s health has dropped to zero
        boolean donkeyDefeated   = donkey.getHealth() <= 0;

//        return mario.hasReached(donkey) && mario.holdHammer();
        return reachedWithHammer || donkeyDefeated;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();

        String healthText = "DONKEY HEALTH " + donkey.getHealth();
        getStatusFont().drawString(healthText, healthX, healthY);

        String text = "BULLETS " + mario.getBulletsCount();
        getStatusFont().drawString(text, bulletCountX, bulletCountY);
    }


    public Level2Screen(Properties gameProps) {
        super(gameProps);

        // === Mario ===
        String[] marioPos = gameProps.getProperty("mario.level2").split(",");
        mario = new Mario(Double.parseDouble(marioPos[0]), Double.parseDouble(marioPos[1]));

        // === Donkey ===
        String[] donkeyPos = gameProps.getProperty("donkey.level2").split(",");
        donkey = new Donkey(Double.parseDouble(donkeyPos[0]), Double.parseDouble(donkeyPos[1]));

        // === Barrels ===
        int barrelCount = Integer.parseInt(gameProps.getProperty("barrel.level2.count"));
        barrels = new Barrel[barrelCount];
        for (int i = 1; i <= barrelCount; i++) {
            String[] coords = gameProps.getProperty("barrel.level2." + i).split(",");
            barrels[i - 1] = new Barrel(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        }

        // === Ladders ===
        int ladderCount = Integer.parseInt(gameProps.getProperty("ladder.level2.count"));
        ladders = new Ladder[ladderCount];
        for (int i = 1; i <= ladderCount; i++) {
            String[] coords = gameProps.getProperty("ladder.level2." + i).split(",");
            ladders[i - 1] = new Ladder(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        }

        // === Platforms ===
        String[] platformEntries = gameProps.getProperty("platforms.level2").split(";");
        platforms = new Platform[platformEntries.length];
        for (int i = 0; i < platformEntries.length; i++) {
            String[] coords = platformEntries[i].trim().split(",");
            platforms[i] = new Platform(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        }

        // === Hammer ===
        String[] hammerCoords = gameProps.getProperty("hammer.level2.1").split(",");
        hammer = new Hammer(Double.parseDouble(hammerCoords[0]), Double.parseDouble(hammerCoords[1]));

        // 9) Create Blasters
        int blasterCount = Integer.parseInt(GAME_PROPS.getProperty("blaster.level2.count"));
        blasters = new Blaster[blasterCount];
        for (int i = 1; i <= blasterCount; i++) {
            String[] coords = GAME_PROPS.getProperty("blaster.level2." + i).split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            blasters[i - 1] = new Blaster(x, y, true);
        }
        initializeGameObjects();
    }



    @Override
    public boolean update(Input input) {

        currFrame++;

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
//                score += BARREL_CROSS_SCORE;
                addScore(BARREL_CROSS_SCORE);
            }
            if (!barrel.isDestroyed() && mario.isTouchingBarrel(barrel)) {
                if (!mario.holdHammer()) {
                    isGameOver = true;
                } else {
                    barrel.destroy();
//                    score += BARREL_SCORE;
                    addScore(BARREL_SCORE);
                }
            }
            barrel.update(platforms);
        }


        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);

            if (!bullet.isAlive()) {
                bullets.remove(i);
                continue;
            }

            bullet.update();
            bullet.draw();

            if (bullet.collidesWith(donkey)) {
                donkey.changeState(bullet);
                bullet.changeState(donkey);
                bullets.remove(i);
                continue;
            }

            for (Monkey monkey : monkeys) {
                if (monkey.isAlive() && bullet.collidesWith(monkey)) {
                    monkey.changeState(bullet);
                    bullet.changeState(monkey);
                    break;
                }
            }
        }


        for (Monkey monkey : monkeys) {
            if (monkey.isAlive()) {

                if (monkey.collidesWith(mario)) {
                    System.out.println("Mario touched a monkey — Game Over.");
                    isGameOver = true;
                    break;
                }

                monkey.update(platforms);
                monkey.draw();

                // IntelligentMonkey shoot banana
                if (monkey instanceof IntelligentMonkey) {
                    IntelligentMonkey intelMonkey = (IntelligentMonkey) monkey;
                    if (intelMonkey.shouldShoot()) {
                        bananas.add(intelMonkey.shootBanana());
                    }
                }
            }
        }


        // draw banana
        for (Banana banana : bananas) {
            if (banana.isActive()) {
                if (banana.collidesWith(mario)) {
                    banana.changeState(mario);
                    isGameOver = true;
                }

                banana.update();
                banana.draw();
            }
        }


        // 4) Check game time and donkey status
        if (checkingGameTime()) {
            isGameOver = true;
        }

        donkey.update(platforms);

        // 5) Draw hammer and donkey
        hammer.draw();
        donkey.draw();
        for (Blaster b : blasters) {
            b.draw();
        }


        // 6) Update Mario
        mario.updateLevel2(input, ladders, platforms, hammer, blasters, bullets);

        // 7) Check if Mario reaches Donkey
        if (mario.hasReached(donkey) && !mario.holdHammer()) {
            isGameOver = true;
        }

        // 8) Display score and time left
        displayInfo();

        // 9) Return game state
        return isGameOver || isLevelCompleted();

    }


    @Override
    public void initializeGameObjects() {

        this.intelligentMonkeys = new ArrayList<>();
        this.normalMonkeys = new ArrayList<>();
        this.monkeys = new ArrayList<>();

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

        String[] donkeyHealth = GAME_PROPS.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.healthX = Double.parseDouble(donkeyHealth[0]);
        this.healthY = Double.parseDouble(donkeyHealth[1]);

        this.bulletCountX = Double.parseDouble(donkeyHealth[0]);
        this.bulletCountY = Double.parseDouble(donkeyHealth[1]) + 30;

        donkey.setGamePlayScreen(this);

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
        String[] hammerCoords = GAME_PROPS.getProperty("hammer.level2.1").split(",");
        double hammerX = Double.parseDouble(hammerCoords[0]);
        double hammerY = Double.parseDouble(hammerCoords[1]);
        this.hammer = new Hammer(hammerX, hammerY);

        // 9) Create the Blasters array
        int blasterCount = Integer.parseInt(GAME_PROPS.getProperty("blaster.level2.count"));
        this.blasters = new Blaster[blasterCount];
        int blasterIndex = 0;
        for (int i = 1; i <= blasterCount; i++) {
            String blasterData = GAME_PROPS.getProperty("blaster.level2." + i);
            if (blasterData != null) {
                String[] coords = blasterData.split(",");
                if (coords.length < 2) {
                    System.out.println("Warning: Incomplete data for blaster." + i);
                    continue; // Skip invalid entries
                }
                double blasterX = Double.parseDouble(coords[0]);
                double blasterY = Double.parseDouble(coords[1]);
                if (blasterIndex < blasterCount) {
                    blasters[blasterIndex] = new Blaster(blasterX, blasterY, true);
                    blasterIndex++;
                }
            }
        }

        int intelligentCount = Integer.parseInt(GAME_PROPS.getProperty("intelligentMonkey.level2.count"));
        for (int i = 1; i <= intelligentCount; i++) {
            String monkeyData = GAME_PROPS.getProperty("intelligentMonkey.level2." + i);
            if (monkeyData != null) {
                String[] parts = monkeyData.split(";");
                if (parts.length < 3) {
                    System.out.println("Warning: Incomplete data for intelligentMonkey." + i);
                    continue;
                }

                String[] coords = parts[0].split(",");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);

                String direction = parts[1];

                String[] routeStr = parts[2].split(",");
                int[] patrolPath = new int[routeStr.length];
                for (int j = 0; j < routeStr.length; j++) {
                    patrolPath[j] = Integer.parseInt(routeStr[j]);
                }

                IntelligentMonkey monkey = new IntelligentMonkey(x, y, direction, patrolPath);
                intelligentMonkeys.add(monkey);
                monkeys.add(monkey);
            }
        }

        int normalCount = Integer.parseInt(GAME_PROPS.getProperty("normalMonkey.level2.count"));
        for (int i = 1; i <= normalCount; i++) {
            String monkeyData = GAME_PROPS.getProperty("normalMonkey.level2." + i);
            if (monkeyData != null) {
                String[] parts = monkeyData.split(";");
                if (parts.length < 3) {
                    System.out.println("Warning: Incomplete data for normalMonkey." + i);
                    continue;
                }

                String[] coords = parts[0].split(",");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);

                String direction = parts[1];

                String[] routeStr = parts[2].split(",");
                int[] patrolPath = new int[routeStr.length];
                for (int j = 0; j < routeStr.length; j++) {
                    patrolPath[j] = Integer.parseInt(routeStr[j]);
                }

                NormalMonkey monkey = new NormalMonkey(x, y, direction, patrolPath);
                normalMonkeys.add(monkey);
                monkeys.add(monkey);
            }
        }
    }


}


