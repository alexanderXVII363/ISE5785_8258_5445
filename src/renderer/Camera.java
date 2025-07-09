package renderer;

import primitives.*;
import primitives.Util;
import primitives.Vector;
import primitives.Ray;
import scene.Scene;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

/** * The Camera class represents a camera in a 3D scene.
 * It allows setting the camera position, direction, view plane size, and distance to the view plane.
 * It can render images by tracing rays through each pixel and coloring them.
 */
public class Camera implements Cloneable {
    /** * The camera's position, forward direction, up direction, right direction,
     * view plane width, height, distance to the view plane, image writer, ray tracer,
     * and resolution (number of horizontal and vertical pixels).
     */
    private Point p0 = new Point(0, 0, 0);         // Camera position
    int antiAliasingRaysNum = 0;
    int adaptive_super_sampling=0;// Number of rays for anti-aliasing
    /**
     * The forward direction vector (vTo) is the direction the camera is looking at.
     * The up direction vector (vUp) is the direction that is considered "up" for the camera.
     * The right direction vector (vRight) is perpendicular to both vTo and vUp.
     */
    private Vector vTo = new Vector(0, 0, -1);     // Forward direction
    private static final int SPARE_THREADS = 2;
    private double printInterval = 0;
    private PixelManager pixelManager;
    private int threadsCount = 0;
    /**
     * The up direction vector (vUp) is the direction that is considered "up" for the camera.
     * The right direction vector (vRight) is perpendicular to both vTo and vUp.
     */
    private Vector vUp = new Vector(0, 1, 0);      // Up direction
    /**
     * The right direction vector (vRight) is perpendicular to both vTo and vUp.
     * It is used to calculate the position of pixels on the view plane.
     */
    private Vector vRight = new Vector(1, 0, 0);   // Right direction
    /**
     * The view plane width and height define the size of the area that the camera can see.
     * The distance to the view plane is the distance from the camera position to the view plane.
     */
    private double width = 0.0;                    // View plane width
    /**
     * The view plane height defines the vertical size of the area that the camera can see.
     * The distance to the view plane is the distance from the camera position to the view plane.
     */
    private double height = 0.0;                   // View plane height

    private double distance = 0.0;// Distance to view plane
    private ImageWriter imageWriter = null; // Image writer for rendering
    private RayTracerBase rayTracer = null; // Ray tracer for rendering
    private int nX = 1; // Number of horizontal pixels
    private int nY = 1; // Number of vertical pixels

    // Private default constructor
    private Camera() {}
    // Static method to get Builder object (implementation to be added later)
    public static Builder getBuilder() {
        return new Builder();
    }
    /**
     * Performs adaptive super sampling for a specific pixel area.
     * Sends rays through the four corners of the given pixel region.
     * If all four corner colors are equal, returns that color.
     * Otherwise, recursively subdivides the region and averages the resulting colors.
     * Limits recursion depth to avoid infinite subdivision.
     *
     * @param i      pixel row index (0-based from top)
     * @param j      pixel column index (0-based from left)
     * @param depth  current recursion depth
     * @param minX   minimum X coordinate within the pixel
     * @param maxX   maximum X coordinate within the pixel
     * @param minY   minimum Y coordinate within the pixel
     * @param maxY   maximum Y coordinate within the pixel
     * @return the averaged color of the region
     */
    private Color adaptiveSuperSampling(int i, int j, int depth,
                                        double minX, double maxX, double minY, double maxY) {

        // Send ray to top-left corner
        Ray rayTL = constructRay(nX, nY, i, j, minX, minY);
        Color cTL = rayTracer.traceRay(rayTL);
        // Base case: maximum recursion depth reached – return top-left corner color
        if (depth >= adaptive_super_sampling) {
            return cTL;
        }


        // Check top-right only if needed
        Ray rayTR = constructRay(nX, nY, i, j, maxX, minY);
        Color cTR = rayTracer.traceRay(rayTR);
        if (!cTR.equals(cTL)) {
            return recurseAll(i, j, depth, minX, maxX, minY, maxY);
        }

        // Check bottom-left only if still equal
        Ray rayBL = constructRay(nX, nY, i, j, minX, maxY);
        Color cBL = rayTracer.traceRay(rayBL);
        if (!cBL.equals(cTL)) {
            return recurseAll(i, j, depth, minX, maxX, minY, maxY);
        }

        // Check bottom-right only if still equal
        Ray rayBR = constructRay(nX, nY, i, j, maxX, maxY);
        Color cBR = rayTracer.traceRay(rayBR);
        if (!cBR.equals(cTL)) {
            return recurseAll(i, j, depth, minX, maxX, minY, maxY);
        }

        // All corners are equal
        return cTL;
    }

