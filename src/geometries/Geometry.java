package geometries;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a generic geometry in three-dimensional space.
 */
abstract public class Geometry extends Intersectable {
    /**
     * new field for emission color of the geometry.
     */
    protected Color emission = Color.BLACK;
    private Material material = new Material();

    /**
     * Abstract method to retrieve the normal vector to the geometry at a specified point on its surface.
     *
     * @param point_on_body The point on the surface of the geometry for which to find the normal vector.
     * @return The normal vector to the geometry at the specified point.
     */
    abstract public Vector getNormal(Point point_on_body);
    public Color getEmission() {
        return emission;
    }
    /**
     * Sets the emission color of the geometry.
     *
     * @param emission The new emission color to set.
     * @return The current Geometry instance for method chaining.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
    public Material getMaterial() {
        return material;
    }
    /**
     * Sets the material properties of the geometry.
     *
     * @param material The Material object containing the properties to set.
     * @return The current Geometry instance for method chaining.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}