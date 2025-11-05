import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;


/*
 * Abstract base class for both games (Breakout, Galaga)
 * 
 * Provides shared functionality such as score/lives display,
 * game over, and scene settup
 * 
 * @author Colby R, Tyler M, Joe L
 */
public abstract class GameController {

    protected int width;
    protected int height;

    protected int score;
    protected int highScore;
    protected int lives;
    protected boolean gameOver;

    protected Text scoreText;
    protected Text highScoreText;
    protected Text livesText;

   
    private Timeline animation;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    
    
    /*
     * Creates and initializes the root node for the game.
     */
    public Group createRootForAnimation(int windowWidth, int windowHeight) {
        width = windowWidth;
        height = windowHeight;

        Group root = new Group();
        setupUI(root);
        setupGame(root);
        return root;
    }

    /*
     * Sets up the on-screen UI text for score, high score, and lives.
     */
    protected void setupUI(Group root) {
        score = 0;
        highScore = 0;
        lives = 3;

        scoreText = new Text(10, height - 10, "Score: " + score);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font(16));

        highScoreText = new Text(width - 150, height - 10, "High Score: " + highScore);
        highScoreText.setFill(Color.WHITE);
        highScoreText.setFont(Font.font(16));

        livesText = new Text(width / 2.0 - 30, height - 10, "Lives: " + lives);
        livesText.setFill(Color.WHITE);
        livesText.setFont(Font.font(16));

        root.getChildren().addAll(scoreText, highScoreText, livesText);
    }

    /*
     * Update score text.
     */
    protected void updateScoreDisplay() {
        scoreText.setText("Score: " + score);
        highScoreText.setText("High Score: " + highScore);
    }

    /*
     * Update lives text.
     */
    protected void updateLivesDisplay() {
        livesText.setText("Lives: " + lives);
    }

    /*
     * Display end game message.
     */
    protected void endGame(Group root, boolean playerWon) {
        gameOver = true;
        Text endText = new Text(width / 2.0 - 60, height / 2.0,
                playerWon ? "YOU WIN!" : "GAME OVER");
        endText.setFill(Color.YELLOW);
        endText.setFont(Font.font(24));

        Text restartText = new Text(width / 2.0 - 90, height / 2.0 + 30, "Press R to restart");
        restartText.setFill(Color.WHITE);
        restartText.setFont(Font.font(16));

        root.getChildren().addAll(endText, restartText);
    }
    
    public void startAnimation() {
        if (animation != null) return; 
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation.getKeyFrames().add(frame);
        animation.play();
    }
    
    public void stopAnimation() {
        if (animation != null) {
            animation.stop();
            animation = null;
        }
    }

    // Abstract methods subclasses must implement
    protected abstract void setupGame(Group root);
    public abstract void step(double elapsedTime);
    public abstract void restartGame();

    public abstract void handleKeyInput(KeyCode code);
    public abstract void handleKeyRelease(KeyCode code);
    
}
