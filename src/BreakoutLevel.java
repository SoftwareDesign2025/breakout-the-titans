import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;

/*
 * @author Colby R
 */

public class BreakoutLevel extends Level {
	
	private static final int BRICK_HEIGHT = 50;
	private static final int BRICK_WIDTH = 20;
	private static final double OBSTACLE_DENSITY = 0.05;
	private static final double BRICK_DENSITY = 0.75;
	
	private List<Brick> bricks;
	
	public BreakoutLevel(int rows, int cols) {
		super(rows, cols);
	}

	@Override
	public void createLevel(Group root) {
		bricks = new ArrayList<>();
		
		// randomly chooses positions to place bricks and obstacles based on their set densities
		for (int row = 0; row < rows; row++) {
			Brick brick = null;
			for (int col = 0; col < cols; col++) {
				if (Math.random() > BRICK_DENSITY) {
					continue;
				}
				int x = OFFSET_X + col * (3 * BRICK_WIDTH + SPACING);
				int y = OFFSET_Y + row * (BRICK_HEIGHT + SPACING);
				if (Math.random() < OBSTACLE_DENSITY) {
					brick = new Obstacle(x,y);
				} else {
					Color color = Color.hsb((row * 60) % 360, 0.8, 0.9);
					brick = new Brick(x, y, 100, color);
				}
				bricks.add(brick);
				root.getChildren().add(brick.getView());
			}
		}
	}
	
	public List<Brick> getBricks(){
		return bricks;
	}
	
}
