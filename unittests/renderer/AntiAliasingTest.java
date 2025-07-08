package renderer;

import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.Scene;
import primitives.Color;

public class AntiAliasingTest {
    @Test
    void mostBasicTest(){
        Scene scene =  new Scene("TestScene");
        scene.geometries.add(
                new Sphere(new Point(0,0,2),2)
        );
        scene.setBackground(new Color(255,255,255));
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene,RayTracerType.SIMPLE)
                .setLocation(new Point(0,0,30))
                .setVpDistance(1000).setVpSize(150,150)
                .setResolution(2000,2000)
                .setAdaptiveSuperSampling(4)
                .setAntiAliasing(81)
                .setDirection(new Point(0,0,0), Vector.AXIS_Y);

        builder.build().renderImage().writeToImage("antiAliasingTestASS");
    }
}
