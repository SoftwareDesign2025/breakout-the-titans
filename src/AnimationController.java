import javafx.scene.Group;

public class AnimationController {
    private int width;
    private int height;
    private Paddle paddle;

    public Group createRootForAnimation(int windowWidth, int windowHeight) {
        width = windowWidth;
        height = windowHeight;

        Group root = new Group();

        paddle = new Paddle(width, height);
        root.getChildren().add(paddle.getView());

        return root;
    }

    public void moverMovesHorizontally(boolean goLeft) {
        if (goLeft) {
            paddle.moveLeft();
        } else {
            paddle.moveRight();
        }
    }

    public void step(double elapsedTime) {
        // Nothing to animate yet, but will soon!
    }

    public void handleMouseInput(double x, double y) {
        // Future: maybe move paddle with mouse
    }
}
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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
	
	public Group createRootForAnimation(int windowWidth, int windowHeight) {
		width = windowWidth;
		height = windowHeight;

		// create one top level collection to organize the things in the scene
		Group root = new Group();
		// make some shapes and set their properties
		
		gameBall = new Ball(width/2, height/2);
		paddle = new Rectangle(100,100);
		
		root.getChildren().add(gameBall.getBall());
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

	}
	
	public void moverMovesHorizontally(boolean goLeft) {
	    if (goLeft) {
	        // myMover.setX(myMover.getX() - MOVER_SPEED);
	    } else {}
	        // myMover.setX(myMover.getX() + MOVER_SPEED);
	}


	public void handleMouseInput (double x, double y) {
//		if (myGrower.contains(x, y)) {
//
//		}
	}
}
