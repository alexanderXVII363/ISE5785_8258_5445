package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Testing Geometries
 */
public class GeometriesTest {
    // Helper geometries for testing
    private final Sphere sphere = new Sphere(1, new Point(0, 0, 0));
    private final Triangle triangle = new Triangle(
            new Point(-1, -1, 1),
            new Point( 1, -1, 1),
            new Point( 0,  1, 1)
    );
    private final Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
    // A ray that misses everything (points away)
    private final Ray missRay = new Ray(new Point(2, 2, -1), new Vector(0, 0, -1));
    // A ray that hits all of them (goes through origin toward +Z)
    private final Ray hitAllRay = new Ray(new Point(0, 0, -2), new Vector(0, 0, 1));
    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // Arrange: Define the geometries and rays for the tests
        Geometries empty = new Geometries(); // Empty collection
        Geometries allThree = new Geometries(sphere, triangle, plane); // Collection with sphere, triangle, and plane
        // ============ Equivalence Partition Tests =============
        // TC01: Intersection with multiple geometries (sphere + triangle + plane) → 4 intersection points
        List<Point> ptsSome = allThree.findIntersections(hitAllRay);
        assertNotNull(ptsSome, "The ray should intersect multiple geometries");
        assertEquals(4, ptsSome.size(), "There should be 4 intersection points from multiple geometries");
        // ============== Boundary Value Tests =================
        // TC02: Empty collection → should return null
        assertNull(empty.findIntersections(hitAllRay),
                "An empty collection should return null when the ray doesn't intersect any geometry");
        // TC03: No intersection → should return null
        assertNull(allThree.findIntersections(missRay),
                "When the ray doesn't hit any geometry, it should return null");
        // TC04: Single intersection (sphere only) → 2 intersection points
        List<Point> ptsSphereOnly = new Geometries(sphere).findIntersections(hitAllRay);
        assertNotNull(ptsSphereOnly, "The ray should intersect only the sphere");
        assertEquals(2, ptsSphereOnly.size(), "The sphere creates two intersection points");
        // TC05: Intersection of all geometries in stages: 4 points
        List<Point> pts3 = allThree.findIntersections(hitAllRay);
        assertNotNull(pts3);
        assertEquals(4, pts3.size(), "Sphere, triangle, and plane together create 4 intersection points");
    }

}
