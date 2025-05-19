package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {
    public Color calcColor(Point p){
        return scene.ambientLight.getIntensity();
    }
    // Constructor receives a scene and uses super to initialize the base class
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }
    // The traceRay method is overridden and throws UnsupportedOperationException
    @Override
    public Color traceRay(Ray ray){
        // This method is not implemented yet
        List<Point> l = scene.geometries.findIntersections(ray);
        if(l == null)
            return scene.background;
        Point p = ray.findClosestPoint(l);
        if(p==null)
            return scene.background;
        return calcColor(p);
    }




}
