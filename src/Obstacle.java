import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Obstacle extends Brick {

	public Obstacle(int x, int y) {
		super(x, y, -1, Color.BLACK);
		
	}
	
	@Override
	public int handleHit(Circle ball) {
	    return getPointValue();
	}
	
}
