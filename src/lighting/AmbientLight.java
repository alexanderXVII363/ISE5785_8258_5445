package lighting;

import primitives.Color;

/**
 * Represents an ambient light source in a scene.
 * Ambient light is a non-directional light that illuminates all objects equally,
 * regardless of their position or orientation.
 */
public class AmbientLight extends Light {
    /**
     * A constant representing no ambient light (black).
     * This can be used to indicate the absence of ambient light in a scene.
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an AmbientLight with a specified intensity.
     *
     * @param intensity the color intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        super(intensity);
    }

}

