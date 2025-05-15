package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import static org.junit.jupiter.api.Assertions.*;

/** Testing Triangles*/
class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     * Tests that the normal vector returned by the triangle is correct and normalized.
     */
    @Test
    void testGetNormal() {
        // Create a triangle in 3D space
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Triangle triangle = new Triangle(p1, p2, p3);

        // Choose a point on the triangle surface
        // In this case, we use a point on the triangle (for example, the center)
        Point point = new Point(1.0 / 3, 1.0 / 3, 0);

        // Get the actual normal from the triangle
        Vector normal = triangle.getNormal(point);

        // For this triangle (lying on the XY plane), the normal should be along Z axis
        // Since either direction can be valid (0,0,1) or (0,0,-1), we check both
        Vector expectedNormal = new Vector(0, 0, 1);

        // Test that the normal is either the expected normal or its negation
        boolean normalIsCorrect = normal.equals(expectedNormal) ||
                normal.equals(expectedNormal.scale(-1));

        assertTrue(normalIsCorrect, "Triangle normal vector is not perpendicular to the surface");

        // Test that the normal is a unit vector (length = 1)
        assertEquals(1, normal.length(), 0.00001, "Normal vector is not normalized");
    }
    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     * Tests the intersection of a ray with a triangle.
     */
    @Test
    void testFindIntersections() {
        //================== Equivalence Partitions Tests ==================
        // TC01: the intersection point is inside the triangle
        Triangle triangle = new Triangle(new Point(1, 1, 0), new Point(1, 0, 0), new Point(0, 1, 0));
        assertEquals(1, triangle.findIntersections(new Ray(new Point(1.8, 1.8, 1), new Vector(-1, -1, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");
        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 2, 1), new Vector(1, 0, 0))),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");
        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(new Ray(new Point(2, 2, 1), new Vector(1, 0, 0))),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");
        //================== Boundary Values Tests ==================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 1, -1), new Vector(1, 0, 0))),
                "Failed to find the intersection point when the intersection point is on the edge of the triangle");
        // TC05: the intersection point is on the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the triangle");
        // TC06: the intersection point is outside the triangle but in the path of the edge
        assertNull(triangle.findIntersections(new Ray(new Point(2, 1, -1), new Vector(1, 0, 0))),
                "Failed to find the intersection point when the intersection point is outside the triangle but in the path of the edge");
    }
}
