package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // TC01: Test for a proper result
        Sphere sphere = new Sphere(1, new Point(1, 0, 0));
        Point pointOnSphere = new Point(2, 0, 0);
        Vector expectedNormal = new Vector(1, 0, 0);
        Vector actualNormal = sphere.getNormal(pointOnSphere);
        assertEquals(expectedNormal, actualNormal, "ERROR: The normal vector is incorrect. Expected a vector pointing outward from the sphere's center to the point on the surface.");
    }
    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        //=========== Equivalence Partitions Tests ==============
        // TC01: Ray is outside the sphere and does not intersect
        Sphere sphere = new Sphere(1, new Point(0, 0, 0));
        Ray rayOutside = new Ray(new Point(2, 2, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayOutside), "Ray outside the sphere - no intersection expected");

        // TC02: Ray starts before the sphere and intersects it at 2 points
        Ray rayBefore = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        List<Point> result = sphere.findIntersections(rayBefore);
        assertEquals(2, result.size(), "2 points expected");
        assertEquals(List.of(new Point(-1, 0, 0), new Point(1, 0, 0)), result, "Incorrect intersection points");

        // TC03: Ray starts inside the sphere and intersects at 1 point
        Ray rayInside = new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0));
        result = sphere.findIntersections(rayInside);
        assertEquals(1, result.size(), "1 point expected");
        assertEquals(List.of(new Point(1, 0, 0)), result, "Incorrect intersection point");

        // TC04: Ray starts after the sphere - no intersection
        Ray rayAfter = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayAfter), "Ray after the sphere - no intersection expected");
        //=========== Boundary Values Tests ==============
        // Group: Ray crosses sphere (but not through center)

        // TC05: Ray starts at surface and goes inside → 1 point
        Ray rayAtSurface = new Ray(new Point(1, 0, 0), new Vector(-1, 0, 0));
        result = sphere.findIntersections(rayAtSurface);
        assertEquals(1, result.size(), "1 point expected");
        assertEquals(List.of(new Point(-1, 0, 0)), result, "Incorrect intersection point");

        // TC06: Ray starts at surface and goes outside → 0 points
        Ray rayAtSurfaceOutside = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayAtSurfaceOutside), "Ray at surface going outside - no intersection expected");
        // Group: Ray crosses sphere through center

        // TC07: Ray starts before the sphere and goes through center → 2 points
        Ray rayThroughCenter = new Ray(new Point(-2, 0, 0), new Vector(2, 0, 0));
        result = sphere.findIntersections(rayThroughCenter);
        assertEquals(2, result.size(), "2 points expected");
        assertEquals(List.of(new Point(-1, 0, 0), new Point(1, 0, 0)), result, "Incorrect intersection points");

        // TC08: Ray starts at center and goes outside → 1 point
        Ray rayAtCenter = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        result = sphere.findIntersections(rayAtCenter);
        assertEquals(1, result.size(), "1 point expected");
        assertEquals(List.of(new Point(1, 0, 0)), result, "Incorrect intersection point");

        //TC09: Ray starts at surface and goes through center → 1 point
        Ray rayAtSurfaceThroughCenter = new Ray(new Point(1, 0, 0), new Vector(-1, 0, 0));
        result = sphere.findIntersections(rayAtSurfaceThroughCenter);
        assertEquals(1, result.size(), "1 point expected");
        assertEquals(List.of(new Point(-1, 0, 0)), result, "Incorrect intersection point");

        // TC10: Ray starts at inside the sphere and goes through center → 1 point
        Ray rayInsideThroughCenter = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        result = sphere.findIntersections(rayInsideThroughCenter);
        assertEquals(1, result.size(), "1 point expected");
        assertEquals(List.of(new Point(1, 0, 0)), result, "Incorrect intersection point");

        //TC11:Ray starts after sphere (same line through center) → no intersection
        Ray rayAfterThroughCenter = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayAfterThroughCenter), "Ray starts after sphere on center-line - no intersection expected");

        // TC12: Ray starts at surface and goes outside (same line through center) → no intersection
        Ray rayAtSurfaceAfter = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayAtSurfaceAfter), "Ray at surface going outside on center-line - no intersection expected");

        // TC13: Ray starts inside sphere and goes out without going through the center (same line through center) → 1 point
        Ray rayInsideOut = new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0));
        result = sphere.findIntersections(rayInsideOut);
        assertEquals(1, result.size(), "1 point expected");
        assertEquals(List.of(new Point(1, 0, 0)), result, "Incorrect intersection point");
        // Group: Ray tangent to sphere

        // TC14: Ray is tangent to the sphere → no intersection
        Ray rayTangent = new Ray(new Point(0, 1, -1), new Vector(0, 0, 1));
        assertNull(sphere.findIntersections(rayTangent), "Ray tangent to sphere - no intersection expected");

        // TC15: Ray starts at point of tangency and goes away → no intersection
        Ray rayTangentAtPoint = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayTangentAtPoint), "Ray starts at point of tangency and goes away - no intersection expected");

        // TC16: Ray starts at the line of tangency and goes away → no intersection
        Ray rayTangentAway = new Ray(new Point(0, 1, 0), new Vector(0, 1, 0));
        assertNull(sphere.findIntersections(rayTangentAway), "Ray starts at the line of tangency and goes away - no intersection expected");
        // Group: Ray is orthogonal to vector from center to ray origin

        // TC17: Ray orthogonal and outside sphere → no intersection
        Ray rayOrthogonalOutside = new Ray(new Point(0, 2, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(rayOrthogonalOutside), "Ray orthogonal and outside sphere - no intersection expected");

        // TC18: Ray orthogonal and grazes the sphere( center lies on the normal) → no intersection
        Ray rayOrthogonalGrazes = new Ray(new Point(0, 1, 0), new Vector(0, 0, 1));
        assertNull(sphere.findIntersections(rayOrthogonalGrazes), "Ray orthogonal and grazes the sphere - no intersection expected");
    }
}