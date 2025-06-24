package lighting;
import primitives.Point;
import primitives.Color;
import primitives.Vector;

public interface LightSource {
    Color getIntensity(Point p);
    Vector getL(Point p);
    // getDistance method returns the distance from the light source to a point in space
    double getDistance(Point p);
}
