import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;


/**
 * Represents the player-controlled character, Mario.
 * Mario can move, jump, climb ladders, pick up a hammer, and interact with platforms.
 */
public class Mario extends GameEntity{
    private double velocityY = 0; // Vertical velocity
    private boolean isJumping = false; // Whether Mario is currently jumping
    private boolean hasHammer = false; // Whether Mario has collected a hammer
    private boolean hasBlaster = false;// Whether Mario has collected a blaster
    private final int totalBullets = 5;
    private int bulletsCount = 0;
    private boolean isFacingRight = true; // Mario's facing direction

    // Mario images for different states
    private Image marioImage;
    private final Image MARIO_LEFT_IMAGE;
    private final Image MARIO_HAMMER_LEFT_IMAGE;
    private final Image MARIO_HAMMER_RIGHT_IMAGE;
    private final Image MARIO_BLASTER_LEFT_IMAGE;
    private final Image MARIO_BLASTER_RIGHT_IMAGE;

    // Movement physics constants
    private static final double JUMP_STRENGTH = -5;
    private static final double MOVE_SPEED = 3.5;
    private static final double CLIMB_SPEED = 2;
    private static final double MARIO_GRAVITY = 0.2;
    private static double height;
    private static double width;
    private static final Image MARIO_RIGHT_IMAGE = new Image("res/mario_right.png");

    /**
     * Constructs a Mario character at the specified starting position.
     *
     * @param x Initial x-coordinate.
     * @param y Initial y-coordinate.
     */
    public Mario(double x, double y) {
        super(MARIO_RIGHT_IMAGE, x, y);

        // Load images for left and right-facing Mario
//        this.MARIO_RIGHT_IMAGE = new Image("res/mario_right.png");
        this.MARIO_LEFT_IMAGE = new Image("res/mario_left.png");
        this.MARIO_HAMMER_RIGHT_IMAGE = new Image("res/mario_hammer_right.png");
        this.MARIO_HAMMER_LEFT_IMAGE = new Image("res/mario_hammer_left.png");
        this.MARIO_BLASTER_RIGHT_IMAGE = new Image("res/mario_blaster_right.png");
        this.MARIO_BLASTER_LEFT_IMAGE = new Image("res/mario_blaster_left.png");

        // Default Mario starts facing right
        this.marioImage = MARIO_HAMMER_RIGHT_IMAGE;
        width = marioImage.getWidth();
        height = marioImage.getHeight();
    }

    /**
     * Sets whether Mario has picked up the hammer.
     *
     * @param status {@code true} if Mario has the hammer, {@code false} otherwise.
     */
    public void setHasHammer(boolean status) {
        this.hasHammer = status;
    }

    public void setHasBlaster(boolean status) {
        this.hasBlaster = status;
    }

    /**
     * Checks if Mario has the hammer.
     *
     * @return {@code true} if Mario has the hammer, {@code false} otherwise.
     */
    public boolean holdHammer() {
        return this.hasHammer;
    }

    public boolean holdBlaster() {
        return this.hasBlaster;
    }


    /**
     * Updates Mario's movement, jumping, ladder climbing, hammer collection, and interactions.
     * This method is called every frame to process player input and update Mario's state.
     *
     * @param input     The player's input (keyboard/mouse).
     * @param ladders   The array of ladders in the game that Mario can climb.
     * @param platforms The array of platforms in the game that Mario can walk on.
     */

    public void updateLevel1(Input input, ArrayList<Ladder> ladders, Platform[] platforms) {
        handleHorizontalMovement(input); // 1) Horizontal movement
//        updateLevel1Sprite(hammer); // 2) Update Mario’s current sprite (hammer or not, facing left or right)
//        handleHammerCollection(hammer); // 3) If you just picked up the hammer:
        updateLevel1Sprite(); // 4) Now replace sprite (since either isFacingRight or hasHammer could have changed)

        // 5) Ladder logic – check if on a ladder
        boolean isOnLadder;
        isOnLadder = handleLadders(input, ladders);

        // 6) Jump logic: if on platform (we'll detect after we move) but let's queue jump if needed
        boolean wantsToJump = input.wasPressed(Keys.SPACE);

        // 7) If not on ladder, apply gravity, move Mario
        if (!isOnLadder) {
            velocityY += MARIO_GRAVITY;
            velocityY = Math.min(TERMINAL_VELOCITY, velocityY);
        }

        // 8) Actually move Mario vertically after gravity
        setY(getY() + velocityY);

        // 9) Check for platform collision AFTER Mario moves
        boolean onPlatform;
        onPlatform = handlePlatforms(platforms);

        // 10) If we are on the platform, allow jumping; Prevent Mario from falling below the ground
        handleJumping(onPlatform, wantsToJump);

        // 11) Enforce horizontal screen bounds
        enforceBoundaries();

        // 12) Draw Mario
        draw();
    }

