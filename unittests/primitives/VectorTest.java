package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    /**
     * Delta value for accuracy when comparing double values in assertEquals
     */
    private static final double DELTA = 0.000001;



    /** Test method for constructor from three doubles {@link primitives.Vector#Vector(double, double, double)}. **/
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the constructor with valid values
        Vector v = new Vector(1, 2, 3);
        assertEquals(new Double3(1, 2, 3), v.xyz, "ERROR: Vector constructor with parameters is not working correctly");
        // ============ Boundary Values Tests ==============
        // TC02: Test the constructor with zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "ERROR: Vector constructor with zero vector did not throw an exception");
    }

    /** Test method for constructor from Double3 {@link primitives.Vector#Vector(Double3)}. **/
    @Test
    void testConstructorDouble3() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the constructor with valid values
        Vector v = new Vector(new Double3(1, 2, 3));
        assertEquals(new Double3(1, 2, 3), v.xyz, "ERROR: Vector constructor with parameters is not working correctly");
        // ============ Boundary Values Tests ==============
        // TC02: Test the constructor with zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "ERROR: Vector constructor with zero vector did not throw an exception");
    }
    /** Test method for LengthSquared {@link Vector#lengthSquared()}. **/
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the length squared of a vector
        Vector v = new Vector(1, 2, 3);
        assertEquals(14, v.lengthSquared(), "ERROR: Length squared is not correct");
        }

    /** test method for Length {@link Vector#length()}.**/
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the length squared of a vector
        Vector v = new Vector(1, 2, 3);
        assertEquals(Math.sqrt(14), v.length(), "ERROR: Length squared is not correct");

    }

    /** Test method for constructor from Double3 {@link primitives.Vector#add(Vector)}. **/
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the addition of two vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        assertEquals(new Vector(5, 7, 9), v1.add(v2), "ERROR: Addition of two vectors is not correct");
        // ============ Boundary Values Tests ==============
        // TC02: Test the addition of inverse vectors
        Vector v3 = new Vector(-1, -2, -3);
        assertThrows(IllegalArgumentException.class, () -> v1.add(v3), "ERROR: Addition of inverse vectors did not throw an exception");
    }

    /** Test method for scale {@link primitives.Vector#scale(double)}. **/
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the scaling of a vector
        Vector v = new Vector(1, 2, 3);
        assertEquals(new Vector(2, 4, 6), v.scale(2), "ERROR: Scaling of a vector is not correct");
        // ============ Boundary Values Tests ==============
        // TC02: Test the scaling of a zero vector
        assertThrows(IllegalArgumentException.class, () -> v.scale(0), "ERROR: Scaling of a zero vector did not throw an exception");
    }

    /** Test method for dot product {@link primitives.Vector#dotProduct(Vector)}. **/
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, -5, 6);
        Vector unit = new Vector(0, 0, 1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Regular dot product computation add one with obtuse angle and acute angle
        assertEquals(12, v1.dotProduct(v2), DELTA, "dotProduct() wrong result");

        // ============ Boundary Values Tests ==================
        // TC02: Orthogonal vectors should give zero
        assertEquals(0, v1.dotProduct(new Vector(-2, 1, 0)), DELTA, "dotProduct() of orthogonal vectors must be zero");
        //TC03: Parallel vectors should give positive value (if they share the same direction)
        assertEquals(28, v1.dotProduct(new Vector(2, 4, 6)), DELTA, "dotProduct() of parallel vectors must be positive");
        // TC04: Parallel vectors should give negative value (if they are in opposite direction)
        assertEquals(-28, v1.dotProduct(new Vector(-2, -4, -6)), DELTA, "dotProduct() of parallel vectors must be negative");
        // TC05: Unit Vector dot product with another unit vector should be the cos() of the angle between them
        assertEquals(0, unit.dotProduct(new Vector(1, 0, 0)), DELTA, "dotProduct() of unit vectors must be cos(angle)");
    }
    /** Test method for cross product {@link primitives.Vector#crossProduct(Vector)}. **/
    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the cross product of two vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        assertEquals(new Vector(-3, 6, -3), v1.crossProduct(v2), "ERROR: Cross product of two vectors is not correct");
        // ============ Boundary Values Tests ==============
        // TC02: Cross product of parallel vectors should throw an exception
        Vector v3 = new Vector(2, 4, 6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3), "ERROR: Cross product of parallel vectors did not throw an exception");
        // TC03: Cross product with itself should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1), "ERROR: Cross product with itself did not throw an exception");
        // TC04: Cross product 180 degrees should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(-1, -2, -3)), "ERROR: Cross product 180 degrees did not throw an exception");
        // TC05: Cross product with 0 degrees should throw an exception
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(1, 2, 3)), "ERROR: Cross product with 0 degrees did not throw an exception");
    }
    /** Test method for normalize {@link primitives.Vector#normalize()}. **/
    @Test
    void normalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test the normalization of a vector
        Vector v = new Vector(3, 4, 0);
        assertEquals(new Vector(0.6, 0.8, 0), v.normalize(), "ERROR: Normalization of a vector is not correct");
        // ============ Boundary Values Tests ==============
        // TC02: Normalization should keep the direction of the vector
        Vector v2 = new Vector(1, 2, 3);
        Vector v3 = new Vector(2, 4, 6);
        assertEquals(1, v2.normalize().dotProduct(v3.normalize()), "ERROR: Normalization should keep the direction of the vector");
    }
}