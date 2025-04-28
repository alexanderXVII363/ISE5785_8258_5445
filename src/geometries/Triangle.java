
package geometries;

import primitives.Point;

/**
 * A class that represents a triangle in three-dimensional space defined by three vertices.
 * The class inherits from the Polygon class.
 */
public class Triangle extends Polygon{
    public Triangle(Point point1, Point point2, Point point3) {
        super(point1, point2,point3);}
}
