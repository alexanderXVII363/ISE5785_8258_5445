package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor initializes an empty list of geometries.
     * This constructor is used to create an instance of Geometries without any initial geometries.
     */
    public Geometries() {
        // List is already initialized above.
    }

    /**
     * Constructor that accepts a collection of Intersectable geometries.
     * This allows for the creation of a Geometries object with an initial set of geometries.
     *
     * @param geometries Collection of Intersectable objects to be added to this Geometries instance.
     */
    public Geometries(Intersectable... geometries) {
        this();  // reuse default constructor
        for (Intersectable geo : geometries) {
            add(geo);
        }
    }

    /**
     * Adds a collection of Intersectable geometries to this Geometries instance.
     * This method allows for adding multiple geometries at once.
     *
     * @param geometry Varargs parameter allowing multiple Intersectable objects to be added.
     */
    public void add(Intersectable...geometry) {
        this.geometries.addAll(List.of(geometry));
    }

    /**
     * Adds a single Intersectable geometry to this Geometries instance.
     * This method allows for adding a single geometry at a time.
     *
     * @param ray The Ray object to be added to the geometries.
     */
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