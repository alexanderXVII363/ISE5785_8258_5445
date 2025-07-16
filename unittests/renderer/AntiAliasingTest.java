package renderer;

import geometries.*;
import lighting.LightSource;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;
import primitives.Color;

import java.util.List;

import static java.awt.Color.*;

public class AntiAliasingTest {

    @Test
    void advanceTest(){
        Scene scene =  new Scene("TestScene2");
        Color snowman = new Color(220, 220, 220);
        Color eyes = new Color(BLUE);
        double distance = 2.5;
        scene.geometries.add(
               /*floor*/ new Plane (new Point(0,0,0), Vector.AXIS_Z).setEmission(new Color(BLACK)).setMaterial(new Material().setKs(0.1).setKd(0.1).setKr(0.3)),
                /*big ball*/ new Sphere(new Point(0,0,0.45),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*middle ball*/ new Sphere(new Point(0,0,1.23),0.27).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*top ball*/ new Sphere(new Point(0,0,1.62),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*nose*/ new Triangle(new Point(0.17,0,1.72),new Point(0.17,0,1.58),new Point(0.5,0,1.65)).setEmission(new Color(ORANGE)),
                /*left hand*/ new Triangle(new Point(0,0.2,1.2),new Point(0,0.2,1.3),new Point(0,1,1.4)).setEmission(new Color(150,75,0)),
                /*right hand*/ new Triangle(new Point(0,-0.2,1.2),new Point(0,-0.2,1.3),new Point(0,-1,1.4)).setEmission(new Color(150,75,0)),
                /*left eye*/ new Sphere(new Point(0.15,0.1,1.72),0.02).setEmission(eyes),
                /*right eye*/ new Sphere(new Point(0.13,-0.1,1.72),0.02).setEmission(eyes),

                /* SM2 big ball*/ new Sphere(new Point(0,distance+0.2,1.75),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 middle ball*/ new Sphere(new Point(0,distance+0.1,0.95),0.30).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 small ball*/ new Sphere(new Point(0,distance,0.52),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2  nose*/ new Triangle(new Point(0.17,distance,0.43),new Point(0.17,distance,0.57),new Point(0.5,distance,0.51)).setEmission(new Color(ORANGE)),
                /*SM2 left top hand*/    new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.25,0.95)).setEmission(new Color(150,75,0)),
                /*SM2 left bottom hand*/ new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.3,0)).setEmission(new Color(150,75,0)),
                /*SM2 right hand*/ new Triangle(new Point(0,distance+0.1-0.25,0.9),new Point(0,distance+0.1-0.25,1),new Point(0,distance+0.1-0.3-0.7,0.4)).setEmission(new Color(150,75,0)),
                /*SM2 left eye*/ new Sphere(new Point(0.13,distance+0.1,0.42),0.02).setEmission(eyes),
                /*SM2 right eye*/ new Sphere(new Point(0.13,distance-0.1,0.42),0.02).setEmission(eyes)

        );
        scene.lights = List.of(
               /* light from floor towards SM1 */ new SpotLight(new Color(255,255,255),new Point(1,0,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* light from floor towards SM2 */ new SpotLight(new Color(255,255,255),new Point(1,distance,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* point light on top of the scene*/ new PointLight(new Color(255,255,255),new Point(0,0,30)).setKl(0.00005).setKq(0.00005),
                /* sunlight */ new DirectionalLight(new Color(YELLOW), new Vector(0,-1,-1)),
               /*spotlight onto the middle of the reflections*/ new SpotLight(new Color(255,255,255),new Point(2,distance,0.7), new Vector(0,0,-1)).setKl(0.005).setKq(0.005)
        );
        scene.setBackground(new Color(255,255,255));
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene,RayTracerType.SIMPLE)
                .setLocation(new Point(30,15,10))
                .setVpDistance(1000).setVpSize(150,150)
                .setResolution(1000,1000)
                .setAdaptiveSuperSampling(4)
                .setAntiAliasing(81)
                .setMultithreading(3)
                .setDirection(new Point(0,distance/2,0), Vector.AXIS_Z);

        builder.build().renderImage().writeToImage("AA-Final with_both");
    }
    @Test
    void advanceTest3(){
        Scene scene =  new Scene("TestScene2");
        Color snowman = new Color(220, 220, 220);
        Color eyes = new Color(BLUE);
        double distance = 2.5;
        scene.geometries.add(
                /*floor*/ new Plane (new Point(0,0,0), Vector.AXIS_Z).setEmission(new Color(BLACK)).setMaterial(new Material().setKs(0.1).setKd(0.1).setKr(0.3)),
                /*big ball*/ new Sphere(new Point(0,0,0.45),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*middle ball*/ new Sphere(new Point(0,0,1.23),0.27).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*top ball*/ new Sphere(new Point(0,0,1.62),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*nose*/ new Triangle(new Point(0.17,0,1.72),new Point(0.17,0,1.58),new Point(0.5,0,1.65)).setEmission(new Color(ORANGE)),
                /*left hand*/ new Triangle(new Point(0,0.2,1.2),new Point(0,0.2,1.3),new Point(0,1,1.4)).setEmission(new Color(150,75,0)),
                /*right hand*/ new Triangle(new Point(0,-0.2,1.2),new Point(0,-0.2,1.3),new Point(0,-1,1.4)).setEmission(new Color(150,75,0)),
                /*left eye*/ new Sphere(new Point(0.15,0.1,1.72),0.02).setEmission(eyes),
                /*right eye*/ new Sphere(new Point(0.13,-0.1,1.72),0.02).setEmission(eyes),

                /* SM2 big ball*/ new Sphere(new Point(0,distance+0.2,1.75),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 middle ball*/ new Sphere(new Point(0,distance+0.1,0.95),0.30).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 small ball*/ new Sphere(new Point(0,distance,0.52),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2  nose*/ new Triangle(new Point(0.17,distance,0.43),new Point(0.17,distance,0.57),new Point(0.5,distance,0.51)).setEmission(new Color(ORANGE)),
                /*SM2 left top hand*/    new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.25,0.95)).setEmission(new Color(150,75,0)),
                /*SM2 left bottom hand*/ new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.3,0)).setEmission(new Color(150,75,0)),
                /*SM2 right hand*/ new Triangle(new Point(0,distance+0.1-0.25,0.9),new Point(0,distance+0.1-0.25,1),new Point(0,distance+0.1-0.3-0.7,0.4)).setEmission(new Color(150,75,0)),
                /*SM2 left eye*/ new Sphere(new Point(0.13,distance+0.1,0.42),0.02).setEmission(eyes),
                /*SM2 right eye*/ new Sphere(new Point(0.13,distance-0.1,0.42),0.02).setEmission(eyes)

        );
        scene.lights = List.of(
                /* light from floor towards SM1 */ new SpotLight(new Color(255,255,255),new Point(1,0,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* light from floor towards SM2 */ new SpotLight(new Color(255,255,255),new Point(1,distance,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* point light on top of the scene*/ new PointLight(new Color(255,255,255),new Point(0,0,30)).setKl(0.00005).setKq(0.00005),
                /* sunlight */ new DirectionalLight(new Color(YELLOW), new Vector(0,-1,-1)),
                /*spotlight onto the middle of the reflections*/ new SpotLight(new Color(255,255,255),new Point(2,distance,0.7), new Vector(0,0,-1)).setKl(0.005).setKq(0.005)
        );
        scene.setBackground(new Color(255,255,255));
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene,RayTracerType.SIMPLE)
                .setLocation(new Point(30,15,10))
                .setVpDistance(1000).setVpSize(150,150)
                .setResolution(1000,1000)
                //.setAdaptiveSuperSampling(4)
                .setAntiAliasing(81)
                .setMultithreading(3)
                .setDirection(new Point(0,distance/2,0), Vector.AXIS_Z);

        builder.build().renderImage().writeToImage("AA-Final_withoutAdaptiveSampling");
    }
    @Test
    void advanceTest2(){
        Scene scene =  new Scene("TestScene2");
        Color snowman = new Color(220, 220, 220);
        Color eyes = new Color(BLUE);
        double distance = 2.5;
        scene.geometries.add(
                /*floor*/ new Plane (new Point(0,0,0), Vector.AXIS_Z).setEmission(new Color(BLACK)).setMaterial(new Material().setKs(0.1).setKd(0.1).setKr(0.3)),
                /*big ball*/ new Sphere(new Point(0,0,0.45),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*middle ball*/ new Sphere(new Point(0,0,1.23),0.27).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*top ball*/ new Sphere(new Point(0,0,1.62),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*nose*/ new Triangle(new Point(0.17,0,1.72),new Point(0.17,0,1.58),new Point(0.5,0,1.65)).setEmission(new Color(ORANGE)),
                /*left hand*/ new Triangle(new Point(0,0.2,1.2),new Point(0,0.2,1.3),new Point(0,1,1.4)).setEmission(new Color(150,75,0)),
                /*right hand*/ new Triangle(new Point(0,-0.2,1.2),new Point(0,-0.2,1.3),new Point(0,-1,1.4)).setEmission(new Color(150,75,0)),
                /*left eye*/ new Sphere(new Point(0.15,0.1,1.72),0.02).setEmission(eyes),
                /*right eye*/ new Sphere(new Point(0.13,-0.1,1.72),0.02).setEmission(eyes),

                /* SM2 big ball*/ new Sphere(new Point(0,distance+0.2,1.75),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 middle ball*/ new Sphere(new Point(0,distance+0.1,0.95),0.30).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 small ball*/ new Sphere(new Point(0,distance,0.52),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2  nose*/ new Triangle(new Point(0.17,distance,0.43),new Point(0.17,distance,0.57),new Point(0.5,distance,0.51)).setEmission(new Color(ORANGE)),
                /*SM2 left top hand*/    new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.25,0.95)).setEmission(new Color(150,75,0)),
                /*SM2 left bottom hand*/ new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.3,0)).setEmission(new Color(150,75,0)),
                /*SM2 right hand*/ new Triangle(new Point(0,distance+0.1-0.25,0.9),new Point(0,distance+0.1-0.25,1),new Point(0,distance+0.1-0.3-0.7,0.4)).setEmission(new Color(150,75,0)),
                /*SM2 left eye*/ new Sphere(new Point(0.13,distance+0.1,0.42),0.02).setEmission(eyes),
                /*SM2 right eye*/ new Sphere(new Point(0.13,distance-0.1,0.42),0.02).setEmission(eyes)

        );
        scene.lights = List.of(
                /* light from floor towards SM1 */ new SpotLight(new Color(255,255,255),new Point(1,0,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* light from floor towards SM2 */ new SpotLight(new Color(255,255,255),new Point(1,distance,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* point light on top of the scene*/ new PointLight(new Color(255,255,255),new Point(0,0,30)).setKl(0.00005).setKq(0.00005),
                /* sunlight */ new DirectionalLight(new Color(YELLOW), new Vector(0,-1,-1)),
                /*spotlight onto the middle of the reflections*/ new SpotLight(new Color(255,255,255),new Point(2,distance,0.7), new Vector(0,0,-1)).setKl(0.005).setKq(0.005)
        );
        scene.setBackground(new Color(255,255,255));
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene,RayTracerType.SIMPLE)
                .setLocation(new Point(30,15,10))
                .setVpDistance(1000).setVpSize(150,150)
                .setResolution(1000,1000)
                .setAdaptiveSuperSampling(4)
                //.setAntiAliasing(81)
                .setMultithreading(3)
                .setDirection(new Point(0,distance/2,0), Vector.AXIS_Z);

        builder.build().renderImage().writeToImage("AA-Final_without_anti_aliasing");
    }
    @Test
    void advanceTest1(){
        Scene scene =  new Scene("TestScene2");
        Color snowman = new Color(220, 220, 220);
        Color eyes = new Color(BLUE);
        double distance = 2.5;
        scene.geometries.add(
                /*floor*/ new Plane (new Point(0,0,0), Vector.AXIS_Z).setEmission(new Color(BLACK)).setMaterial(new Material().setKs(0.1).setKd(0.1).setKr(0.3)),
                /*big ball*/ new Sphere(new Point(0,0,0.45),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*middle ball*/ new Sphere(new Point(0,0,1.23),0.27).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*top ball*/ new Sphere(new Point(0,0,1.62),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*nose*/ new Triangle(new Point(0.17,0,1.72),new Point(0.17,0,1.58),new Point(0.5,0,1.65)).setEmission(new Color(ORANGE)),
                /*left hand*/ new Triangle(new Point(0,0.2,1.2),new Point(0,0.2,1.3),new Point(0,1,1.4)).setEmission(new Color(150,75,0)),
                /*right hand*/ new Triangle(new Point(0,-0.2,1.2),new Point(0,-0.2,1.3),new Point(0,-1,1.4)).setEmission(new Color(150,75,0)),
                /*left eye*/ new Sphere(new Point(0.15,0.1,1.72),0.02).setEmission(eyes),
                /*right eye*/ new Sphere(new Point(0.13,-0.1,1.72),0.02).setEmission(eyes),

                /* SM2 big ball*/ new Sphere(new Point(0,distance+0.2,1.75),0.55).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 middle ball*/ new Sphere(new Point(0,distance+0.1,0.95),0.30).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2 small ball*/ new Sphere(new Point(0,distance,0.52),0.18).setEmission(snowman).setMaterial(new Material().setKs(0.3).setKd(0.4)),
                /*SM2  nose*/ new Triangle(new Point(0.17,distance,0.43),new Point(0.17,distance,0.57),new Point(0.5,distance,0.51)).setEmission(new Color(ORANGE)),
                /*SM2 left top hand*/    new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.25,0.95)).setEmission(new Color(150,75,0)),
                /*SM2 left bottom hand*/ new Triangle(new Point(0,distance+0.1+0.3+0.2,0.75),new Point(0,distance+0.1+0.3+0.2,0.6),new Point(0,distance+0.1+0.3,0)).setEmission(new Color(150,75,0)),
                /*SM2 right hand*/ new Triangle(new Point(0,distance+0.1-0.25,0.9),new Point(0,distance+0.1-0.25,1),new Point(0,distance+0.1-0.3-0.7,0.4)).setEmission(new Color(150,75,0)),
                /*SM2 left eye*/ new Sphere(new Point(0.13,distance+0.1,0.42),0.02).setEmission(eyes),
                /*SM2 right eye*/ new Sphere(new Point(0.13,distance-0.1,0.42),0.02).setEmission(eyes)

        );
        scene.lights = List.of(
                /* light from floor towards SM1 */ new SpotLight(new Color(255,255,255),new Point(1,0,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* light from floor towards SM2 */ new SpotLight(new Color(255,255,255),new Point(1,distance,0), new Vector(-1,0,3)).setKl(0.00005).setKq(0.00005),
                /* point light on top of the scene*/ new PointLight(new Color(255,255,255),new Point(0,0,30)).setKl(0.00005).setKq(0.00005),
                /* sunlight */ new DirectionalLight(new Color(YELLOW), new Vector(0,-1,-1)),
                /*spotlight onto the middle of the reflections*/ new SpotLight(new Color(255,255,255),new Point(2,distance,0.7), new Vector(0,0,-1)).setKl(0.005).setKq(0.005)
        );
        scene.setBackground(new Color(255,255,255));
        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(scene,RayTracerType.SIMPLE)
                .setLocation(new Point(30,15,10))
                .setVpDistance(1000).setVpSize(150,150)
                .setResolution(1000,1000)
                //.setAdaptiveSuperSampling(4)
                //.setAntiAliasing(81)
                .setMultithreading(3)
                .setDirection(new Point(0,distance/2,0), Vector.AXIS_Z);

        builder.build().renderImage().writeToImage("AA-Final_without_both");
    }
}
