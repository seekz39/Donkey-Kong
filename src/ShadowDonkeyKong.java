import bagel.*;
import java.util.Properties;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes characters, platforms, ladders, and other game objects,
 * and runs the game loop to ensure smooth gameplay.
 */
public class ShadowDonkeyKong extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private int level1Score = 0;

    private State state = State.HOME;
    private HomeScreen homeScreen;
    private GamePlayScreen gamePlayScreen;
    private GameEndScreen gameEndScreen;

    public static double screenWidth;
    public static double screenHeight;

    /**
     * Constructs a new instance of the ShadowDonkeyKong game.
     * Initializes the game window using provided properties and sets up the home screen.
     *
     * @param gameProps     A {@link Properties} object containing game configuration settings
     *                      such as window width and height.
     * @param messageProps  A {@link Properties} object containing localized messages or UI labels,
     *                      including the title for the home screen.
     */
    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        this.screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        this.screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }


    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        switch (state) {

            case HOME -> {
                if (homeScreen.update(input)) {
                    int level = homeScreen.getSelectedLevel();
                    if (level == 1) {
                        startLevel1();
                    } else {
                        startLevel2();
                    }
                }
            }

            case PLAYING -> {
                if (gamePlayScreen.update(input)) {
                    handleEndOfPlay();
                }
            }

            case END -> {
                if (gameEndScreen.update(input)) {
                    resetToHome();
                }
            }
        }

    }

    /**
     * Initializes and starts Level 1 gameplay.
     * Sets the gamePlayScreen to a new Level1Screen and transitions the state to PLAYING.
     */
    private void startLevel1() {
        gamePlayScreen = new Level1Screen(GAME_PROPS);
        state = State.PLAYING;
    }

    /**
     * Initializes and starts Level 2 gameplay, carrying over the score from Level 1.
     * Sets the gamePlayScreen to a new Level2Screen and transitions the state to PLAYING.
     */
    private void startLevel2() {
        gamePlayScreen = new Level2Screen(GAME_PROPS, level1Score);
        state = State.PLAYING;
    }

    /**
     * Handles the end-of-play transition logic after a gameplay screen completes.
     *
     *   If Level 1 was won, captures its score and immediately starts Level 2.
     *   Otherwise, computes final score and time remaining, creates the end screen,
     *   and transitions the state to END.
     *
     */
    private void handleEndOfPlay() {
        boolean isWon = gamePlayScreen.isPlayerWon();
        boolean isLost = gamePlayScreen.isGameOver();
        int level = gamePlayScreen.getLevel();

        if (isWon && level == 1) {
            level1Score = gamePlayScreen.getScore();
            startLevel2();
            return;
        }

        int finalScore = isLost ? 0 : gamePlayScreen.getScore();
        int timeRemaining = isLost ? 0 : gamePlayScreen.getSecondsLeft();

        gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
        gameEndScreen.setIsWon(isWon && !isLost);
        gameEndScreen.setFinalScore(level, timeRemaining, finalScore);

        gamePlayScreen = null;
        state = State.END;
    }

    /**
     * Resets the game back to the home screen.
     * Clears the end screen, re-initializes the home screen, and sets the state to HOME.
     */
    private void resetToHome() {
        gameEndScreen = null;
        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
        state = State.HOME;
    }

    /**
     * Retrieves the width of the game screen.
     *
     * @return The width of the screen in pixels.
     */
    public static double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Retrieves the height of the game screen.
     *
     * @return The height of the screen in pixels.
     */
    public static double getScreenHeight() {
        return screenHeight;
    }

    /**
     * The main entry point of the Shadow Donkey Kong game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }
}
