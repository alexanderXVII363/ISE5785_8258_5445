package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RayTest {
    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     * equivalence partition: 1. closest point is in the middle of the list
     * boundary value: 2. empty list, expected null
     * 3. the closest point is the first point in the list
     * 4. the closest point is the last point in the list
     */
    @Test
    public void testFindClosestPoint() {
        // Create a ray with a starting point and direction
        Point p0 = new Point(1, 2, 3);
        Vector dir = new Vector(0, 0, 1);
        Ray ray = new Ray(p0, dir);

        // Test case 1: closest point is in the middle of the list
        List<Point> points = List.of(new Point(1, 2, 6), new Point(1, 2, 4), new Point(1, 2, 8));
        Point expectedClosestPoint = new Point(1, 2, 4); // This is truly closest
        assertEquals(expectedClosestPoint, ray.findClosestPoint(points));

        // Test case 2: empty list
        points = List.of();
        assertNull(ray.findClosestPoint(points));

        // Test case 3: closest point is the first point in the list
        points = List.of(new Point(1, 2, 4), new Point(1, 2, 5), new Point(1, 2, 6));
        expectedClosestPoint = new Point(1, 2, 4);
        assertEquals(expectedClosestPoint, ray.findClosestPoint(points));

        // Test case 4: closest point is the last point in the list
        points = List.of(new Point(1, 2, 6), new Point(1, 2, 5), new Point(1, 2, 4));
        expectedClosestPoint = new Point(1, 2, 4);
        assertEquals(expectedClosestPoint, ray.findClosestPoint(points));
    }
}
