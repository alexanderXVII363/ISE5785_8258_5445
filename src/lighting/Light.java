package lighting;

import primitives.Color;
/**
 * Abstract class representing a light source in a 3D scene.
 * It contains the intensity of the light.
 */
abstract class Light {
    /**
     * The intensity of the light source.
     * This is a color that represents the brightness and color of the light.
     */
    protected final Color intensity;
    /**
     * Constructor for the Light class.
     * @param intensity The color intensity of the light source.
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }
    /**
     * Gets the intensity of the light source.
     * @return The color intensity of the light.
     */
    public Color getIntensity() {
        return intensity;
    }
}
