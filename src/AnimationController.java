import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * 
 * @author Shannon Duvall
 * 
 * This program animates two squares.  The top is the "mover" and 
 * the bottom is the "grower".
 * 
 * modified to be starting point of breakout lab
 */

public class AnimationController {

	private int width;
	private int height;

	private Ball gameBall;
	private Rectangle paddle;

	private Paddle gamePaddle;
	private List<Brick> bricks;
	
	private int score;
	private int highScore;
	private Text scoreText;
	private Text highScoreText;
	
	private boolean moveLeft = false;
	private boolean moveRight = false;
	
	// for live and level control
	private int lives;
	private Text livesText;
	private boolean gameOver = false;
	
	private List<Ball> extraBalls;       
	private List<PowerUp> powerUps;     
	private Paddle extraPaddle;          
	private double extraPaddleTimer = 0; 
	
	private String keyBuffer = ""; // stores recent keys typed
	private final int MAX_KEY_BUFFER = 10; // max length to avoid unlimited growth


	public Group createRootForAnimation(int windowWidth, int windowHeight) {
		width = windowWidth;
		height = windowHeight;

		// create one top level collection to organize the things in the scene
		Group root = new Group();

		// make some shapes and set their properties
		gameBall = new Ball(width/2, height/2);
		paddle = new Rectangle(100,100);

		gamePaddle = new Paddle(width, height);
		root.getChildren().add(gameBall.getBall());
		root.getChildren().add(gamePaddle.getView());

		bricks = new ArrayList<>();
		extraBalls = new ArrayList<>();
		powerUps = new ArrayList<>();

		int rows = 5;
		int cols = 12;
		int spacing = 3;
		int brickWidth = 60;
		int brickHeight = 30;
		int offsetX = 20;
		int offsetY = 40;

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = offsetX + col * (brickWidth + spacing);
				int y = offsetY + row * (brickHeight + spacing);
				Color color = Color.hsb((row * 60) % 360, 0.8, 0.9);
				Brick brick = new Brick(x, y, 100, color);
				bricks.add(brick);
				root.getChildren().add(brick.getView());
			}
		}

