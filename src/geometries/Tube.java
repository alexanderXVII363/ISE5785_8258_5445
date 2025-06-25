package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * A class that represents a tube in three-dimensional space, defined by a radius and an axis (Ray).
 * The class inherits from the RadialGeometry class.
 */
public class Tube extends RadialGeometry {
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
    public Tube(double _radius, Ray _axis) {
        super(_radius);
        this.axis = _axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getHead();
        Vector v = axis.getDirection();

        // Calculate the vector from the ray's head to the point
        Vector p0ToPoint = point.subtract(p0);

        // Calculate the projection of p0ToPoint on the ray's direction
        double t = v.dotProduct(p0ToPoint);

        // Calculate the point on the axis ray that is closest to the given point
        Point projectedPoint;
        if (Util.isZero(t)) {
            // The point is right across from the ray's head
            projectedPoint = p0;
        } else {
            // Calculate the point on the axis ray at distance t from head
            projectedPoint = p0.add(v.scale(t));
        }

        // The normal is the vector from the projected point to the given point, normalized
        return point.subtract(projectedPoint).normalize();
    }

    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // The tube does not have a specific intersection calculation method,
        // so we return null to indicate no intersections.
        return null;
    }

}