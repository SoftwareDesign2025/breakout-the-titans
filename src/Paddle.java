import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 15;
    private static final double SPEED = 250; // pixels per second, tweak for comfort

    private final Rectangle view;
    private final int sceneWidth;

    public Paddle(int sceneWidth, int sceneHeight) {
        this.sceneWidth = sceneWidth;
        view = new Rectangle((sceneWidth - WIDTH) / 2.0, sceneHeight - 40, WIDTH, HEIGHT);
        view.setFill(Color.DARKBLUE);
    }

    public Rectangle getView() {
        return view;
    }

    public void moveLeft(double elapsedTime) {
        double dx = -SPEED * elapsedTime;
        double newX = view.getX() + dx;
        if (newX < 0) newX = 0; // keep on screen
        view.setX(newX);
    }

    public void moveRight(double elapsedTime) {
        double dx = SPEED * elapsedTime;
        double newX = view.getX() + dx;
        if (newX + WIDTH > sceneWidth) newX = sceneWidth - WIDTH; // keep on screen
        view.setX(newX);
    }
}