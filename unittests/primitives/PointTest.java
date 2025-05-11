package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;
    /** Test method for {@link primitives.Point#subtract(Point)}. */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtracting two different points
        Point p1 = new Point(4, 5, 6);
        Point p2 = new Point(1, 2, 3);
        assertEquals(new Vector(3, 3, 3), p1.subtract(p2), "subtract() wrong result");

        // =============== Boundary Values Tests ==================
        // TC10: Subtracting a point from itself should throw an exception
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "subtract() from itself must throw exception");
    }

    /** Test method for {@link primitives.Point#add(Vector)}. */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Adding a vector to a point
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(3, 3, 3);
        assertEquals(new Point(4, 5, 6), p.add(v), "add() wrong result");
    }
    /** Test method for {@link primitives.Point#distanceSquared(Point)}. */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance squared between two different points
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 3);
        assertEquals(25.0, p1.distanceSquared(p2), DELTA, "distanceSquared() wrong result");
        // =============== Boundary Values Tests ==================
        // TC10: Distance to itself should be zero
        assertEquals(0.0, p1.distanceSquared(p1), DELTA, "distanceSquared() to same point is not zero");
    }

    /** Test method for {@link primitives.Point#distance(Point)}. */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Distance between two different points
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(4, 6, 3);
        assertEquals(5.0, p1.distance(p2), DELTA, "distance() wrong result");

        // =============== Boundary Values Tests ==================
        // TC10: Distance to itself should be zero
        assertEquals(0.0, p1.distance(p1), DELTA, "distance() to same point is not zero");
}

}
/**
 * Unit tests for primitives.Point class
 * @author Yossi Cohen
 */
class PointTests {
    /**
     * Test method for
     */
    /** Test method for {@link primitives.Point#add(Vector)}. */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Adding a vector to a point
        Point p = new Point(1, 2, 3);
        Vector v = new Vector(3, 3, 3);
        assertEquals(new Point(4, 5, 6), p.add(v), "add() wrong result");
    }

}