import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 15;
    private static final int MOVE_DISTANCE = 15;

    private Rectangle view;

    public Paddle(int sceneWidth, int sceneHeight) {
        // Start near the bottom center
        view = new Rectangle((sceneWidth - WIDTH) / 2, sceneHeight - 40, WIDTH, HEIGHT);
        view.setFill(Color.DARKBLUE);
    }

    public Rectangle getView() {
        return view;
    }

    public void moveLeft() {
        view.setX(view.getX() - MOVE_DISTANCE);
    }

    public void moveRight() {
        view.setX(view.getX() + MOVE_DISTANCE);
    }
}