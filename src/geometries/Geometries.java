package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {
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

    public void add(Intersectable...geometry) {
        this.geometries.addAll(List.of(geometry));
    }

    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> totalIntersections = null;
        for (Intersectable geo : geometries) {
            List<Intersection> intersections = geo.calculateIntersections(ray);
            if (intersections != null && !intersections.isEmpty()) {
                if (totalIntersections == null) {
                    totalIntersections = new LinkedList<>(intersections);
                } else {
                    totalIntersections.addAll(intersections);
                }
            }
        }
        return totalIntersections;
    }





}