package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a generic geometry in three-dimensional space.
 */
abstract public class Geometry {
    /**
     * Abstract method to retrieve the normal vector to the geometry at a specified point on its surface.
     *
     * @param point_on_body The point on the surface of the geometry for which to find the normal vector.
     * @return The normal vector to the geometry at the specified point.
     */
    abstract public Vector getNormal(Point point_on_body);
}