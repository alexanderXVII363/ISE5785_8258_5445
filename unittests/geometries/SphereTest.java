package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

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
}