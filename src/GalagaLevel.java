import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/*
 * @author Colby R
 * 
 * A level of Galaga, with rows and columns of bugs 
 */

public class GalagaLevel extends Level {

	private static final double BUG_DENSITY = 0.9;
	private static final int BUG_WIDTH = 20;
	private static final int BUG_HEIGHT = 50;
	protected static final int SPACING_X = 60;
	protected static final int SPACING_Y = 40;
	protected static final int OFFSET_X = 100;
	protected static final int OFFSET_Y = 60;
	
	private List<Bug> enemies;
	
	public GalagaLevel(int rows, int cols) {
		super(rows, cols);
	}

	// creates array of bugs and adds to root
	@Override
	public void createLevel(Group root) {
		enemies = new ArrayList<>();
		
		// randomly chooses positions to place bricks and obstacles based on their set densities
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
            	if(Math.random() > BUG_DENSITY) {
            		break;
            	}
                Bug enemy = new Bug(OFFSET_X + col * SPACING_X, OFFSET_Y + row * SPACING_Y, 100, Color.RED);
                enemies.add(enemy);
                root.getChildren().add(enemy.getView());
            }
        }
	}
	
	public List<Bug> getBugs(){
		return enemies;
	}
}
