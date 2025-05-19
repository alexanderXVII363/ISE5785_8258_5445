package renderer;

import primitives.*;
import primitives.Util;
import primitives.Vector;
import primitives.Ray;
import scene.Scene;

import java.util.MissingResourceException;

public class Camera implements Cloneable {
    private Point p0 = new Point(0, 0, 0);         // Camera position
    private Vector vTo = new Vector(0, 0, -1);     // Forward direction
    private Vector vUp = new Vector(0, 1, 0);      // Up direction
    private Vector vRight = new Vector(1, 0, 0);   // Right direction
    private double width = 0.0;                    // View plane width
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
    public void castRay(int x, int y){
        imageWriter.writePixel(x, y, rayTracer.traceRay(constructRay(nX, nY, x, y)));
    }

    /**
     * Constructs a ray from the camera through the pixel (j, i) on the view plane.
     *
     * @param nX number of horizontal pixels
     * @param nY number of vertical pixels
     * @param j  column index of the pixel (0-based)
     * @param i  row index of the pixel (0-based)
     * @return Ray from camera through the pixel
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
    @Override
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
        for(int i=0;i<nY;i++){
            for(int j=0;j<nX;j++){
                castRay(j,i);
            }
        }
        return this;
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
