import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

// Joe was here

/**
 * A basic example JavaFX program for the first lab.
 * 
 * @author Shannon Duvall
 * 
 * This class contains the code that runs the basic window.
 * 
 * Modified to be starting point of the breakout lab
 * 
 */
public class Main extends Application {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final String TITLE = "Breakout";
    public static final Paint BACKGROUND = Color.GRAY;
    private GameController currentController;


	private Scene myScene;
	// This class will handle the logic that is specific to this application.
	private AnimationController myAnimation;

	
	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start (Stage stage) {
		showStartMenu(stage);	
	}

	// What to do each time a key is pressed
	private void handleMouseInput (double x, double y) {
		myAnimation.handleMouseInput(x, y);
	}

	/*
	 * Start the program.
	 */
	public static void main (String[] args) {
		launch(args);
	}
	
	private void setupAndRunGame(Stage stage, GameController controller) {
	    Group root = controller.createRootForAnimation(WIDTH, HEIGHT);
	    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);

	    scene.setOnKeyPressed(e -> controller.handleKeyInput(e.getCode()));
	    scene.setOnKeyReleased(e -> controller.handleKeyRelease(e.getCode()));

	    stage.setScene(scene);
	    stage.setTitle(TITLE);
	    stage.show();

	    controller.startAnimation();
	

	    // Run the animation loop
	    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
	        e -> currentController.step(SECOND_DELAY));
	    Timeline animation = new Timeline();
	    animation.setCycleCount(Timeline.INDEFINITE);
	    animation.getKeyFrames().add(frame);
	    animation.play();

	    stage.setScene(scene);
	}
	
	private void startBreakout(Stage stage) {
	    currentController = new AnimationController();
	    setupAndRunGame(stage, currentController);
	}

	private void startGalaga(Stage stage) {
	    currentController = new GalagaController();
	    setupAndRunGame(stage, currentController);
	}
	
	
	
	private void showStartMenu(Stage stage) {
	    Group menuRoot = new Group();
	    Scene menuScene = new Scene(menuRoot, WIDTH, HEIGHT, Color.BLACK);

	    javafx.scene.text.Text title = new javafx.scene.text.Text(WIDTH / 2.0 - 180, HEIGHT / 2.0 - 100, "Choose Your Game");
	    title.setFill(Color.CYAN);
	    title.setFont(javafx.scene.text.Font.font(36));

	    javafx.scene.text.Text breakoutOption = new javafx.scene.text.Text(WIDTH / 2.0 - 120, HEIGHT / 2.0, "Press B for Breakout");
	    breakoutOption.setFill(Color.WHITE);
	    breakoutOption.setFont(javafx.scene.text.Font.font(24));

	    javafx.scene.text.Text galagaOption = new javafx.scene.text.Text(WIDTH / 2.0 - 120, HEIGHT / 2.0 + 50, "Press G for Galaga");
	    galagaOption.setFill(Color.WHITE);
	    galagaOption.setFont(javafx.scene.text.Font.font(24));

	    menuRoot.getChildren().addAll(title, breakoutOption, galagaOption);

	    menuScene.setOnKeyPressed(e -> {
	        if (e.getCode() == KeyCode.B) {
	            startBreakout(stage);
	        } else if (e.getCode() == KeyCode.G) {
	            startGalaga(stage);
	        }
	    });

	    stage.setScene(menuScene);
	    stage.show();
	}
}


