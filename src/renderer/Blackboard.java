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

    /**
     * Constructor for creating a blackboard with a specified height, width, center point, and normal vector.
     *
     * @param height    The height of the blackboard.
     * @param width     The width of the blackboard.
     * @param center    The center point of the blackboard.
     * @param normal    The normal vector to the blackboard.
     * @param numRays   The number of rays to be generated.
     */
    public Blackboard(double height, double width, Point center, Vector normal,int numRays) {
        this(height, width, center, normal.getOrthogonal().crossProduct(normal).normalize(), normal.getOrthogonal().normalize(), numRays);
    }


    /**
     * Constructor for creating a blackboard with a specified height, width, center point, and orthogonal vectors.
     *
     * @param height    The height of the blackboard.
     * @param width     The width of the blackboard.
     * @param center    The center point of the blackboard.
     * @param y         The vector in the Y direction (orthogonal to the X direction).
     * @param x         The vector in the X direction (orthogonal to the Y direction).
     * @param numRays   The number of rays to be generated.
     */
    public Blackboard(double height, double width, Point center, Vector y,Vector x,int numRays) {
        this.height = height;
        this.width = width;
        this.center = center;
        this.x = x.normalize();
        this.y = y.normalize();
        this.sqrtNumRays = (int) Math.sqrt(numRays);
    }
    /**
     * Generates a list of rays originating from a specified head point, directed towards points on the blackboard.
     *
     * @param head The point from which the rays originate.
     * @return A list of rays directed towards points on the blackboard.
     */
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
