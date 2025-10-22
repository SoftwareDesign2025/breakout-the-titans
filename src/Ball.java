import java.util.Random;
import javafx.geometry.Point2D;
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
	
    public void move(double elapsedTime) {
        ball.setCenterX(ball.getCenterX() + ballVelocity.getX() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ballVelocity.getY() * elapsedTime);
    }
    
    public void wallBounce(double screenWidth, double screenHeight) {
        if (ball.getCenterX() - BALL_RADIUS <= 0 || ball.getCenterX() + BALL_RADIUS >= screenWidth) {
            ballVelocity = new Point2D(-ballVelocity.getX(), ballVelocity.getY());
        }

        if (ball.getCenterY() - BALL_RADIUS <= 0 || ball.getCenterY() + BALL_RADIUS >= screenHeight) {
            ballVelocity = new Point2D(ballVelocity.getX(), -ballVelocity.getY());
        }
    }
    
    // ADDED: refined paddle collision bounce
    public void paddleBounce(Rectangle paddle) {
    	Shape intersection = Shape.intersect(ball, paddle);
		if (intersection.getBoundsInLocal().getWidth() != -1) {
			// only bounce upward if ball was descending
			if (ballVelocity.getY() > 0) {
				ballVelocity = new Point2D(ballVelocity.getX(), -ballVelocity.getY());
				ball.setCenterY(paddle.getY() - BALL_RADIUS - 1); // prevent sticking
			}
		}
    }
    public void reverseY() {
        ballVelocity = new Point2D(ballVelocity.getX(), -ballVelocity.getY());
    }
    
    //We should add a general bounce method that can be applied to the bricks + paddle + wall
}
