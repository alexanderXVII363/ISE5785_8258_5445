package lighting;
import primitives.Point;
import primitives.Color;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }
    @Override
    public Vector getL(Point p) {
        return direction;
    }
    @Override
    public double getDistance(Point p) {
        // Directional light is considered to be infinitely far away
        return Double.POSITIVE_INFINITY;
    }



}

