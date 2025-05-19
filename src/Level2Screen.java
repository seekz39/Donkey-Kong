import bagel.Input;
import java.util.Properties;
import java.util.ArrayList;

/**
 * Represents the gameplay screen for Level 2.
 */
public class Level2Screen extends GamePlayScreen {

    private static final int LEVEL = 2;
    private static final int BARREL_SCORE = 100;
    private static final int BARREL_CROSS_SCORE = 30;
    private static final int MONKEY_SCORE = 100;
    private static final int BULLET_COUNT_DIFF_Y = 30;
    private Mario mario;
    private Hammer hammer;
    private Donkey donkey;
    private Platform[] platforms;
    private ArrayList<Ladder> ladders = new ArrayList<>();
    private ArrayList<Barrel> barrels = new ArrayList<>();
    private ArrayList<Banana> bananas = new ArrayList<>();
    private ArrayList<Blaster> blasters = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Monkey> monkeys = new ArrayList<>();
    private double healthX;
    private double healthY;


    public Level2Screen(Properties gameProps, int startingScore) {
        super(gameProps);
        initializeGameObjects();
        this.addScore(startingScore);
    }

    /**
     * Returns the current level number for this gameplay screen.
     *
     * @return the integer constant LEVEL (2 for Level2Screen)
     */
    @Override
    public int getLevel() {
        return LEVEL;
    }

    /**
     * Renders the HUD elements specific to Level 2, in addition to the base information.
     *
     * Calls the superclass to draw score and time, then displays Donkey Kong’s current health
     * and Mario’s remaining bullet count at the configured screen coordinates.
     *
     */
    @Override
    public void displayInfo() {
        super.displayInfo();

        String healthText = "DONKEY HEALTH " + donkey.getHealth();
        getStatusFont().drawString(healthText, healthX, healthY);

        String text = "BULLETS " + mario.getBulletsCount();
        getStatusFont().drawString(text, healthX, healthY + BULLET_COUNT_DIFF_Y);
    }


