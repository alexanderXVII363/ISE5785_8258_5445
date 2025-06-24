package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;
import geometries.Intersectable. Intersection;

public class SimpleRayTracer extends RayTracerBase {
    private static final double DELTA = 0.1;


    private boolean unshaded(Intersection intersection) {
        Point point = intersection.point;
        LightSource lightSource = intersection.lightSource;
        Vector lightDir = lightSource.getL(point);
        double lightDistance = lightSource.getDistance(point);

        Ray shadowRay = new Ray(point.add(intersection.n.scale(intersection.nl<0?DELTA:-DELTA)), lightDir.scale(-1));

        List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay, lightDistance);

        if (intersections == null) return true;

        for (Intersection i : intersections) {
            // Ignore the geometry the point is already on
            if (!i.geometry.equals(intersection.geometry)) {
                return false; // There is a blocking object between point and light
            }
        }

        return true; // No blockers found
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
        Color localEffects = calcColorLocalEffects(intersection);
        // Return the total color at the intersection point
        return ambientLight.add(localEffects);

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
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Intersection closestIntersection = ray.findClosestIntersection(intersections);
        if (closestIntersection == null) {
            return scene.background;
        }
        return calcColor((closestIntersection),ray);
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

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource))
                continue;

            if (!unshaded(intersection))
                continue;

            // Light intensity at the point
            Color iL = lightSource.getIntensity(intersection.point);

            // Compute diffuse and specular components
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);

            // Add both contributions scaled by light intensity
            color = color.add(iL.scale(diffusive)).add(iL.scale(specular));
        }

        return color;
    }


}
