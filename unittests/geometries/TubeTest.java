package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

/** Testing Tubes*/
class TubeTest {

    /** Test method for {@link geometries.Tube#getNormal(primitives.Point)}. */
    @Test
    public void testGetNormal() {
        // Create a tube along the Y-axis
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        Tube tube = new Tube(1, axisRay);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test with a point on the tube's surface but not across from the ray head
        Point point1 = new Point(1, 1, 0);
        Vector expected1 = new Vector(1, 0, 0);
        assertEquals(expected1, tube.getNormal(point1), "Tube normal calculation is incorrect");

        // =============== Boundary Values Tests ==================
        // TC11: Test with a point directly across from the ray head (t = 0)
        Point point2 = new Point(0, 0, 1);
        Vector expected2 = new Vector(0, 0, 1);
        assertEquals(expected2, tube.getNormal(point2), "Tube normal calculation at boundary is incorrect");

        // Verify that all normals have length 1
        assertEquals(1, tube.getNormal(point1).length(), 0.00001, "Tube normal length is not 1");
        assertEquals(1, tube.getNormal(point2).length(), 0.00001, "Tube normal length at boundary is not 1");
    }
}