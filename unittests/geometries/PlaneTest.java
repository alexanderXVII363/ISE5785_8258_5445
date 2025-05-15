package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

/**
 * Testing Planes
 */
class PlaneTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    // Points and planes for testing
    Point p1 = new Point(0, 0, 0);
    Point p2 = new Point(1, 0, 0);
    Point p3 = new Point(0, 1, 1);
    Point p4 = new Point(0, 0, 1);
    Plane plane = new Plane(p1, p2, p3);

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructorThreePoints() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct plane from three points
        // Compute two vectors from points
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        // Ensure normal is orthogonal to both vectors
        Vector normal = plane.getNormal(p1);
        assertEquals(0, normal.dotProduct(v1), DELTA, "Plane normal is not orthogonal to first vector");
        assertEquals(0, normal.dotProduct(v2), DELTA, "Plane normal is not orthogonal to second vector");

        // Ensure the normal is a unit vector
        assertEquals(1, normal.length(), DELTA, "Plane normal is not a unit vector");

        // =============== Boundary Values Tests ==================

        // TC10: First and second points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3),
                "Constructed a plane with two identical points");

        // TC11: First and third points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1),
                "Constructed a plane with two identical points");

        // TC12: Second and third points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2),
                "Constructed a plane with two identical points");

        // TC13: All points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1),
                "Constructed a plane with all identical points");

        // TC14: All points are on the same line
        Point p4 = new Point(2, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p4),
                "Constructed a plane with all points on the same line");
    }

    /** Test method for {@link geometries.Plane#getNormal(primitives.Point)}. */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that the normal to plane is properly calculated

        // Calculate the expected normal
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Vector expected = v1.crossProduct(v2).normalize();

        // Test with a point on the plane (p1)
        Vector result = plane.getNormal(p1);

        // Since the normal can point in either of two opposite directions,
        // it's enough to check that the result is either the expected vector or its negation
        assertTrue(expected.equals(result) || expected.scale(-1).equals(result),
                "Plane normal calculation is incorrect");

        // Verify that the normal's length is 1
        assertEquals(1, result.length(), 0.00001, "Plane normal length is not 1");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // =========== Equivalence Partitions Tests ==============
        // TC01: Ray intersects the plane and is neither orthogonal nor parallel to the plane
        Ray ray = new Ray(p4, new Vector(1,1,-1));
        assertEquals(1, plane.findIntersections(ray).size(), "Ray should intersect the plane");

        // TC02: Ray does not intersect the plane and is neither orthogonal nor parallel to the plane
        ray = new Ray(p4, new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // =============== Boundary Values Tests ==================

        // Group 1: Parallel
        // TC03: Ray is contained in the plane
        ray = new Ray(p1, new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // TC04: Ray is parallel to the plane
        ray = new Ray(p4, new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        //Group 2: Orthogonal
        // TC05: Ray starts off the plane and points away from it
        ray = new Ray(p4, new Vector(0, -1, 1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        // TC06: Ray starts off the plane and points towards it
        ray = new Ray(p4, new Vector(0, 1, -1));
        assertEquals(1,plane.findIntersections(ray).size(), "Ray should intersect the plane");

        // TC07: Ray starts on the plane and points away from it
        ray = new Ray(p1, new Vector(0, 1, -1));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

        //Group 3: Begins on plane
        // TC08: Ray starts on the origin of the plane but is neither orthogonal nor parallel to the plane
        ray = new Ray(p1, new Vector(1, 1, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");
        // TC09: Ray starts on the plane and is orthogonal to the plane
        ray = new Ray(new Point(2,2,2), new Vector(1, 1, 0));
        assertNull(plane.findIntersections(ray), "Ray should not intersect the plane");

    }

}

