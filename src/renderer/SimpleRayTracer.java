package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable. Intersection;

public class SimpleRayTracer extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    private Double3 transparency(Intersection intersection){
        Double3 ktr = Double3.ONE;
        Point point = intersection.point;
        LightSource lightSource = intersection.lightSource;
        double lightDistance = lightSource.getDistance(point);

        Ray shadowRay = new Ray(point,intersection.l.scale(-1),intersection.n);

        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);

        if (intersections == null ) return ktr;

        for (Intersection i : intersections) {
            // Ignore the geometry the point is already on
            // Check if the transparency coefficient is less than the minimum threshold
            if (lightDistance*lightDistance>i.point.distanceSquared(intersection.point) || !i.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                ktr = ktr.product(i.material.kT);

                if(ktr.lowerThan(MIN_CALC_COLOR_K)){
                    return Double3.ZERO;
                }
            }
            // Otherwise, continue checking other intersections
        }

        return ktr;

    }
    /**
     * Checks if the intersection point is unshaded by any geometry.
     *
     * @param intersection the intersection to check
     * @return true if the point is unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection) {

        Point point = intersection.point;
        LightSource lightSource = intersection.lightSource;
        Vector lightDir = lightSource.getL(point);
        double lightDistance = lightSource.getDistance(point);

        Ray shadowRay = new Ray(point,intersection.l.scale(-1),intersection.n);

        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);

        if (intersections == null ) return true;

        for (Intersection i : intersections) {
            // Ignore the geometry the point is already on
                // Check if the transparency coefficient is less than the minimum threshold
                if (lightDistance*lightDistance>i.point.distanceSquared(intersection.point) || !i.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return false; // This object blocks enough light to cause a shadow
                }
                // Otherwise, continue checking other intersections
        }

        return true; // No blockers with low transparency found, so the point is unshaded
    }
    // This method calculates the color at a given point in the scene
    /**
     * Calculates the color at the intersection point based on ambient light and local effects.
     *
     * @param intersection the intersection point in the scene
     * @param ray          the ray that hit the geometry
     * @return the calculated color at the intersection point
     */

    public Color calcColor(Intersection intersection, Ray ray) {
        // If the intersection is not valid, return black
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }
        // Calculate the ambient light contribution
        Color ambientLight = scene.ambientLight.getIntensity().scale(intersection.material.kA);
        // Calculate the local effects from light sources
        Color recursiveColor = calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K);
        return ambientLight.add(recursiveColor);
    }
    // Constructor receives a scene and uses super to initialize the base class

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }
    // The traceRay method is overridden and throws UnsupportedOperationException
    /**
     * Traces a ray through the scene and returns the color at the intersection point.
     * If no intersection is found, returns the background color of the scene.
     *
     * @param ray the ray to trace
     * @return the color at the intersection point or the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        if (closestIntersection == null) {
            return scene.background;
        }
        return calcColor(closestIntersection, ray);
    }

    /**
     * Prepares intersection data for lighting calculations.
     * @param intersection the intersection to process
     * @param rayDirection the direction of the ray that hit the geometry
     * @return true if the intersection is valid for lighting calculations, false otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        if (intersection == null) {
            return false;
        }
        intersection.v = rayDirection;
        intersection.n = intersection.geometry.getNormal(intersection.point);
        intersection.nv = intersection.v.dotProduct(intersection.n);
        return !Util.isZero(intersection.nv);
    }
    /**
     * Sets the light source for the intersection and calculates the light vector and its dot product with the normal.
     * @param intersection the intersection to set the light source for
     * @param lightSource the light source to set
     * @return true if the intersection is valid for lighting calculations, false otherwise
     */
    public Boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.l = lightSource.getL(intersection.point);
        intersection.nl = intersection.l.dotProduct(intersection.n);
        return Util.alignZero(intersection.nv * intersection.nl) > 0;
    }
    /**
     * Calculates the specular component of the light at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the specular component scaled by the material's specular coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        // Calculate the reflection vector of l about the normal n
        Vector reflection = intersection.l.add(
                intersection.n.scale(-2 * intersection.nl)
        );
        // Calculate the cosine of the angle between view vector and reflection vector
        double cosAngle = Math.max(0, -intersection.v.dotProduct(reflection));
        // Calculate the specular highlight factor using the shininess exponent
        double specularFactor = Math.pow(cosAngle, intersection.material.nShininess);
        // Return the scaled specular color component
        return intersection.material.kS.scale(specularFactor);
    }
    /**
     * Calculates the diffusive component of the light at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the diffusive component scaled by the material's diffuse coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.nl));
    }
    /**
     * Calculates the local lighting effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection to evaluate
     * @return the resulting color from all local light sources
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission(); // Start with emission color
        Double3 ktr;
        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource))
                continue;

            ktr = transparency(intersection);

            // Light intensity at the point
            Color iL = lightSource.getIntensity(intersection.point);

            // Compute diffuse and specular components
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);

            // Add both contributions scaled by light intensity
            color = color.add(iL.scale(diffusive.add(specular)).scale(ktr));
        }

        return color;
    }

    /**
     * Calculates the color at the intersection point based on ambient light and local effects.
     * @param intersection the intersection point in the scene
     * @param level the recursion level for global effects (not used in this simple tracer)
     * @param k the attenuation factor for the color calculation
     * @return the calculated color at the intersection point
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        // Base case (for now just local effects, no global recursion)
         Color base = calcColorLocalEffects(intersection);
         return level == 1 ? base : base.add(calcGlobalEffects(intersection,level,k));

    }
    /**
     * Constructs a reflected ray from the given intersection point.
     *
     * @param intersection the intersection where the ray hits the surface
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector r = intersection.v.subtract(intersection.n.scale(2 * intersection.nv)); // Reflection direction
        return new Ray(intersection.point, r , intersection.n);
    }

    /**
     * Constructs a refracted (transparency) ray from the given intersection point.
     *
     * @param intersection the intersection where the ray hits the surface
     * @return the transparency ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v,intersection.n); // Same direction as incoming ray
    }
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {

        // Update attenuation by multiplying by this geometry's coefficient
        Double3 kkx = k.product(kx);
        // Stop recursion if level reached or attenuation too low
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }
        // Find the closest intersection point to the ray's origin
        Intersection closestIntersection = findClosestIntersection(ray);
        if (closestIntersection == null) {
            return scene.background.scale(kx);
        }

        // Recursive call: calculate color at the new intersection point
        return preprocessIntersection(closestIntersection,ray.getDirection())? calcColor(closestIntersection, level - 1, kkx).scale(kx):Color.BLACK;
    }
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        // Return sum of both global effects colors
        return calcGlobalEffect(constructReflectedRay(intersection), level, k, intersection.material.kR)
                .add(calcGlobalEffect(constructRefractedRay(intersection), level, k, intersection.material.kT));

    }
    private Intersection findClosestIntersection(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        return ray.findClosestIntersection(intersections);
    }

}
