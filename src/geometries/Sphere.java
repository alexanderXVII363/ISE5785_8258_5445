package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;

import java.util.List;

/**
 * A class that represents a sphere in three-dimensional space, defined by a radius and a center point.
 * The class inherits from the RadialGeometry class.
 */
public class Sphere extends RadialGeometry{
    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a new Sphere with the specified radius and center point.
     *
     * @param _radius The radius of the sphere.
     * @param _point  The center point of the sphere.
     */
    public Sphere(double _radius, Point _point){
        super(_radius);
        this.center = _point;
    }
    public Sphere( Point _point, double _radius){
        super(_radius);
        this.center = _point;
    }
    @Override
    public Vector getNormal(Point point_on_body) {
        // Calculate the normal vector at the given point on the sphere's surface
        Vector normal = point_on_body.subtract(center);
        // Normalize the vector to ensure it has a length of 1
        return normal.normalize();
    }

    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();  // The starting point of the ray
        Vector dir = ray.getDirection();  // The direction vector of the ray

        // Case 1: The ray starts at the center of the sphere
        if (center.equals(p0)) {
            // If the ray starts at the center, the intersection will be on the surface of the sphere at a distance equal to the radius
            return List.of(new Intersection(this,ray.getPoint(radius)));  // Add the scaled direction to the center to get the intersection point
        }

        // Case 2: General case for a ray not starting at the center
        Vector u = center.subtract(p0);  // Vector from the ray origin to the center of the sphere
        double tm = dir.dotProduct(u);  // Projection of the vector u onto the direction vector dir
        double d = Util.alignZero(Math.sqrt(u.lengthSquared() - tm * tm));  // Calculate the perpendicular distance from the center to the ray

        // If the perpendicular distance is greater than or equal to the radius, there is no intersection
        if (Util.alignZero(d - radius) >= 0)
            return null;

        // Calculate th, the distance along the ray to the intersection points
        double th = Math.sqrt(radius * radius - d * d);  // The distance from the ray origin to the intersection points

        double t1 = Util.alignZero(tm - th);  // First intersection point
        double t2 = Util.alignZero(tm + th);  // Second intersection point

        // Both intersection points are valid (the ray intersects the sphere at two points)
        if (t1 > 0 && t2 > 0) {
            return List.of(new Intersection(this,ray.getPoint(t1)),new Intersection(this,ray.getPoint(t2)) );  // Both intersections are valid
        }

        // The ray starts inside the sphere and intersects at one point
        if (t1 > 0) {
            return List.of(new Intersection(this,ray.getPoint(t1)));  // One intersection point inside the sphere
        }

        // The ray intersects at one point
        if (t2 > 0) {
            return List.of(new Intersection(this,ray.getPoint(t2)));  // One intersection point outside the sphere
        }

        // If no valid intersection, return null
        return null;  // No valid intersections
    }
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Calculate intersections with the sphere and filter by maxDistance
        List<Intersection> intersections = calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty()) {
            return List.of();  // No intersections found
        }

        // Filter intersections based on the maximum distance
        return intersections.stream()
                .filter(intersection -> ray.getHead().distance(intersection.point) <= maxDistance)
                .toList();
    }
}
