package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A class that represents a cylinder in three-dimensional space, defined by a radius, an axis (Ray), and a height.
 * The class inherits from the Tube class.
 */
public class Cylinder extends Tube{
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a new Cylinder with the specified radius, axis, and height.
     *
     * @param _radius The radius of the cylinder.
     * @param _axis   The axis of the cylinder, represented by a Ray.
     * @param _height The height of the cylinder.
     */
    public Cylinder(double _radius, Ray _axis, double _height){
        super(_radius, _axis);
        height = _height;
    }

    @Override
    public Vector getNormal(Point point_on_body) {
        return null;
    }
}