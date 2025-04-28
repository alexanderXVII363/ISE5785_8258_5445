package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A class that represents a tube in three-dimensional space, defined by a radius and an axis (Ray).
 * The class inherits from the RadialGeometry class.
 */
public class Tube extends RadialGeometry{
    /**
     * The axis of the tube, represented by a Ray.
     */
    protected final Ray axis;

    /**
     * Constructs a new Tube with the specified radius and axis.
     *
     * @param _radius The radius of the tube.
     * @param _axis   The axis of the tube, represented by a Ray.
     */
    public Tube(double _radius, Ray _axis){
        super(_radius);
        this.axis = _axis;
    }

    @Override
    public Vector getNormal(Point point_on_body) {
        return null;
    }
}