    /**
     * Advances the game state by one frame for Level 2 gameplay.
     *
     * This method increments the frame counter, renders the background and platforms,
     * updates ladders, barrels , bullets , and each monkey and bananas
     * handles hammer and blaster pickups and Mario’s update
     * then updates Donkey Kong, and finally displays the score.
     * After all updates, it checks for end‐of‐game or victory conditions.
     *
     * @param input the current keyboard and mouse input state
     * @return {@code true} if the game is over or the player has won; {@code false} otherwise
     */
    @Override
    public boolean update(Input input) {

//        currFrame++;
        setCurrFrame(getCurrFrame() + 1);

        // Draw background
        getBackground().drawFromTopLeft(0, 0);

        // Draw platforms
        for (Platform platform : platforms) {
            if (platform != null) {
                platform.draw();
            }
        }

        // Update ladders
        for (Ladder ladder : ladders) {
            if (ladder != null) {
                ladder.update(platforms);
            }
        }

        // Update barrels and collisions
        for (Barrel barrel : barrels) {
            if (barrel == null) continue;

            if (mario.jumpOver(barrel)) {
                addScore(BARREL_CROSS_SCORE);
                break;
            }

            if (!barrel.isDestroyed() && mario.collidesWith(barrel)) {
                if (mario.holdHammer()) {
                    barrel.changeState(mario);
                    addScore(BARREL_SCORE);
                }
            }
            barrel.update(platforms);
        }

        // update bullets and collisions
        for (int i = bullets.size() - 1; i >= 0; i--) {

            Bullet bullet = bullets.get(i);

            if (!bullet.isAlive()) {
                bullets.remove(i);
                continue;
            }

            //bullet collide with donkey
            if (bullet.collidesWith(donkey)) {
                donkey.changeState(bullet);
                bullet.changeState(donkey);
                bullets.remove(i);
                continue;
            }

            //bullet collide with monkey
            for (Monkey monkey : monkeys) {
                if (bullet.collidesWith(monkey)) {
                    monkey.changeState(bullet);
                    bullet.changeState(monkey);
                    addScore(MONKEY_SCORE);
                    break;
                }
            }

            //bullet collide with platform
            for (Platform platform : platforms) {
                if (bullet.collidesWith(platform)) {
                    bullet.changeState(platform);
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
            bullet.update();
        }

        //update monkey
        for (Monkey monkey : monkeys) {
            if (!monkey.isAlive()) {
                continue;
            }

            //mario with hammer collide with monkey
            if (monkey.collidesWith(mario) && mario.holdHammer()) {
                monkey.changeState(mario);
                addScore(MONKEY_SCORE);
                System.out.println("Mario killed a monkey using hammer.");
                break;
            }

            monkey.update(platforms);

            // intelligent monkey shoot banana
            if (monkey instanceof IntelligentMonkey) {
                IntelligentMonkey intel = (IntelligentMonkey) monkey;
                if (intel.shouldShoot()) {
                    bananas.add(intel.shootBanana());
                }
            }
        }

        // update banana
        for (Banana banana : bananas) {
            banana.update();
        }

        // update Hammer
        if (!hammer.isCollected() && mario.collidesWith(hammer)) {
            mario.changeState(hammer);
        }
        hammer.update();

        // update blaster
        for (Blaster blaster : blasters) {

            if (!blaster.isCollected() && mario.collidesWith(blaster)) {
                mario.changeState(blaster);
            }
            blaster.update();
        }

        // Update Mario
        mario.updateLevel2(input, ladders, platforms, bullets);

        // Update Donkey
        donkey.update(platforms);

        // Display score and time left
        displayInfo();

        // Return game state
        return isGameOver() || isPlayerWon();

    }

    /**
     * Checks whether the player has won the level.
     * The player wins if Mario reaches Donkey Kong while holding a hammer,
     * or if Donkey Kong's health has been reduced to zero or below.
     *
     * @return {@code true} if the win condition is met; {@code false} otherwise.
     */
    @Override
    public boolean isPlayerWon() {

        // Win if Mario reaches Donkey with the hammer…
        boolean reachedWithHammer = mario.collidesWith(donkey) && mario.holdHammer();
        // …or if Donkey’s health has dropped to zero
        boolean donkeyDefeated   = donkey.getHealth() <= 0;

        return reachedWithHammer || donkeyDefeated;
    }

    /**
     * Determines whether the game is over.
     *
     * The game ends if Mario collides with a living monkey while not holding a hammer;
     * if Mario is hit by an active banana;
     * if Mario collides with Donkey Kong while not holding a hammer;
     * if Mario collides with a non-destroyed barrel while not holding a hammer;
     * or if the game timer runs out.
     *
     * @return {@code true} if any end condition is met; {@code false} otherwise.
     */
    @Override
    public boolean isGameOver() {

        // 1) Mario and Monkey collision
        for (Monkey monkey : monkeys) {
            if (monkey.isAlive() && mario.collidesWith(monkey) && !mario.holdHammer()) {
                System.out.println("Mario killed by monkey.");
                return true;
            }
        }

        // 2) Mario and Banana collision
        for (Banana banana : bananas) {
            if (banana.isActive() && banana.collidesWith(mario)) {
                System.out.println("Mario killed by banana.");
                return true;
            }
        }

        // 3) Mario and Donkey collision
        if (mario.collidesWith(donkey) && !mario.holdHammer()) {
            System.out.println("Mario killed by donkey.");
            return true;
        }

        // 3) Mario and Barrel collision
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

    /**
     * Initializes all objects needed for level 2 such as Mario, Donkey Kong, barrels, ladders, platforms, hammer
     * blaster, bullet, monkey, and banana
     */
    @Override
    public void initializeGameObjects() {
        this.barrels   = new ArrayList<>();
        this.ladders   = new ArrayList<>();
        this.monkeys   = new ArrayList<>();
        this.bananas   = new ArrayList<>();
        this.bullets   = new ArrayList<>();
        this.blasters  = new ArrayList<>();

        // 1) Create Mario
        Properties gameProps = getGameProps();
        String[] marioPos = gameProps.getProperty("mario.level2").split(",");
        double marioX = Double.parseDouble(marioPos[0]);
        double marioY = Double.parseDouble(marioPos[1]);
        this.mario = new Mario(marioX, marioY);

        // 2) Create Donkey Kong
        String[] donkeyPos = gameProps.getProperty("donkey.level2").split(",");
        double donkeyX = Double.parseDouble(donkeyPos[0]);
        double donkeyY = Double.parseDouble(donkeyPos[1]);
        this.donkey = new Donkey(donkeyX, donkeyY);

        String[] donkeyHealth = gameProps.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.healthX = Double.parseDouble(donkeyHealth[0]);
        this.healthY = Double.parseDouble(donkeyHealth[1]);

        // 3) Create the Barrels array
        int barrelCount = Integer.parseInt(gameProps.getProperty("barrel.level2.count"));
        for (int i = 1; i <= barrelCount; i++) {
            String data = gameProps.getProperty("barrel.level2." + i);
            if (data == null) continue;
            String[] coord = data.split(",");
            double x = Double.parseDouble(coord[0]);
            double y = Double.parseDouble(coord[1]);
            barrels.add(new Barrel(x, y));
        }

        // 4) Create the Ladders array
        int ladderCount = Integer.parseInt(gameProps.getProperty("ladder.level2.count"));
        for (int i = 1; i <= ladderCount; i++) {
            String data = gameProps.getProperty("ladder.level2." + i);
            if (data == null) continue;
            String[] coord = data.split(",");
            double x = Double.parseDouble(coord[0]);
            double y = Double.parseDouble(coord[1]);
            ladders.add(new Ladder(x, y));
        }

        // 5) Create the Platforms array
        String[] platformEntries = gameProps.getProperty("platforms.level2").split(";");
        platforms = new Platform[platformEntries.length];
        for (int i = 0; i < platformEntries.length; i++) {
            String entry = platformEntries[i].trim();
            String[] coord = entry.split(",");
            double x = Double.parseDouble(coord[0]);
            double y = Double.parseDouble(coord[1]);
            platforms[i] = new Platform(x, y);
        }

        // 6) Create Hammer
        String[] hammerCoords = gameProps.getProperty("hammer.level2.1").split(",");
        double hammerX = Double.parseDouble(hammerCoords[0]);
        double hammerY = Double.parseDouble(hammerCoords[1]);
        this.hammer = new Hammer(hammerX, hammerY);

        // 9) Create the Blasters array
        int blastCount = Integer.parseInt(gameProps.getProperty("blaster.level2.count"));
        for (int i = 1; i <= blastCount; i++) {
            String data = gameProps.getProperty("blaster.level2." + i);
            if (data == null) continue;
            String[] coord = data.split(",");
            double x = Double.parseDouble(coord[0]);
            double y = Double.parseDouble(coord[1]);
            blasters.add(new Blaster(x, y, true));
        }

        int intelligentCount = Integer.parseInt(gameProps.getProperty("intelligentMonkey.level2.count"));
        for (int i = 1; i <= intelligentCount; i++) {
            String monkeyData = gameProps.getProperty("intelligentMonkey.level2." + i);
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

                IntelligentMonkey monkey = new IntelligentMonkey(x, y, direction, patrolPath, platforms);
                monkeys.add(monkey);
            }
        }

        int normalCount = Integer.parseInt(gameProps.getProperty("normalMonkey.level2.count"));
        for (int i = 1; i <= normalCount; i++) {
            String monkeyData = gameProps.getProperty("normalMonkey.level2." + i);
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

                NormalMonkey monkey = new NormalMonkey(x, y, direction, patrolPath, platforms);
                monkeys.add(monkey);
            }
        }

    }

}