    public void updateLevel2(Input input, ArrayList<Ladder> ladders, Platform[] platforms, ArrayList<Bullet> bullets) {
        handleHorizontalMovement(input); // 1) Horizontal movement
        updateLevel2Sprite();
        handleShoot(input, bullets);

        for (Bullet bullet : bullets) {
            bullet.update();
            bullet.draw();
        }

        // 4) Now replace sprite (since either isFacingRight or hasHammer could have changed)

        // 5) Ladder logic – check if on a ladder
        boolean isOnLadder;
        isOnLadder = handleLadders(input, ladders);

        // 6) Jump logic: if on platform (we'll detect after we move) but let's queue jump if needed
        boolean wantsToJump = input.wasPressed(Keys.SPACE);

        // 7) If not on ladder, apply gravity, move Mario
        if (!isOnLadder) {
            velocityY += MARIO_GRAVITY;
            velocityY = Math.min(TERMINAL_VELOCITY, velocityY);
        }

        // 8) Actually move Mario vertically after gravity
        setY(getY() + velocityY);

        // 9) Check for platform collision AFTER Mario moves
        boolean onPlatform;
        onPlatform = handlePlatforms(platforms);


        // 10) If we are on the platform, allow jumping; Prevent Mario from falling below the ground
        handleJumping(onPlatform, wantsToJump);


        // 11) Enforce horizontal screen bounds
        enforceBoundaries();

        // 12) Draw Mario
        draw();
    }

    /**
     * Handles Mario's interaction with platforms to determine if he is standing on one.
     * Mario will only snap to a platform if he is moving downward (velocityY >= 0),
     * preventing his jump from being interrupted in mid-air.
     *
     * @param platforms An array of {@link Platform} objects representing the platforms in the game.
     * @return {@code true} if Mario is standing on a platform, {@code false} otherwise.
     */
    private boolean handlePlatforms(Platform[] platforms) {
        boolean onPlatform = false;

        // We'll only snap Mario to a platform if he's moving downward (velocityY >= 0)
        // so we don't kill his jump in mid-air.
        if (velocityY >= 0) {
            for (Platform platform : platforms) {
                Rectangle marioBounds    = getBoundingBox();
                Rectangle platformBounds = platform.getBoundingBox();

                if (marioBounds.intersects(platformBounds)) {
                    double marioBottom = marioBounds.bottom();
                    double platformTop = platformBounds.top();

                    // If Mario's bottom is at or above the platform's top
                    // and not far below it (a small threshold based on velocity)
                    if (marioBottom <= platformTop + velocityY) {
                        // Snap Mario so his bottom = the platform top
                        double newY = platformTop - (marioImage.getHeight() / 2);
                        setY(newY);
                        velocityY = 0;
                        isJumping = false;
                        onPlatform = true;
                        break; // We found a platform collision
                    }
                }
            }
        }
        return onPlatform;
    }

