package primitives;

import java.util.List;

/**
 * Class Ray is the basic class representing a ray in Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system, with a starting point and a direction vector.
 */
public class Ray {
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
        if (points == null || points.isEmpty()) {
            return null;
        }

        Point closestPoint = points.get(0);
        double minDistanceSquared = head.distanceSquared(closestPoint);

        for (int i = 1; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            double currentDistanceSquared = head.distanceSquared(currentPoint);

            if (currentDistanceSquared < minDistanceSquared) {
                closestPoint = currentPoint;
                minDistanceSquared = currentDistanceSquared;
            }
        }

        return closestPoint;
    }
}
