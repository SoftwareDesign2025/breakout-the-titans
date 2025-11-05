import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import javafx.scene.input.KeyCode;


/*
 * Controller for the Breakout-style game.
 * Uses shared functionality from GameController for score, lives, and text.
 * 
 * @author Colby R, Joe L, Tyler M
 */

public class AnimationController extends GameController {

    private Ball gameBall;
    private Paddle gamePaddle;
    private List<Brick> bricks;
    private BreakoutLevel level;

    private boolean moveLeft = false;
    private boolean moveRight = false;

    private List<Ball> extraBalls;       
    private List<PowerUp> powerUps;     
    private Paddle extraPaddle;          
    private double extraPaddleTimer = 0; 
    
    protected static final int ROWS = 5;
    protected static final int COLUMNS = 10;
    
    
    // creates game and adds to root
    protected void setupGame(Group root) {
        gameBall = new Ball(width / 2, height / 2);
        gamePaddle = new Paddle(width, height);
        root.getChildren().addAll(gameBall.getBall(), gamePaddle.getView());

        bricks = new ArrayList<>();
        extraBalls = new ArrayList<>();
        powerUps = new ArrayList<>();

        level = new BreakoutLevel(ROWS, COLUMNS);
        level.createLevel(root);
        bricks = level.getBricks();
    }

    // perform game actions each frame
    @Override
    public void step(double elapsedTime) {
        if (moveLeft) gamePaddle.moveLeft(elapsedTime);
        if (moveRight) gamePaddle.moveRight(elapsedTime);
        if (extraPaddle != null) {
            if (moveLeft) extraPaddle.moveLeft(elapsedTime);
            if (moveRight) extraPaddle.moveRight(elapsedTime);
        }

        List<Ball> allBalls = new ArrayList<>();
        allBalls.add(gameBall);
        allBalls.addAll(extraBalls);

        Group root = (Group) livesText.getParent();
        Iterator<Ball> ballIter = allBalls.iterator();

        while (ballIter.hasNext()) {
            Ball b = ballIter.next();
            b.move(elapsedTime);
            b.wallBounce(width, height);
            b.objectBounce(gamePaddle.getView());
            if (extraPaddle != null) b.objectBounce(extraPaddle.getView());

            Iterator<Brick> brickIter = bricks.iterator();
            while (brickIter.hasNext()) {
                Brick brick = brickIter.next();
                int earnedPoints = brick.handleHit(b.getBall());
                if (earnedPoints > 0) {
                    brickIter.remove();
                    root.getChildren().remove(brick.getView());
                    b.reverseY();
                    score += earnedPoints;
                    if (score > highScore) highScore = score;
                    updateScoreDisplay();
                    if (Math.random() < 0.25) {
                        PowerUp.Type type = Math.random() < 0.5 ? PowerUp.Type.MULTI_BALL : PowerUp.Type.MULTI_PADDLE;
                        PowerUp pu = new PowerUp((int) brick.getView().getX(), (int) brick.getView().getY(), type);
                        powerUps.add(pu);
                        root.getChildren().add(pu.getView());
                    }
                    break;
                }
            }

            if (b.getBall().getCenterY() + Ball.BALL_RADIUS >= height && b != gameBall) {
                root.getChildren().remove(b.getBall());
                ballIter.remove();
            }
        }

        Iterator<PowerUp> pIter = powerUps.iterator();
        while (pIter.hasNext()) {
            PowerUp pu = pIter.next();
            pu.move(elapsedTime);
            if (pu.intersects(gamePaddle.getView())) {
                activatePowerUp(pu, root);
                pIter.remove();
                root.getChildren().remove(pu.getView());
            } else if (pu.getView().getCenterY() > height) {
                pIter.remove();
                root.getChildren().remove(pu.getView());
            }
        }

        if (extraPaddle != null) {
            extraPaddleTimer -= elapsedTime;
            if (extraPaddleTimer <= 0) {
                root.getChildren().remove(extraPaddle.getView());
                extraPaddle = null;
            }
        }

        if (gameBall.getBall().getCenterY() + Ball.BALL_RADIUS >= height && !gameOver) {
            lives--;
            updateLivesDisplay();
            if (lives <= 0) {
                endGame(root, false);
            } else {
                gameBall.resetBall(width / 2, height / 2);
            }
        }
    }

    // resets game with a new, different level
    @Override
    public void restartGame() {
        if (!gameOver) return;

        gameOver = false;
        lives = 3;
        score = 0;
        updateScoreDisplay();
        updateLivesDisplay();

        Group root = (Group) livesText.getParent();
        root.getChildren().removeIf(node -> node instanceof Text && node != scoreText && node != highScoreText && node != livesText);
        root.getChildren().removeIf(node -> node instanceof Rectangle && node != gamePaddle.getView());

        bricks.clear();
        level = new BreakoutLevel(ROWS, COLUMNS);
        level.createLevel(root);
        bricks = level.getBricks();

        gameBall.resetBall(width / 2, height / 2);
        gamePaddle.getView().setX((width - 80) / 2.0);
    }

    public void moverMovesHorizontally(boolean goLeft, boolean isPressed) {
        if (goLeft) moveLeft = isPressed;
        else moveRight = isPressed;
    }

    public void handleMouseInput(double x, double y) {
        gamePaddle.getView().setX(x - gamePaddle.getView().getWidth() / 2);
    }

    private void activatePowerUp(PowerUp pu, Group root) {
        if (pu.getType() == PowerUp.Type.MULTI_BALL) {
            for (int i = 0; i < 2; i++) {
                Ball newBall = new Ball(
                    (int) gameBall.getBall().getCenterX(),
                    (int) gameBall.getBall().getCenterY()
                );
                double angle = Math.random() * 2 * Math.PI;
                newBall.setVelocity(Ball.BALL_SPEED * Math.cos(angle), Ball.BALL_SPEED * Math.sin(angle));
                extraBalls.add(newBall);
                root.getChildren().add(newBall.getBall());
            }
        } else if (pu.getType() == PowerUp.Type.MULTI_PADDLE) {
            if (extraPaddle == null) {
                extraPaddle = new Paddle(width / 2, height);
                root.getChildren().add(extraPaddle.getView());
                extraPaddleTimer = 10;
            } else {
                extraPaddleTimer = 10;
            }
        }
    }
    
    @Override
    public void handleKeyInput(KeyCode code) {
        if (code == KeyCode.LEFT) {
            moverMovesHorizontally(true, true);
        } else if (code == KeyCode.RIGHT) {
            moverMovesHorizontally(false, true);
        } else if (code == KeyCode.R) {
            restartGame(); // allow restart from SPACE when game over (matches prior behavior)
        }
    }
    
    public void handleKeyRelease(KeyCode code) {
        if (code == KeyCode.LEFT) {
            moverMovesHorizontally(true, false);
        } else if (code == KeyCode.RIGHT) {
            moverMovesHorizontally(false, false);
        }
    }
}
