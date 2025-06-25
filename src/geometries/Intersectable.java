package geometries;

import java.util.List;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public  abstract class Intersectable {
    /**
     * Find intersections of a ray with the geometry object
     *
     * @param ray the ray to which we find intersections with
     * @return a list of intersection points
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list==null||list.isEmpty() ? null : list.stream().map(intersection -> intersection.point).toList();
    }
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    //we will add a public static class called Intersection with geometry and point
    public static class Intersection {

        public final Geometry geometry;
        public final Point point;
        public final Material material;
        // Cached fields for shading calculations
        // Direction vector of the ray at the intersection
        public Vector v;
        // Normal vector at the intersection point
        public Vector n;
        // Dot product of the view vector and the normal vector
        public double nv;
        // Cached light source for lighting calculations
        public LightSource lightSource;
        // Vector from the intersection point to the light source
        public Vector l;
        // Dot product of the normal vector and the light vector
        public double nl;


        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry != null ? geometry.getMaterial() : null;
        }
        /**
         * equals method to compare two Intersection objects based on their geometry and point.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Intersection)) return false;
            Intersection other = (Intersection) obj;
            return geometry.equals(other.geometry) && point.equals(other.point);
        }
        /**
         * toString method to return a string representation of the Intersection object.
         */
        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }


    }

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }







}
