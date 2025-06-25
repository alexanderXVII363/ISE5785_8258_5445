package primitives;

import java.util.List;
import geometries.Intersectable.Intersection;
/**
 * Class Ray is the basic class representing a ray in Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system, with a starting point and a direction vector.
 */
public class Ray {

    private static final double DELTA = 0.1;

    /**
     * The starting point of the ray
     */
    private final Point head;

    /**
     * The normalized direction vector of the ray
     */
    private final Vector direction;

    /**
     * Constructor for creating a new ray with a point and direction vector
     * @param p0 The starting point of the ray
     * @param dir The direction vector of the ray (will be normalized)
     */
    public Ray(Point p0, Vector dir) {
        this.head = p0;
        this.direction = dir.normalize();
    }

    public Ray(Point p,Vector direction, Vector normal){
        this.direction = direction;
        head = p.add(normal.scale(direction.dotProduct(normal)<0?-DELTA:DELTA));
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other) && this.head.equals(other.head) && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "Ray [origin=" + head + ", direction=" + direction + "]";
    }


    /*
     * Getter for the point on the ray
     * @return the point on the ray
     */
    public Point getHead() {return head;}

    /**
     * Getter for the direction vector of the ray
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns a point on the ray at a given distance from the starting point
     * @param t The distance from the starting point
     * @return A new point on the ray at distance t from the starting point
     */
    public Point getPoint(double t){
        if(Util.isZero(t)) return head;
        return head.add(direction.scale(t));
    }
    // findClosestPoint receives a list of points and returns the closest point to head of the ray
    public Point findClosestPoint(List<Point> points) {
        return points.isEmpty() ? null
                : findClosestIntersection(
                points.stream()
                        .map(p -> new Intersection(null, p))
                        .toList()
        ).point;
    }

    /**
     * Finds the closest intersection point from a list of intersections.
     * @param intersections
     * @return the closest intersection point to the ray's head, or null if no intersections exist
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) return null;
        return intersections.stream()
                .min((i1, i2) -> Double.compare(i1.point.distance(head), i2.point.distance(head)))
                .orElse(null);
    }


}
