import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/*
 * @author Colby R
 */

public class GalagaLevel extends Level {

	private static final double BUG_DENSITY = 0.9;
	private static final int BUG_WIDTH = 20;
	private static final int BUG_HEIGHT = 50;
	
	private List<Bug> enemies;
	
	public GalagaLevel(int rows, int cols) {
		super(rows, cols);
	}

	@Override
	public void createLevel(Group root) {
		enemies = new ArrayList<>();
		
        int spacingX = 60, spacingY = 40;
        int offsetX = 100, offsetY = 60;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
            	if(Math.random() > BUG_DENSITY) {
            		break;
            	}
                Bug enemy = new Bug(offsetX + col * spacingX, offsetY + row * spacingY, 100, Color.RED);
                enemies.add(enemy);
                root.getChildren().add(enemy.getView());
            }
        }
	}
	
	public List<Bug> getBugs(){
		return enemies;
	}
}
