import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

		int rows = 5;
		int cols = 7;
		int spacing = 5;
		int brickWidth = 50;
		int brickHeight = 20;
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
		
		return root;
	}

	public void step (double elapsedTime) {
		// update "actors" attributes
		gameBall.move(elapsedTime);
		gameBall.wallBounce(width, height);
		gameBall.paddleBounce(gamePaddle.getView());

		// check for collisions
		// with shapes, can check precisely
		// Shape intersection = Shape.intersect(myMover, myGrower);
//		if (intersection.getBoundsInLocal().getWidth() != -1) {
//			
//
//		}
		
		// with images can only check bounding box
//		boolean hit = false;
//		for (Bouncer b : myBouncers) {
//			if (myGrower.getBoundsInParent().intersects(b.getView().getBoundsInParent())) {
//
//				hit = true;
//			}
//		}
//		if (hit) {
//		    myGrower.setFill(HIGHLIGHT);
//		}

		Iterator<Brick> iter = bricks.iterator();
		while (iter.hasNext()) {
			Brick brick = iter.next();
			if (brick.isHitByBall(gameBall.getBall())) {
				gameBall.reverseY();
				brick.getView().setVisible(false);
				iter.remove();
				break;
			}
		}
	}
	
	public void moverMovesHorizontally(boolean goLeft) {
	    if (goLeft) {
	        // myMover.setX(myMover.getX() - MOVER_SPEED);
	        gamePaddle.moveLeft();
	    } else {
	        // myMover.setX(myMover.getX() + MOVER_SPEED);
	        gamePaddle.moveRight();
	    }
	}


	public void handleMouseInput (double x, double y) {
//		if (myGrower.contains(x, y)) {
//
//		}
		gamePaddle.getView().setX(x - gamePaddle.getView().getWidth() / 2);
	}
}