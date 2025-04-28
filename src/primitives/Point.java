package primitives;

/**
 * A class that represents a point in three-dimensional space with x, y, and z coordinates.
 * The class use a Double3 object.
 */
public class Point {
    /**
     * The origin point with coordinates (0, 0, 0).
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * The three-dimensional coordinates of the point.
     */
    protected final Double3 xyz;

    /**
     * Constructs a new Point with the specified x, y, and z coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param z The z-coordinate of the point.
     */
    public Point(double x, double y, double z){
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a new Point using the coordinates of the given Double3 object.
     *
     * @param _new_obj The Double3 object containing the coordinates.
     */
    public Point(Double3 _new_obj){
        this.xyz = _new_obj;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return xyz.toString();
    }

    /**
     * Computes the vector obtained by subtracting another Point from this Point.
     *
     * @param another_point The Point to subtract from this Point.
     * @return The vector representing the subtraction of points.
     */
    public Vector subtract(Point another_point){
        return new Vector(this.xyz.subtract(another_point.xyz));
    }

    /**
     * Computes a new Point by adding a vector to this Point.
     *
     * @param another_vector The vector to add to this Point.
     * @return A new Point resulting from the addition of the vector.
     */
    public Point add(Vector another_vector){
        return new Point(this.xyz.add(another_vector.xyz));
    }

    /**
     * Computes the squared distance between this Point and another Point.
     *
     * @param another_point The other Point to compute the squared distance.
     * @return The squared distance between this Point and the other Point.
     */
    public double distanceSquared(Point another_point){
        // double dx = this.xyz.d1 - another_point.xyz.d1;
        return (this.xyz.d1 - another_point.xyz.d1) * (this.xyz.d1 - another_point.xyz.d1)
                + (this.xyz.d2 - another_point.xyz.d2) * (this.xyz.d2 - another_point.xyz.d2)
                + (this.xyz.d3 - another_point.xyz.d3) * (this.xyz.d3 - another_point.xyz.d3);
    }

    /**
     * Computes the distance between this Point and another Point.
     *
     * @param another_point The other Point to compute the distance.
     * @return The distance between this Point and the other Point.
     */
    public double distance(Point another_point){
        return Math.sqrt(distanceSquared(another_point));
    }
}