import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class PowerUp {
    public enum Type {
        MULTI_BALL, MULTI_PADDLE
    }

    private static final double RADIUS = 10;
    private static final double FALL_SPEED = 100; 

    private Circle view;
    private Type type;
    private boolean active = true;

    public PowerUp(double x, double y, Type type) {
        this.type = type;
        view = new Circle(x, y, RADIUS);
        if (type == Type.MULTI_BALL) {
            view.setFill(Color.GOLD);
        } else {
            view.setFill(Color.LIGHTGREEN);
        }
    }

    public Circle getView() {
        return view;
    }

    public Type getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void move(double elapsedTime) {
        view.setCenterY(view.getCenterY() + FALL_SPEED * elapsedTime);
    }

    public boolean intersects(Shape shape) {
        return view.getBoundsInParent().intersects(shape.getBoundsInParent());
    }
}

