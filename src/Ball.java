import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Ball {
	public static final int BALL_RADIUS = 10;
    public static final int BALL_SPEED = 200;

    private Circle ball;
    private Point2D ballVelocity;
    
	public Ball (int startXPosition, int startYPosition) {
        ball = new Circle(BALL_RADIUS, Color.BLUE);
        
        ball.setCenterX(startXPosition);
        ball.setCenterY(startYPosition);
        
        double startAngle = Math.random() * 2 * Math.PI;
        ballVelocity = new Point2D(BALL_SPEED * Math.cos(startAngle), BALL_SPEED * Math.sin(startAngle));
	}
	
	public Circle getBall() {
		return ball;
	}
	
	 // Move the ball based on velocity and elapsed time
    public void move(double elapsedTime) {
        ball.setCenterX(ball.getCenterX() + ballVelocity.getX() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ballVelocity.getY() * elapsedTime);
    }
    
    // Bounce off screen edges
    public void wallBounce(double screenWidth, double screenHeight) {

        // bounce horizontally
        if (ball.getCenterX() - BALL_RADIUS <= 0 || ball.getCenterX() + BALL_RADIUS >= screenWidth) {
            ballVelocity = new Point2D(-ballVelocity.getX(), ballVelocity.getY());
        }

        // bounce vertically
        if (ball.getCenterY() - BALL_RADIUS <= 0 || ball.getCenterY() + BALL_RADIUS >= screenHeight) {
            ballVelocity = new Point2D(ballVelocity.getX(), -ballVelocity.getY());
        }
    }
    
    // Bounce off paddle
    public void paddleBounce(Rectangle paddle) {
    	Shape intersection = Shape.intersect(ball, paddle);
		if (intersection.getBoundsInLocal().getWidth() != -1) {
			if (ball.getCenterY() < paddle.getY() && ballVelocity.getY() > 0) {
				
			}

		}
    }
}
