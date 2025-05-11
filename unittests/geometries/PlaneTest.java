package geometries;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

/**
 * Testing Planes
 */
class PlaneTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructorThreePoints() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct plane from three points
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 1);
        Plane plane = new Plane(p1, p2, p3);

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
        // Create a plane using 3 points constructor
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

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
}