    /**
     * Recursively subdivides the current pixel region into 4 quadrants and averages their colors.
     *
     * @param i      pixel row index
     * @param j      pixel column index
     * @param depth  current recursion depth
     * @param minX   minimum X coordinate of the region
     * @param maxX   maximum X coordinate of the region
     * @param minY   minimum Y coordinate of the region
     * @param maxY   maximum Y coordinate of the region
     * @return averaged color from the 4 subregions
     */
    private Color recurseAll(int i, int j, int depth,
                             double minX, double maxX, double minY, double maxY) {
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        Color r1 = adaptiveSuperSampling(i, j, depth + 1, minX, midX, minY, midY); // top-left
        Color r2 = adaptiveSuperSampling(i, j, depth + 1, midX, maxX, minY, midY); // top-right
        Color r3 = adaptiveSuperSampling(i, j, depth + 1, minX, midX, midY, maxY); // bottom-left
        Color r4 = adaptiveSuperSampling(i, j, depth + 1, midX, maxX, midY, maxY); // bottom-right

        return r1.add(r2, r3, r4).reduce(4);
    }
    public void castRay(int x, int y){
        if(adaptive_super_sampling!=0){
            imageWriter.writePixel(x, y, adaptiveSuperSampling(x, y, 0, -0.5, 0.5, -0.5, 0.5));
            pixelManager.pixelDone();
        }
        else if(antiAliasingRaysNum<2) {
            // If anti-aliasing is not enabled, trace a single ray for the pixel
            imageWriter.writePixel(x, y, rayTracer.traceRay(constructRay(nX, nY, x, y)));
            pixelManager.pixelDone();
            return;
        }
        else {
            Point p = p0.add(vTo.scale(distance)); // Start point of the ray
            double yI = -(y - (nY - 1) / 2d) * height / nY;
            double xJ = (x - (nX - 1) / 2d) * width / nX;

            //check if xJ or yI are not zero, so we will not add zero vector
            if (!Util.isZero(xJ)) p = p.add(vRight.scale(xJ));
            if (!Util.isZero(yI)) p = p.add(vUp.scale(yI));

            imageWriter.writePixel(x, y, rayTracer.traceBeam(new Blackboard(height/(double)nY,width/(double)nX,p,vUp,vRight,antiAliasingRaysNum).getRays(p0)));
            pixelManager.pixelDone();
        }
    }
    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX number of columns (horizontal resolution)
     * @param nY number of rows (vertical resolution)
     * @param j  pixel column index (0-based from left)
     * @param i  pixel row index (0-based from top)
     * @return a {@link Ray} from the camera through the specified pixel
     */

    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pIJ = p0;
        double yI = -(i - (nY - 1) / 2d) * height / nY;
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        //check if xJ or yI are not zero, so we will not add zero vector
        if (!Util.isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        // we need to move the point in the direction of vTo by distance
        pIJ = pIJ.add(vTo.scale(distance));

        return new Ray(p0, pIJ.subtract(p0).normalize());
    }
    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }

    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }


    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX number of columns (horizontal resolution)
     * @param nY number of rows (vertical resolution)
     * @param j  pixel column index (0-based from left)
     * @param i  pixel row index (0-based from top)
     * @param xOff offset in the X direction for anti-aliasing and adaptive super sampling
     * @param yOff offset in the Y direction for anti-aliasing and adaptive super sampling
     * @return a {@link Ray} from the camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i, double xOff, double yOff){
        Point pij = p0.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double xJ = (j + xOff - (nX - 1) / 2.0) * rX;
        double yI = -(i + yOff - (nY - 1) / 2.0) * rY;
        xJ = xJ + Util.random(-0.5, 0.5) * rX;
        yI = yI + Util.random(-0.5, 0.5) * rY;
        if (!Util.isZero(xJ))
            pij = pij.add(vRight.scale(xJ));
        if (!Util.isZero(yI))
            pij = pij.add(vUp.scale(yI));
        return new Ray(p0, pij.subtract(p0));
}

    @Override
    /**
     * Clones the Camera object.
     * This method is used to create a copy of the Camera object.
     *
     * @return a clone of the Camera object
     */
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }

    }
    /**
     * Renders the image by tracing rays through each pixel and coloring them.
     *
     * @return this camera object
     */
    public Camera renderImage() {
//        for(int i=0;i<nY;i++){
//            for(int j=0;j<nX;j++){
//                castRay(j,i);
//            }
//        }
//        return this;
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }
    /**
     * Prints a grid on the image.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return this camera object
     */
    public Camera printGrid(int interval, Color color){
        for (int i = 0; i < nY; i+=interval) {
            for (int j = 0; j < nX; j++) {
                    imageWriter.writePixel(j, i, color);
            }
        }
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j+=interval) {
                imageWriter.writePixel(j, i, color);
            }
        }
        return this;
    }
    /**
     * Writes the image to a file with the specified name.
     *
     * @param name the name of the file (without extension)
     * @return this camera object
     */
    public Camera writeToImage(String name) {
        imageWriter.writeToImage(name);
        return this;
    }

    public static class Builder {
        private final Camera camera = new Camera();


        public Builder setMultithreading(int threads){
            if (threads<=-3){
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            }
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else {
                camera.threadsCount = threads;
            }
            return this;
        }
        public Builder setDebugPrint(double printInterval) {
            if (printInterval < 0) {
                throw new IllegalArgumentException("interval parameter must be non-negative");
            }
            camera.printInterval = printInterval;
            return this;
        }
        /**
         * Sets anti-aliasing ray's number.
         * @param antiAliasingNum the number of rays for anti-aliasing
         * @return this for concatenation
         */
        public Builder setAntiAliasing(int antiAliasingNum) {
            camera.antiAliasingRaysNum = antiAliasingNum;
            return this;
        }
        /**
         * Sets the camera position.
         *
         * @param p0 the Camera position
         * @return This Builder object
         * @throws IllegalArgumentException if p0 is null
         */
        public Builder setLocation(Point p0) {
            if (p0 == null) {
                throw new IllegalArgumentException("Camera position cannot be null");
            }
            camera.p0 = p0;
            return this;
        }
        // setTracerRay receives Object of scene and RayTracerType and updates rayTracer
        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene to be used by the ray tracer
         * @param t     the type of ray tracer to use
         * @return this builder
         * @throws IllegalArgumentException if scene or t is null
         */
        public Builder setRayTracer(Scene scene, RayTracerType t) {
            if (scene == null || t == null) {
                throw new IllegalArgumentException("Scene and RayTracer cannot be null");
            }
            if( t == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            }
            return this;
        }


        /**
         * Sets the direction of the camera using forward and up vectors.
         *
         * @param vTo forward vector
         * @param vUp up vector
         * @return this builder
         * @throws IllegalArgumentException if vectors are null or not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Vectors cannot be null");
            }
            if (!Util.isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("Vectors must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vRight = vTo.crossProduct(vUp).normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and approximate up vector.
         *
         * @param target   the point the camera looks at
         * @param upApprox approximate up vector
         * @return this builder
         */
        public Builder setDirection(Point target, Vector upApprox) {
            if (camera.p0 == null) {
                throw new IllegalArgumentException("Camera position cannot be null");
            }
            if (target == null || upApprox == null) {
                throw new IllegalArgumentException("Target point and up vector cannot be null");
            }

            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(upApprox).normalize();

            // Orthogonalize upApprox with respect to vTo
            camera.vUp = camera.vRight.crossProduct(camera.vTo);


            return this;
        }


        /**
         * Sets the direction using only the target point (up vector assumed to be Y-axis).
         *
         * @param target the point the camera looks at
         * @return this builder
         */
        public Builder setDirection(Point target) {
            return setDirection(target, Vector.AXIS_Y);
        }

        /**
         * Sets the view plane size.
         *
         * @param width  the view plane width
         * @param height the view plane height
         * @return this builder
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }
        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance to the view plane
         * @return this builder
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }
        public Builder setResolution(int nX, int nY){
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("Resolution must be positive");
            }
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }
        public Builder setAdaptiveSuperSampling(int adaptive_super_sampling){
            if (adaptive_super_sampling < 0) {
                throw new IllegalArgumentException("Adaptive super sampling must be non-negative");
            }
            camera.adaptive_super_sampling = adaptive_super_sampling;
            return this;
        }
        /**
         * Builds the final Camera object.
         * Validates all required fields.
         * Calculates missing vectors and sets them.
         * Returns a cloned and finalized Camera object.
         *
         * @return a cloned and finalized Camera object
         * @throws MissingResourceException if any required value is missing
         * @throws IllegalArgumentException if any value is invalid
         */
        public Camera build() {
            if (camera.p0 == null) {
                throw new MissingResourceException("Camera position is missing", "Camera", "p0");
            }
            if (camera.vTo == null) {
                throw new MissingResourceException("Forward vector is missing", "Camera", "vTo");
            }
            if (camera.vUp == null) {
                throw new MissingResourceException("Up vector is missing", "Camera", "vUp");
            }
            if (camera.height == 0) {
                throw new MissingResourceException("View plane height is missing", "Camera", "height");
            }
            if (camera.width <= 0 || camera.height <= 0) {
                throw new MissingResourceException("View plane size is missing", "Camera", "width/height");
            }
            if (camera.distance <= 0) {
                throw new MissingResourceException("View plane distance is missing", "Camera", "distance");
            }
            // 1e-10 is a small value to check for orthogonality of vectors vTo and vUp
            if (Math.abs (camera.vTo.dotProduct(camera.vUp)) > 1e-10) {
                throw new IllegalArgumentException("Vectors vTo and vUp must be orthogonal");
            }
            camera .vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            if(camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException("Resolution must be positive");
            }
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
            if(camera.rayTracer==null) {
                camera.rayTracer = new SimpleRayTracer(null);
            }
            return (Camera)camera.clone();
        }
    }

}
