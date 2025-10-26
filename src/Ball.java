import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Ball {
	public static final int BALL_RADIUS = 10;
    public static final int BALL_SPEED = 150;

    private Circle ball;
    private Point2D ballVelocity;
    private int defaultXPosition;
    private int defaultYPosition;
    private Point2D previousPosition;
    
	public Ball (int startXPosition, int startYPosition) {
        ball = new Circle(BALL_RADIUS, Color.BLUE);
        defaultXPosition = startXPosition;
        defaultYPosition = startYPosition;
        resetBall(startXPosition, startYPosition);
	}
	
	// resets ball position to center of screen with velocity in random direction
	public void resetBall(int defaultXPosition, int defaultYPosition) {
		ball.setCenterX(defaultXPosition);
        ball.setCenterY(defaultYPosition);
        
        double startAngle = Math.random() * 2 * Math.PI;
        ballVelocity = new Point2D(BALL_SPEED * Math.cos(startAngle), BALL_SPEED * Math.sin(startAngle));
	}
	
	public Circle getBall() {
		return ball;
	}
	
    public void move(double elapsedTime) {
    	// save previous position before moving to assist objectBounce
        previousPosition = new Point2D(ball.getCenterX(), ball.getCenterY());
        
        ball.setCenterX(ball.getCenterX() + ballVelocity.getX() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ballVelocity.getY() * elapsedTime);
    }
    
    // bounce ball of frame of game, or reset if crosses through bottom
    public void wallBounce(double screenWidth, double screenHeight) {
        if (ball.getCenterX() - BALL_RADIUS <= 0 || ball.getCenterX() + BALL_RADIUS >= screenWidth) {
            ballVelocity = new Point2D(-ballVelocity.getX(), ballVelocity.getY());
        }

        if (ball.getCenterY() - BALL_RADIUS <= 0) {
            ballVelocity = new Point2D(ballVelocity.getX(), -ballVelocity.getY());
        }
        
        if (ball.getCenterY() + BALL_RADIUS >= screenHeight) {
            // shows that it fell off bottom
            ballVelocity = Point2D.ZERO;
        }
    }
    
    public void reverseY() {
        ballVelocity = new Point2D(ballVelocity.getX(), -ballVelocity.getY());
    }
    
    public void reverseX() {
    	ballVelocity = new Point2D(-ballVelocity.getX(), ballVelocity.getY());
    }
    
    // bounce ball of any rectangular object
    public void objectBounce(Rectangle rect) {
        // Check for intersection
        if (!ball.getBoundsInParent().intersects(rect.getBoundsInParent())) return;

        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();
        double prevX = previousPosition.getX();
        double prevY = previousPosition.getY();

        double rectLeft = rect.getX();
        double rectRight = rect.getX() + rect.getWidth();
        double rectTop = rect.getY();
        double rectBottom = rect.getY() + rect.getHeight();

        // check if bounced off left
        if (prevX + BALL_RADIUS <= rectLeft && ballX + BALL_RADIUS >= rectLeft) {
            reverseX();
            ball.setCenterX(rectLeft - BALL_RADIUS - 1);
            
        // check if bounced off right
        } else if (prevX - BALL_RADIUS >= rectRight && ballX - BALL_RADIUS <= rectRight) {
            reverseX();
            ball.setCenterX(rectRight + BALL_RADIUS + 1);
        }

        // check if bounced off top
        if (prevY + BALL_RADIUS <= rectTop && ballY + BALL_RADIUS >= rectTop) {
            reverseY();
            ball.setCenterY(rectTop - BALL_RADIUS - 1);

        // check if bounced off bottom
        } else if (prevY - BALL_RADIUS >= rectBottom && ballY - BALL_RADIUS <= rectBottom) {
            reverseY();
            ball.setCenterY(rectBottom + BALL_RADIUS + 1);
        }
        
        // add corner detection
    }

}
