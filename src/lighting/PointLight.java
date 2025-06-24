package lighting;
import primitives.Point;
import primitives.Color;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    private final Point position;
    private double kC = 1;      // constant attenuation
    private double kL = 0;      // linear attenuation
    private double kQ = 0;      // quadratic attenuation

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        double attenuation = kC + kL * d + kQ * d * d;
        return intensity.scale(1.0 / attenuation);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }


    public LightSource setNarrowBeam(int i){
        // I had a problem with the code and i was suggested to fix by doing this
        // PointLight does not support narrow beam, return this for consistency
        return this;
    }
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }
}
