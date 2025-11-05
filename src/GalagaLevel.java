import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class GalagaLevel extends Level {

	private static final double BRICK_DENSITY = 0.75;
	private static final int BUG_WIDTH = 20;
	private static final int BUG_HEIGHT = 50;
	
	private List<Bug> bugs;
	
	public GalagaLevel(int rows, int cols) {
		super(rows, cols);
	}

	@Override
	public void createLevel(Group root) {
		bugs = new ArrayList<>();
		
		// randomly chooses positions to place bugs based on their set density
		for (int row = 0; row < rows; row++) {
			Bug bug = null;
			for (int col = 0; col < cols; col++) {
				if (Math.random() > BRICK_DENSITY) {
					continue;
				}
				int x = OFFSET_X + col * (3 * BUG_WIDTH + SPACING);
				int y = OFFSET_Y + row * (BUG_HEIGHT + SPACING);
				Color color = Color.hsb((row * 60) % 360, 0.8, 0.9);
				//bug = new Bug(x, y, 100, color);
				bugs.add(bug);
				//root.getChildren().add(bug.getView());
			}
		}
	}
	
	public List<Bug> getBugs(){
		return bugs;
	}
}
