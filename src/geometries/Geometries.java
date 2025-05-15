package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {
    private final List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {
        // List is already initialized above.
    }

    public Geometries(Intersectable... geometries) {
        this();  // reuse default constructor
        for (Intersectable geo : geometries) {
            add(geo);
        }
    }

    public void add(Intersectable geometry) {
        geometries.add(geometry);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null; // Initialize as null to avoid unnecessary list creation
        for (Intersectable geometry : geometries) {
            List<Point> geoIntersections = geometry.findIntersections(ray);
            if (geoIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(geoIntersections);
            }
        }
        return intersections; // Return the list of intersections (or null if there are no intersections)
    }
}