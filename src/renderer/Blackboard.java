package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

public class Blackboard {
    double height;
    double width;
    Vector x;
    Vector y;
    int sqrtNumRays;
    Point center;
    //Vector normal;

    public Blackboard(double height, double width, Point center, Vector normal,int numRays) {
        this(height, width, center, normal.getOrthogonal().crossProduct(normal).normalize(), normal.getOrthogonal().normalize(), numRays);
    }


    public Blackboard(double height, double width, Point center, Vector y,Vector x,int numRays) {
        this.height = height;
        this.width = width;
        this.center = center;
        this.x = x.normalize();
        this.y = y.normalize();
        this.sqrtNumRays = (int) Math.sqrt(numRays);
    }
    public List<Ray> getRays(Point head){
        Point base = center;
        Point p;
        List <Ray> rays = new java.util.ArrayList<>();

        for(int i = 0; i < sqrtNumRays; i++) {
            p = base;
            for (int j = 0; j < sqrtNumRays; j++) {
                // Calculate the point on the blackboard
                double yI = -(i - (sqrtNumRays - 1) / 2d) * height / sqrtNumRays;
                double xJ = (j - (sqrtNumRays - 1) / 2d) * width / sqrtNumRays;

                if (!Util.isZero(xJ)) p = p.add(x.scale(xJ));
                if (!Util.isZero(yI)) p = p.add(y.scale(yI));
                rays.add(new Ray(head,p.subtract(head)));
            }
        }
        return rays;
    }
}
