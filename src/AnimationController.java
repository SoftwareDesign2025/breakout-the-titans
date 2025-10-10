import javafx.scene.Group;

public class AnimationController {
    private int width;
    private int height;
    private Paddle paddle;

    public Group createRootForAnimation(int windowWidth, int windowHeight) {
        width = windowWidth;
        height = windowHeight;

        Group root = new Group();

        paddle = new Paddle(width, height);
        root.getChildren().add(paddle.getView());

        return root;
    }

    public void moverMovesHorizontally(boolean goLeft) {
        if (goLeft) {
            paddle.moveLeft();
        } else {
            paddle.moveRight();
        }
    }

    public void step(double elapsedTime) {
        // Nothing to animate yet, but will soon!
    }

    public void handleMouseInput(double x, double y) {
        // Future: maybe move paddle with mouse
    }
}