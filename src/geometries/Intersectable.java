package geometries;

import java.util.List;
import primitives.Point;
import primitives.Ray;
public interface Intersectable {
    /**
     * Find intersections of a ray with the geometry object
     * @param ray the ray to which we find intersections with
     * @return a list of intersection points
     */
    List<Point> findIntersections(Ray ray);

}
