import javafx.scene.Group;

/*
 * @author Colby R
 */

public abstract class Level {
	
	protected static final int SPACING = 3;
	protected static final int OFFSET_X = 20;
	protected static final int OFFSET_Y = 40;
	
	protected int rows;
	protected int cols;
	
	public Level (int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	public abstract void createLevel (Group root);
	
}
