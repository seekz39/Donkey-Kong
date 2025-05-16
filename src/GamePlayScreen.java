import bagel.*;
import bagel.Input;
import java.util.Properties;

/**
 * Represents the main gameplay screen where the player controls Mario.
 * This class manages game objects, updates their states, and handles game logic.
 */
public abstract class GamePlayScreen {
    final Properties GAME_PROPS;

    private Image background;   // Background image for the game
    // Frame tracking
    protected int currFrame = 0;  // Tracks the number of frames elapsed
    // Game parameters
    private final int MAX_FRAMES;  // Maximum number of frames before game ends

    // Display text variables
    private final Font STATUS_FONT;
    private final int SCORE_X;
    private final int SCORE_Y;
    private static final String SCORE_MESSAGE = "SCORE ";
    private static final String TIME_MESSAGE = "Time Left ";

    private static final int BARREL_SCORE = 100;
    private static final int TIME_DISPLAY_DIFF_Y = 30;
    private static final int BARREL_CROSS_SCORE = 30;
    private int score = 0;  // Player's score for jumping over barrels
    private boolean isGameOver = false; // Game over flag

    /**
     * Returns the player's current score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

//    public int getScore() {
//        if (isGameOver) {
//            return 0;
//        }
//        return score;
//    }

    public void addScore(int points) {
        score += points;
    }

    /**
     * Calculates the remaining time left in seconds.
     *
     * @return The number of seconds remaining before the game ends.
     */
    public int getSecondsLeft() {
        return (MAX_FRAMES - currFrame) / 60;
    }

    /**
     * Constructs the gameplay screen, loading resources and initializing game objects.
     *
     * @param gameProps  Properties file containing game settings.
     */
    public GamePlayScreen(Properties gameProps) {
        this.GAME_PROPS = gameProps;

        // Load game parameters
        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        this.STATUS_FONT = new Font(
                gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gamePlay.score.fontSize"))
        );
        this.SCORE_X = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        this.SCORE_Y = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));
        this.background = new Image("res/background.png");

        // Initialize game objects
        initializeGameObjects();
    }

    /**
     * Initializes game objects such as Mario, Donkey Kong, barrels, ladders, platforms, and the hammer.
     */
    public abstract void initializeGameObjects();

    /**
     * Updates game state each frame.
     *
     * @param input The current player input.
     * @return {@code true} if the game ends, {@code false} otherwise.
     */

    public abstract boolean update(Input input);

    Image getBackground() {
        return background;
    }

     /**
     * Displays the player's score & time left on the screen.
     */
    public void displayInfo() {
        STATUS_FONT.drawString(SCORE_MESSAGE + score, SCORE_X, SCORE_Y);

        // Time left in seconds
        int secondsLeft = (MAX_FRAMES - currFrame) / 60;
        int TIME_X = SCORE_X;
        int TIME_Y = SCORE_Y + TIME_DISPLAY_DIFF_Y;
        STATUS_FONT.drawString(TIME_MESSAGE + secondsLeft, TIME_X, TIME_Y);
    }

    /**
     * Checks whether the level is completed by determining if Mario has reached Donkey Kong
     * while holding a hammer. This serves as the game's winning condition.
     *
     * @return {@code true} if Mario reaches Donkey Kong while holding a hammer,
     *         indicating the level is completed; {@code false} otherwise.
     */
     public abstract boolean isLevelCompleted();

    /**
     * Checks if the game has reached its time limit by comparing the current frame count
     * against the maximum allowed frames. If the limit is reached, the game may trigger
     * a timeout condition.
     *
     * @return {@code true} if the current frame count has reached or exceeded
     *         the maximum allowed frames, indicating the time limit has been reached;
     *         {@code false} otherwise.
     */
    public boolean checkingGameTime() {
        return currFrame >= MAX_FRAMES;
    }
}
