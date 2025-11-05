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
 */
public class GalagaController extends GameController {

    private Rectangle player;
    private List<Rectangle> enemies;
    private List<Rectangle> bullets;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean shooting = false;

    private static final double PLAYER_SPEED = 300;
    private static final double BULLET_SPEED = 400;
    private static final double ENEMY_SPEED = 15;

    @Override
    protected void setupGame(Group root) {
        player = new Rectangle(width / 2 - 15, height - 60, 30, 20);
        player.setFill(Color.CYAN);
        root.getChildren().add(player);

        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        int rows = 3, cols = 8;
        int spacingX = 60, spacingY = 40;
        int offsetX = 100, offsetY = 60;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle enemy = new Rectangle(offsetX + col * spacingX, offsetY + row * spacingY, 30, 20);
                enemy.setFill(Color.RED);
                enemies.add(enemy);
                root.getChildren().add(enemy);
            }
        }
    }

    @Override
    public void step(double elapsedTime) {
        if (moveLeft) player.setX(Math.max(0, player.getX() - PLAYER_SPEED * elapsedTime));
        if (moveRight) player.setX(Math.min(width - player.getWidth(), player.getX() + PLAYER_SPEED * elapsedTime));

        Group root = (Group) livesText.getParent();

        if (shooting && bullets.size() < 5) {
            Rectangle bullet = new Rectangle(player.getX() + player.getWidth() / 2 - 2, player.getY() - 10, 4, 10);
            bullet.setFill(Color.YELLOW);
            bullets.add(bullet);
            root.getChildren().add(bullet);
        }

        Iterator<Rectangle> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Rectangle b = bulletIter.next();
            b.setY(b.getY() - BULLET_SPEED * elapsedTime);
            if (b.getY() < 0) {
                bulletIter.remove();
                root.getChildren().remove(b);
            } else {
                Iterator<Rectangle> enemyIter = enemies.iterator();
                while (enemyIter.hasNext()) {
                    Rectangle e = enemyIter.next();
                    if (b.getBoundsInParent().intersects(e.getBoundsInParent())) {
                        enemyIter.remove();
                        root.getChildren().remove(e);
                        bulletIter.remove();
                        root.getChildren().remove(b);
                        score += 100;
                        if (score > highScore) highScore = score;
                        updateScoreDisplay();
                        break;
                    }
                }
            }
        }

        for (Rectangle e : enemies) {
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
        }
    }
}