//		try {
//			// for adding objects from image file
//		}
//		catch (FileNotFoundException e) {}
		
		score = 0;
		highScore = 0;
		
		lives = 3; // starting number of lives
		livesText = new Text(width / 2.0 - 30, height - 10, "Lives: " + lives);
		livesText.setFill(Color.WHITE);
		livesText.setFont(Font.font(16));

		root.getChildren().add(livesText);

		scoreText = new Text(10, height - 10, "Score: " + score);
		scoreText.setFill(Color.WHITE);
		scoreText.setFont(Font.font(16));

		highScoreText = new Text(width - 150, height - 10, "High Score: " + highScore);
		highScoreText.setFill(Color.WHITE);
		highScoreText.setFont(Font.font(16));

		root.getChildren().addAll(scoreText, highScoreText);
		
		return root;
	}
	
	private void updateScoreDisplay() {
	    scoreText.setText("Score: " + score);
	    highScoreText.setText("High Score: " + highScore);
	}
	
	private void updateLivesDisplay() {
	    livesText.setText("Lives: " + lives);
	}

	private void endGame(boolean playerWon) {
	    gameOver = true;
	    Text endText = new Text(width / 2.0 - 60, height / 2.0, 
	            playerWon ? "YOU WIN!" : "GAME OVER");
	    endText.setFill(Color.YELLOW);
	    endText.setFont(Font.font(24));

	    Text restartText = new Text(width / 2.0 - 90, height / 2.0 + 30, "Press SPACE to restart");
	    restartText.setFill(Color.WHITE);
	    restartText.setFont(Font.font(16));

	    Group root = (Group) livesText.getParent();
	    root.getChildren().addAll(endText, restartText);
	}

	public void step (double elapsedTime) {
		// smooth paddles
		if (moveLeft)  gamePaddle.moveLeft(elapsedTime);
		if (moveRight) gamePaddle.moveRight(elapsedTime);

		if (extraPaddle != null) {
		    if (moveLeft)  extraPaddle.moveLeft(elapsedTime);
		    if (moveRight) extraPaddle.moveRight(elapsedTime);
		}
		// update "actors" attributes
		List<Ball> allBalls = new ArrayList<>();
		allBalls.add(gameBall);
		allBalls.addAll(extraBalls);

		Group root = (Group) livesText.getParent();

		Iterator<Ball> ballIter = allBalls.iterator();
		while (ballIter.hasNext()) {
		    Ball b = ballIter.next();
		    
		    b.move(elapsedTime);
		    b.wallBounce(width, height);
		    
		    // Bounce off main and extra paddle
		    b.objectBounce(gamePaddle.getView());
		    if (extraPaddle != null) {
		        b.objectBounce(extraPaddle.getView());
		    }
		    
		 // Check collision with bricks
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

		            // Randomly spawn a power-up
		            if (Math.random() < 0.2) { // 20% chance
		                PowerUp.Type type = Math.random() < 0.5 ? PowerUp.Type.MULTI_BALL : PowerUp.Type.MULTI_PADDLE;
		                PowerUp pu = new PowerUp((int)brick.getView().getX(), (int)brick.getView().getY(), type);
		                powerUps.add(pu);
		                root.getChildren().add(pu.getView());
		            }

		            break; // only one brick collision per ball per step
		        }
		    }


		    // Remove ball if it falls off bottom (for extra balls)
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

		// handle extra paddle timer
		if (extraPaddle != null) {
		    extraPaddleTimer -= elapsedTime;
		    if (extraPaddleTimer <= 0) {
		        root.getChildren().remove(extraPaddle.getView());
		        extraPaddle = null;
		    }
		}

		
		// if ball fell off bottom, lose a life and reset
		if (gameBall.getBall().getCenterY() + Ball.BALL_RADIUS >= height && !gameOver) {
		    lives--;
		    updateLivesDisplay();

		    if (lives <= 0) {
		        endGame(false);
		    } else {
		        // reset ball to center
		        gameBall.resetBall(width / 2, height / 2);
		    }
		}
		
	}
	
	public void restartGame() {
	    // restart if game is over
	    if (!gameOver) return;

	    // reset
	    gameOver = false;
	    lives = 3;
	    score = 0;
	    updateScoreDisplay();
	    updateLivesDisplay();

	    // Clear any text
	    Group root = (Group) livesText.getParent();
	    root.getChildren().removeIf(node -> node instanceof Text && node != scoreText && node != highScoreText && node != livesText);
	    
	    // remove rectangles of bricks
	    root.getChildren().removeIf(node -> node instanceof Rectangle && node != gamePaddle.getView());
	    
	    // restarts bricks
	    bricks.clear();
	    int rows = 5;
	    int cols = 10;
	    int spacing = 5;
	    int brickWidth = 60;
	    int brickHeight = 20;
	    int offsetX = 30;
	    int offsetY = 40;
	    for (int row = 0; row < rows; row++) {
	        for (int col = 0; col < cols; col++) {
	            int x = offsetX + col * (brickWidth + spacing);
	            int y = offsetY + row * (brickHeight + spacing);
	            Color color = Color.hsb((row * 60) % 360, 0.8, 0.9);
	            Brick brick = new Brick(x, y, 100, color);
	            bricks.add(brick);
	            root.getChildren().add(brick.getView());
	        }
	    }

	    // reset ball & paddle
	    gameBall.resetBall(width / 2, height / 2);
	    gamePaddle.getView().setX((width - 80) / 2.0); // recenter paddle
	}
	
	public void moverMovesHorizontally(boolean goLeft, boolean isPressed) {
	    if (goLeft) {
	        moveLeft = isPressed;
	    } else {
	        moveRight = isPressed;
	    }
	}


	public void handleMouseInput (double x, double y) {
//		if (myGrower.contains(x, y)) {
//
//		}
		gamePaddle.getView().setX(x - gamePaddle.getView().getWidth() / 2);
	}
	
	private void activatePowerUp(PowerUp pu, Group root) {
		if (pu.getType() == PowerUp.Type.MULTI_BALL) {
		    for (int i = 0; i < 2; i++) {
		        Ball newBall = new Ball(
		            (int) gameBall.getBall().getCenterX(),
		            (int) gameBall.getBall().getCenterY()
		        );

		        // give the new balls a slightly different random direction and same speed
		        double angle = Math.random() * 2 * Math.PI;
		        newBall.setVelocity(Ball.BALL_SPEED * Math.cos(angle), Ball.BALL_SPEED * Math.sin(angle));

		        extraBalls.add(newBall);
		        root.getChildren().add(newBall.getBall());
		    }
		}
		else if (pu.getType() == PowerUp.Type.MULTI_PADDLE) {
	        if (extraPaddle == null) {
	            extraPaddle = new Paddle(width/2, (height));
	            root.getChildren().add(extraPaddle.getView());
	            extraPaddleTimer = 10; // lasts 10 seconds
	        } else {
	            extraPaddleTimer = 10; // refresh timer if re-collected
	        }
	    }
	}
	
	
}