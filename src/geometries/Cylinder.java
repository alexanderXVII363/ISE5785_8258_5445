package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
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
    public Vector getNormal(Point point) {
        Point p0 = axis.getHead();
        Vector v = axis.getDirection();

        // Calculate the vector from the ray's head to the point
        if(point.equals(p0)){
            return v.scale(-1);
        }
        Vector p0ToPoint = point.subtract(p0);

        // Calculate the projection of p0ToPoint on the ray's direction
        double t = v.dotProduct(p0ToPoint);

        // Check if the point is on one of the bases
        if (Util.isZero(t)) {
            // The point is on the base at the ray's head (bottom base)
            return v.scale(-1);
        }

        if (Util.isZero(t - height)) {
            // The point is on the top base
            return v;
        }

        // The point is on the side (the tube part)
        Point projectedPoint = p0.add(v.scale(t));
        return point.subtract(projectedPoint).normalize();
    }
}