    /**
     * Handles Mario's interaction with ladders, allowing him to climb up or down
     * based on user input and position relative to the ladder.
     *
     * Mario can only climb if he is within the horizontal boundaries of the ladder.
     * He stops sliding unintentionally when not pressing movement keys.
     *
     * @param input   The {@link Input} object that checks for user key presses.
     * @param ladders An array of {@link Ladder} objects representing ladders in the game.
     * @return {@code true} if Mario is on a ladder, {@code false} otherwise.
     */
    private boolean handleLadders(Input input, ArrayList<Ladder> ladders) {
        boolean isOnLadder = false;
        for (Ladder ladder : ladders) {
            double ladderLeft  = ladder.getBoundingBox().left();
            double ladderRight = ladder.getBoundingBox().right();
            double marioRight  = getX() + (marioImage.getWidth() / 2);
            double marioBottom = getY() + (marioImage.getHeight() / 2);
            double ladderTop    = ladder.getBoundingBox().top();
            double ladderBottom = ladder.getBoundingBox().bottom();

            if (collidesWith(ladder)) {
                // Check horizontal overlap so Mario is truly on the ladder
                if (marioRight - marioImage.getWidth() / 2 > ladderLeft && marioRight - marioImage.getWidth() / 2 < ladderRight) {
                    isOnLadder = true;

                    // Stop Mario from sliding up when not moving**
                    if (!input.isDown(Keys.UP) && !input.isDown(Keys.DOWN)) {
                        velocityY = 0;  // Prevent sliding inertia effect
                    }

                    // ----------- Climb UP -----------
                    if (input.isDown(Keys.UP)) {
                        setY(getY() - CLIMB_SPEED);
                        velocityY = 0;
                    }

                    // ----------- Climb DOWN -----------
                    if (input.isDown(Keys.DOWN)) {
                        double nextY = getY() + CLIMB_SPEED;
                        double nextBottom = nextY + (marioImage.getHeight() / 2);

                        if (marioBottom > ladderTop && nextBottom <= ladderBottom) {
                            setY(nextY);
                            velocityY = 0;
                        } else if (marioBottom == ladderBottom) {
                            velocityY = 0;
                        } else if (ladderBottom - marioBottom < CLIMB_SPEED) {
                            double newY = getY() + (ladderBottom - marioBottom);
                            setY(newY);
                            velocityY = 0;
                        }
                    }
                }

            } else if (marioBottom == ladderTop && input.isDown(Keys.DOWN) && (marioRight - marioImage.getWidth() / 2 > ladderLeft && marioRight - marioImage.getWidth() / 2  < ladderRight)) {
                double nextY = getY() + CLIMB_SPEED;
                setY(nextY);
                velocityY = 0; // ignore gravity
            } else if (marioBottom == ladderBottom && input.isDown(Keys.DOWN) && (marioRight - marioImage.getWidth() / 2 > ladderLeft && marioRight - marioImage.getWidth() / 2  < ladderRight)) {
                velocityY = 0; // ignore gravity
            }
        }
        return isOnLadder;
    }

    /** Handles horizontal movement based on player input. */
    private void handleHorizontalMovement(Input input) {
        if (input.isDown(Keys.LEFT)) {
            setX(getX() - MOVE_SPEED);
            isFacingRight = false;
        } else if (input.isDown(Keys.RIGHT)) {
            setX(getX() + MOVE_SPEED);
            isFacingRight = true;
        }
    }

//    /** Handles collecting the hammer if Mario is in contact with it. */
//    private void handleHammerCollection(Hammer hammer) {
//        if (!hammer.isCollected() && collidesWith(hammer)) {
//            setHasHammer(true);
//            hammer.collect();
//            System.out.println("Hammer collected!");
//            hasHammer = true;
//            hasBlaster = false;
//        }
//    }

//    private void handleBlasterCollection(Blaster[] blasters) {
//        for (Blaster blaster : blasters) {
//            if (!blaster.isCollected() && collidesWith(blaster)) {
//                blaster.collect();
//                setHasBlaster(true);
//                hasBlaster = true;
//                hasHammer = false;
//                bulletsCount += totalBullets;
//                System.out.println("Blaster collected!");
//            }
//        }
//    }

