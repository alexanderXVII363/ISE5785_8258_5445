package primitives;

/**
 * A class that represents a ray in three-dimensional space,
 * defined by a starting point (head) and a direction vector.
 * The class use a Point object and a Vector object.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    private final Point head;

    /**
     * The direction vector of the ray, normalized to have unit length.
     */
    private final Vector direction;

    /**
     * Constructs a new Ray with the specified head (starting point) and direction vector.
     * The direction vector is normalized to have unit length.
     *
     * @param _head      The starting point of the ray.
     * @param _direction The direction vector of the ray.
     */
    public Ray(Point _head, Vector _direction){
        this.head = _head;
        this.direction = _direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other) && this.head.equals(other.head) && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return head.toString()+ direction.toString();
    }
}