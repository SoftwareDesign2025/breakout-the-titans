import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Brick {
	private static final int WIDTH = 50;
	private static final int HEIGHT = 20;

	private Rectangle view;
	private int pointValue;

	public Brick(int x, int y, int points, Color color) {
		view = new Rectangle(x, y, WIDTH, HEIGHT);
		view.setFill(color);
		view.setStroke(Color.BLACK);
		pointValue = points;
	}

	public Rectangle getView() {
		return view;
	}

	public int getPointValue() {
		return pointValue;
	}

	public boolean isHit(Circle ball) {
		return view.getBoundsInParent().intersects(ball.getBoundsInParent());
	}
	
	public int handleHit(Circle ball) {
	    if (isHit(ball)) {
	        view.setVisible(false);
	        return pointValue; 
	    }
	    return 0;
	}
}