    /** Handles jumping if Mario is on a platform and jump is requested. */
    private void handleJumping(boolean onPlatform, boolean wantsToJump) {
        if (onPlatform && wantsToJump) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
            System.out.println("Jumping!");
        }
        double bottomOfMario = getY() + (marioImage.getHeight() / 2);
        if (bottomOfMario > ShadowDonkeyKong.getScreenHeight()) {
            setY(ShadowDonkeyKong.getScreenHeight()
                    - (marioImage.getHeight() / 2.0));
            velocityY = 0;
            isJumping = false;
        }
    }

    /**
     * Enforces screen boundaries to prevent Mario from moving out of bounds.
     * Ensures Mario stays within the left, right, and bottom limits of the game window.
     */
    private void enforceBoundaries() {
        // Calculate half the width of the Mario image (used for centering and boundary checks)
        double halfW = marioImage.getWidth() / 2;

        // Prevent Mario from moving beyond the left edge of the screen
        if (getX() < halfW) {
            setX(halfW);
        }

        // Prevent Mario from moving beyond the right edge of the screen
        double maxX = ShadowDonkeyKong.getScreenWidth() - halfW;
        if (getX() > maxX) {
            setX(maxX);
        }

        // Calculate Mario's bottom edge position
        double bottomOfMario = getY() + (marioImage.getHeight() / 2);

        // Prevent Mario from falling below the bottom of the screen
        if (bottomOfMario > ShadowDonkeyKong.getScreenHeight()) {
            // Reposition Mario to stand on the bottom edge
            setY(ShadowDonkeyKong.getScreenHeight() - (marioImage.getHeight() / 2));

            // Stop vertical movement and reset jumping state
            velocityY = 0;
            isJumping = false;
        }
    }


    /**
     * Switch Mario's sprite (left/right, or hammer/no-hammer).
     * Adjust Mario's 'y' so that the bottom edge stays consistent.
     */
    private void updateLevel1Sprite() {
        // 1) Remember the old image and its bottom
        Image oldImage = marioImage;
        double oldHeight = oldImage.getHeight();
        double oldBottom = getY() + (oldHeight / 2);

        // 2) Assign the new image based on facing & hammer
        //    (Whatever logic you currently use in update())
        if (hasHammer) {
            marioImage = isFacingRight ? MARIO_HAMMER_RIGHT_IMAGE : MARIO_HAMMER_LEFT_IMAGE;
        } else {
            marioImage = isFacingRight ? MARIO_RIGHT_IMAGE : MARIO_LEFT_IMAGE;
        }

        // 3) Now recalc Mario’s bottom with the new image
        double newHeight = marioImage.getHeight();
        double newBottom = getY() + (newHeight / 2);

        // 4) Shift 'y' so the bottom edge is the same as before
        //    (If new sprite is taller, we move Mario up so he doesn't sink into platforms)
        setY(getY() - (newBottom - oldBottom));

        // 5) Update the recorded width/height to match the new image
        width  = marioImage.getWidth();
        height = newHeight;
    }

    private void updateLevel2Sprite() {

        Image oldImage = marioImage;
        double oldBottom = getY() + oldImage.getHeight() / 2;

        if (hasBlaster) {
            marioImage = isFacingRight ? MARIO_BLASTER_RIGHT_IMAGE : MARIO_BLASTER_LEFT_IMAGE;
        } else if (hasHammer) {
            marioImage = isFacingRight ? MARIO_HAMMER_RIGHT_IMAGE : MARIO_HAMMER_LEFT_IMAGE;
        } else {
            marioImage = isFacingRight ? MARIO_RIGHT_IMAGE : MARIO_LEFT_IMAGE;
        }

        double newHeight = marioImage.getHeight();
        double newBottom = getY() + newHeight / 2;

        setY(getY() - (newBottom - oldBottom));

        width = marioImage.getWidth();
        height = newHeight;
    }


    /**
     * Draws Mario on the screen.
     */
    public void draw() {
        this.setImage(marioImage);
        marioImage.draw(getX(), getY());
//    drawBoundingBox(); // Uncomment for debugging
    }

    public int getBulletsCount() {
        return bulletsCount;
    }


    public boolean handleShoot(Input input, ArrayList<Bullet> bullets) {
        if (input.wasPressed(Keys.S) && hasBlaster && bulletsCount > 0) {
            boolean faceRight = isFacingRight;
            Bullet bullet = new Bullet(getX(), getY(), faceRight);
            bullets.add(bullet);
            bulletsCount--;

            if (bulletsCount == 0) {
                hasBlaster = false;
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if Mario successfully jumps over a barrel.
     *
     * @param barrel The barrel object to check.
     * @return {@code true} if Mario successfully jumps over the barrel, {@code false} otherwise.
     */
    public boolean jumpOver(Barrel barrel) {
        return !barrel.isDestroyed() && isJumping
                && Math.abs(getX() - barrel.getX()) <= 1
                && (getY() < barrel.getY())
                && ((getY() + height / 2) >= (barrel.getY() + barrel.getImage().getHeight() / 2
                - (JUMP_STRENGTH * JUMP_STRENGTH) / (2 * MARIO_GRAVITY) - height / 2));
    }

    @Override
    public void changeState(GameEntity other) {
        if (other instanceof Hammer hammer && !hammer.isCollected()) {
            setHasHammer(true);
            hammer.collect();
            hasHammer = true;
            hasBlaster = false;
            System.out.println("Hammer collected!");
        }

        else if (other instanceof Blaster blaster && !blaster.isCollected()) {
            setHasBlaster(true);
            blaster.collect();
            hasBlaster = true;
            hasHammer = false;
            bulletsCount += totalBullets;
            System.out.println("Blaster collected!");
        }
    }

}
