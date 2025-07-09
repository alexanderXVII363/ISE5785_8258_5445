package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {
    public String name;
    public Color background = Color.BLACK;
    public AmbientLight ambientLight = AmbientLight.NONE;
    public Geometries geometries = new Geometries();
    public List<LightSource> lights = new LinkedList<>();

    //constructor that receives a name
    /**
     * Constructs a Scene with the specified name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }
    //setters
    /**
     * Sets the background color of the scene.
     *
     * @param background the background color to set
     * @return the current Scene instance for method chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }
    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light to set
     * @return the current Scene instance for method chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }
    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the geometries to set
     * @return the current Scene instance for method chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
    /**
     * Sets the list of light sources in the scene.
     *
     * @param lights the list of light sources to set
     * @return the current Scene instance for method chaining
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
