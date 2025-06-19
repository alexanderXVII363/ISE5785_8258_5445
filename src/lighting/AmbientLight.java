package lighting;

import primitives.Color;

public class AmbientLight extends Light {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);


    public AmbientLight(Color intensity) {
        super(intensity);
    }

}

