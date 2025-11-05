import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/*
 * @author Colby R
 */

public class Bug extends Brick {
	private static final int WIDTH = 50;
	private static final int HEIGHT = 20;
	
	private double y;
	
	public Bug(int x, int y, int points, Color color) {
		super(x,y,points,color);
		this.y = (double)y;
	}
	
	
	public int handleHit() {
	    getView().setVisible(false);
	    return getPointValue();
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
		getView().setY(y);
	}
	
	public int getHeight() {
		return HEIGHT;
	}

}
