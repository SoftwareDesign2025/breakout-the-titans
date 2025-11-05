import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.input.KeyCode;

/*
 * Controller for the Galaga-style game.
 * Uses shared functionality from GameController for score, lives, and text.
 * 
 * @author Colby R, Joe L, Tyler M
 */
public class GalagaController extends GameController {

    private Rectangle player;
    private List<Rectangle> bullets;
    private List<Bug> enemies;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean shooting = false;
    private GalagaLevel level;

    private static final double PLAYER_SPEED = 300;
    private static final double BULLET_SPEED = 400;
    private static final double ENEMY_SPEED = 15;
    protected static final int ROWS = 3;
    protected static final int COLUMNS = 8;

    // fire cooldown timer
    private double shootCooldown = 0;
    private static final double SHOOT_DELAY = 0.25; // seconds between shots

    @Override
    protected void setupGame(Group root) {
        player = new Rectangle(width / 2 - 15, height - 60, 30, 20);
        player.setFill(Color.CYAN);
        root.getChildren().add(player);
        
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        level = new GalagaLevel(ROWS, COLUMNS);
        level.createLevel(root);
        enemies = level.getBugs();
    }

    @Override
    public void step(double elapsedTime) {
        if (moveLeft) player.setX(Math.max(0, player.getX() - PLAYER_SPEED * elapsedTime));
        if (moveRight) player.setX(Math.min(width - player.getWidth(), player.getX() + PLAYER_SPEED * elapsedTime));

        Group root = (Group) livesText.getParent();

        // Update shoot cooldown timer
        if (shootCooldown > 0) {
            shootCooldown -= elapsedTime;
        }

        // Fire bullet only when cooldown is 0
        if (shooting && shootCooldown <= 0) {
            Rectangle bullet = new Rectangle(player.getX() + player.getWidth() / 2 - 2, player.getY() - 10, 4, 10);
            bullet.setFill(Color.YELLOW);
            bullets.add(bullet);
            root.getChildren().add(bullet);
            shootCooldown = SHOOT_DELAY; // reset cooldown
        }

        Iterator<Rectangle> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Rectangle b = bulletIter.next();
            b.setY(b.getY() - BULLET_SPEED * elapsedTime);

            // remove bullet if it leaves screen
            if (b.getY() < 0) {
                bulletIter.remove();
                root.getChildren().remove(b);
                continue;
            }

            boolean hitSomething = false;

            // iterate through Bug objects
            Iterator<Bug> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Bug e = enemyIter.next();

                // collision check
                if (b.getBoundsInParent().intersects(e.getView().getBoundsInParent())) {
                    // remove bug from scene and list
                    enemyIter.remove();
                    root.getChildren().remove(e.getView());

                    // remove bullet from scene and list
                    bulletIter.remove();
                    root.getChildren().remove(b);

                    // scoring
                    score += 100;
                    if (score > highScore) highScore = score;
                    updateScoreDisplay();

                    hitSomething = true;
                    break; // stop checking more enemies for this bullet
                }
            }

            // no need for a global break; continue to next bullet safely
            if (hitSomething) continue;
        }

        // move enemies downward
        for (Bug e : enemies) {
            e.setY(e.getY() + ENEMY_SPEED * elapsedTime);
            if (e.getY() + e.getHeight() >= height - 50 && !gameOver) {
                lives = 0;
                updateLivesDisplay();
                endGame(root, false);
                break;
            }
        }

        if (enemies.isEmpty() && !gameOver) {
            endGame(root, true);
        }
    }

    @Override
    public void restartGame() {
        if (!gameOver) return;
        Group root = (Group) livesText.getParent();
        root.getChildren().clear();
        setupUI(root);
        setupGame(root);
        
        gameOver = false;
    }

    // Input control
    public void setMovement(boolean left, boolean pressed) {
        if (left) moveLeft = pressed; else moveRight = pressed;
    }

    public void setShooting(boolean pressed) {
        shooting = pressed;
    }

    @Override
    public void handleKeyInput(KeyCode code) {
        if (code == KeyCode.LEFT) {
            setMovement(true, true);
        } else if (code == KeyCode.RIGHT) {
            setMovement(false, true);
        } else if (code == KeyCode.SPACE) {
            setShooting(true);
        }
    }

    @Override
    public void handleKeyRelease(KeyCode code) {
        if (code == KeyCode.LEFT) {
            setMovement(true, false);
        } else if (code == KeyCode.RIGHT) {
            setMovement(false, false);
        } else if (code == KeyCode.SPACE) {
            setShooting(false);
        } else if (code == KeyCode.R) {
            restartGame(); // allow restart from SPACE when game over (matches prior behavior)
        }
    }
}