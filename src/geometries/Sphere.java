
package geometries;

import primitives.Point;
import primitives.Vector;

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

    @Override
    public Vector getNormal(Point point_on_body) {
        // Calculate the normal vector at the given point on the sphere's surface
        Vector normal = point_on_body.subtract(center);
        // Normalize the vector to ensure it has a length of 1
        return normal.normalize();
    }
}
