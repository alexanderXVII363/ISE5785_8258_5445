package geometries;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import java.util.List;
import static primitives.Util.alignZero;

/**
 * A class that represents a triangle in three-dimensional space defined by three vertices.
 * The class inherits from the Polygon class.
 */
public class Triangle extends Polygon {
    public Triangle(Point point1, Point point2, Point point3) {
        super(point1, point2, point3);
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Step 1: Find intersection with the plane of the triangle
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) return null;

        Point p0 = ray.getHead();         // Starting point of the ray
        Vector v = ray.getDirection();  // Direction of the ray

        Point p = planeIntersections.getFirst(); // Intersection point with the plane

        // Step 2: Check if the point is inside the triangle using inside-out test
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = alignZero(v.dotProduct(n1));
        double s2 = alignZero(v.dotProduct(n2));
        double s3 = alignZero(v.dotProduct(n3));

        // If all signs are the same (positive or negative), the point is inside
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return List.of(new Intersection(this,p));
        }

        return null; // The point is outside the triangle
    }
}