package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Abstract class representing a ray tracer.
 * It contains the scene to be rendered and an abstract method to trace rays.
 */
public abstract class RayTracerBase {
    /**
     * The scene to be rendered
     */
    protected final Scene scene;

    /**
     * Constructor to initialize the scene
     * @param scene the scene to be rendered
     */
    protected RayTracerBase(Scene scene) {
        this.scene = scene;
    }
    /**
     * Method to trace the rays
     * @param ray the ray to be traced
     * @return the color of the ray
     */
    public abstract Color traceRay(Ray ray);
    public abstract Color traceBeam(List<Ray> rays);
}
