package geometries;

/**
 * Abstract class representing a radial geometry in three-dimensional space.
 * Radial geometries are characterized by a radius value.
 * The class inherits from Geometry class.
 */
abstract public class RadialGeometry extends Geometry {
    /**
     * The radius of the radial geometry.
     */
    protected final double radius;

    /**
     * Constructs a new RadialGeometry with the specified radius.
     *
     * @param _radius The radius of the radial geometry.
     */
    public RadialGeometry(double _radius){
        this.radius = _radius;
    }
}