package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Class representing a plane in 3D space
 */
public class Plane extends Geometry {
    /**
     * A point on the plane
     */
    protected final Point q0;

    /**
     * The normal vector to the plane
     */
    protected final Vector normal;

    /**
     * Constructor for creating a plane from three points
     * @param p1 First point
     * @param p2 Second point
     * @param p3 Third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.q0 = p1;
        // Calculate the normal vector from the three points
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        // The normal is the cross product of the two vectors
        this.normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Constructor for creating a plane from a point and normal vector
     * @param q0 A point on the plane
     * @param normal The normal vector to the plane
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }


    public Vector getNormal() {
        return normal;
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        // Check if the ray starts on the plane
        if (q0.equals(p0)) {
            return null; // The ray lies on the plane or starts on it: considered no intersection
        }

        // if the resulting dot product is zero, the ray is parallel to the plane and therefore no intersection
        if(Util.isZero(v.dotProduct(normal)))
            return null;

        // calculate the intersection point
        double t = normal.dotProduct(q0.subtract(p0)) / normal.dotProduct(v);

        return t <= 0? null : List.of(ray.getPoint(t));
    }
}

