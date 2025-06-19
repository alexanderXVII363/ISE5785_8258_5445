package lighting;
import primitives.Point;
import primitives.Color;
import primitives.Vector;

public interface LightSource {
    Color getIntensity(Point p);
    Vector getL(Point p);
}
