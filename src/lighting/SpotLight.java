package lighting;

import primitives.Point;
import primitives.Color;
import primitives.Vector;

public class SpotLight extends PointLight {
    private final Vector direction;
    private int narrowBeam = 1;

    /**
     * Constructor for SpotLight
     * @param intensity the color/intensity of the light
     * @param position the position of the light source
     * @param direction the direction of the spotlight (will be normalized)
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity(p).scale(Math.pow(Math.max(0, direction.dotProduct(getL(p))), narrowBeam));
    }

    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
    @Override
    public PointLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }
    @Override
    public PointLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }
    @Override
    public PointLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }
}
