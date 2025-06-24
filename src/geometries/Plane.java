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

    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point head = ray.getHead();
        if (q0.equals(head))  // ray starts exactly on the plane's reference point
            return null;

        Vector u = q0.subtract(head);
        double numerator = u.dotProduct(normal);
        double denominator = normal.dotProduct(ray.getDirection());

        // Ray is parallel to the plane
        if (Util.isZero(denominator))
            return null;

        double t = Util.alignZero(numerator / denominator);

        // No intersection if t <= 0 (i.e., point is behind the ray's head or at the head)
        if (t <= 0)
            return null;

        return List.of(new Intersection(this, ray.getPoint(t)));
    }
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty()) {
            return List.of();
        }
        // Filter intersections based on maxDistance
        return intersections.stream()
                .filter(intersection -> intersection.point.distance(ray.getHead()) <= maxDistance)
                .toList();
    }
}

