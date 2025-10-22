import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

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
	private Rectangle paddle; // existing
	private Paddle gamePaddle; // ADDED

	public Group createRootForAnimation(int windowWidth, int windowHeight) {
		width = windowWidth;
		height = windowHeight;

		// create one top level collection to organize the things in the scene
		Group root = new Group();

		// make some shapes and set their properties
		gameBall = new Ball(width/2, height/2);
		paddle = new Rectangle(100,100); // keep existing teammate code

		// ADDED: create Paddle class instance (more functional)
		gamePaddle = new Paddle(width, height);

		// add objects to scene
		root.getChildren().add(gameBall.getBall());
		root.getChildren().add(gamePaddle.getView()); // ADDED

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

		// ADDED: check for ball–paddle collision
		gameBall.paddleBounce(gamePaddle.getView());

		// check for collisions (existing comments retained)
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

	}
	
	public void moverMovesHorizontally(boolean goLeft) {
	    if (goLeft) {
	        // myMover.setX(myMover.getX() - MOVER_SPEED);
	        // ADDED: link paddle movement
	        gamePaddle.moveLeft();
	    } else {
	        // myMover.setX(myMover.getX() + MOVER_SPEED);
	        gamePaddle.moveRight(); // ADDED
	    }
	}


	public void handleMouseInput (double x, double y) {
//		if (myGrower.contains(x, y)) {
//
//		}

		// ADDED: optional mouse control for paddle
		gamePaddle.getView().setX(x - gamePaddle.getView().getWidth() / 2);
	}
}