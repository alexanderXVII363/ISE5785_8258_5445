package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;


/**
 * Integration tests for Camera class with geometric intersections.
 * Tests the integration between camera ray construction and ray-geometry intersection.
 * @author Student Name
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Helper method to test the integration between camera ray construction and geometry intersection.
     * Creates rays through all pixels of View Plane 3x3 (WxH 3x3),
     * calculates intersections with the geometry and checks if the total count matches the expected value.
     *
     * @param camera The camera to shoot rays from
     * @param geometry The geometry to test intersections with
     * @param expectedIntersections The expected number of intersections
     */
    private void testRayIntersections(Camera camera, Intersectable geometry, int expectedIntersections) {
        // Initialize intersection count to 0
        int intersectionCount = 0;

        // For each pixel in the 3x3 view plane
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Construct ray through the pixel
                var ray = camera.constructRay(3, 3, j, i);
                // Calculate intersections with the geometry
                var intersections = geometry.findIntersections(ray);
                // Add the number of intersections (or 0 if null)
                intersectionCount += (intersections == null ? 0 : intersections.size());
            }
        }
        // Assert that the total number of intersections matches expected
        assertEquals(expectedIntersections, intersectionCount, "Wrong number of intersections");
    }
    /**
     * Camera instance used for all the tests in this class.
     * Configured using the builder pattern to ensure proper initialization.

     * - Location: Set to the origin (0, 0, 0).
     * - Direction: View direction set to (0, 0, -1) and up vector set to (0, 1, 0).
     * - View Plane Distance: Set to 1.
     * - View Plane Size: Set to 3x3.
     */
    Camera camera = Camera.getBuilder()
            .setLocation( new Point(0,0,0.5)) // Set the camera's location to the origin
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)) // Set the view direction and up vector
            .setVpDistance(1) // Set the distance from the camera to the view plane
            .setVpSize(3, 3) // Set the size of the view plane
            .build(); // Build the camera instance
    /**
     * Test method for integration between camera rays and sphere intersection.
     * Tests a few cases with different sphere placements relative to camera.
     */
    @Test
    public void testCameraRaySphereIntersections() {
        // TC01: Small sphere in front of the view plane (2 intersections)
        Sphere sphere1 = new Sphere(1, new Point(0, 0, -3));
        testRayIntersections(camera, sphere1, 2);

        // TC02: Big sphere behind the view plane that all the rays go through (18 intersections)
        Sphere sphere2 = new Sphere(2.5, new Point(0, 0, -2.5));
        testRayIntersections(camera, sphere2, 18);

        // TC03: Medium size sphere intersects the view plane (10 intersections)
        Sphere sphere3 = new Sphere(2, new Point(0, 0, -2));
        testRayIntersections(camera, sphere3, 10);

        //TC04: Sphere in front of the view plane (9 intersections)
        testRayIntersections(camera, new Sphere(4, new Point(0, 0, -1)), 9);

        // TC05: Sphere behind the camera (0 intersections)
        Sphere sphere4 = new Sphere(0.2, new Point(0, 0, 1));
        testRayIntersections(camera, sphere4, 0);

        // TC06: Sphere in the view plane (0 intersections)
        Sphere sphere5 = new Sphere(0.5, new Point(2, 2, -4));
        testRayIntersections(camera, sphere5, 0);
    }
    /**
     * Test method for integration between camera rays and plane intersection.
     * Tests a few cases with different plane placements relative to camera.
     */
    @Test
    public void testCameraRayPlaneIntersections() {
        // TC01: Plane perpendicular to the view direction (9 intersections - all rays expected)
        Plane plane1 = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        testRayIntersections(camera, plane1, 9);

        // TC02: Plane with small angle to the view direction (9 intersections - all rays expected)
        Plane plane2 = new Plane(new Point(0, 0, -5), new Vector(0, -0.5, 1)); // acute angle
        testRayIntersections(camera, plane2, 9);

        // TC03: Plane with steep angle - intersects with only some of the rays (6 intersections)
        Plane plane3 = new Plane(new Point(0, 0, -5), new Vector(0, 10, 1));
        testRayIntersections(camera, plane3, 6);
    }
    /**
     * Test method for integration between camera rays and triangle intersection.
     * Tests a few cases with different triangle placements relative to camera.
     */
    @Test
    public void testCameraRayTriangleIntersections() {

        // TC01: Small triangle in front of the view plane (1 intersection)
        Triangle triangle1 = new Triangle(new Point(0, 1, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2));
        testRayIntersections(camera, triangle1, 1);

        // TC02: Triangle covering the center of the view plane (2 intersections)
        Triangle triangle2 = new Triangle(new Point(0, 20, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2));
        testRayIntersections(camera, triangle2, 2);
    }
}