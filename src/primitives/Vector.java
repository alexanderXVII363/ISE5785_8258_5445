package primitives;

/**
 * A class that represents a three-dimensional vector with x, y, and z components.
 * The class inherits from the Point class.
 */
public class Vector extends Point {
    public static Vector AXIS_X = new Vector (1,0,0);
    public static Vector AXIS_Y = new Vector (0,1,0);
    public static Vector AXIS_Z = new Vector (0,0,1);

    /**
     * Constructs a new Vector with the specified x, y, and z components.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     * @param z The z-component of the vector.
     * @throws IllegalArgumentException If the vector is the zero vector.
     */
    public Vector(double x, double y, double z){
        super(x, y, z);
        if(this.xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("the zero vector not ok");
        }
    }

    /**
     * Constructs a new Vector using the coordinates of the given Double3 object.
     *
     * @param new_xyz The Double3 object containing the vector components.
     * @throws IllegalArgumentException If the vector is the zero vector.
     */
    public Vector(Double3 new_xyz){
        super(new_xyz.d1, new_xyz.d2, new_xyz.d3);
        if(new_xyz.equals(Double3.ZERO)){
            throw new IllegalArgumentException("The zero vector is Illegal");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) && super.equals(other);
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }

    /**
     * Computes the squared length of this vector.
     *
     * @return The squared length of this vector.
     */
    public double lengthSquared(){
        return this.dotProduct(this);
    }

    /**
     * Computes the length of this vector.
     *
     * @return The length of this vector.
     */
    public double length(){
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Adds another vector to this vector.
     *
     * @param another_vector The vector to add.
     * @return A new vector representing the sum of this vector and the given vector.
     */
    public Vector add(Vector another_vector) {
        return new Vector(this.xyz.add(another_vector.xyz));
    }

    /**
     * Scales this vector by a scalar factor.
     *
     * @param num The scalar factor.
     * @return A new vector representing the scaled vector.
     */
    public Vector scale(double num){
        return new Vector(this.xyz.scale(num));
    }

    /**
     * returns a vector that is orthogonal to this vector.
     * @return A vector that is orthogonal to this vector.
     */
    public Vector getOrthogonal(){
        if(xyz.d2()!=0 ||xyz.d3()!=0){
            return new Vector(0, xyz.d3(), -xyz.d2());
        }
        return AXIS_Y;
    }
    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param another_vector The other vector.
     * @return The dot product of this vector and the other vector.
     */
    public double dotProduct(Vector another_vector){
        return (this.xyz.d1 * another_vector.xyz.d1) + (this.xyz.d2 * another_vector.xyz.d2) + (this.xyz.d3 * another_vector.xyz.d3);
    }

    /**
     * Computes the cross product of this vector and another vector.
     *
     * @param another_vector The other vector.
     * @return A new vector representing the cross product of this vector and the other vector.
     */
    public Vector crossProduct(Vector another_vector){
        double THIS_x = this.xyz.d1;
        double THIS_y = this.xyz.d2;
        double THIS_z = this.xyz.d3;
        double V_x = another_vector.xyz.d1;
        double V_y = another_vector.xyz.d2;
        double V_z = another_vector.xyz.d3;
        return new Vector((THIS_y * V_z) - (THIS_z * V_y), (THIS_z * V_x) - (THIS_x * V_z), (THIS_x * V_y) - (THIS_y * V_x));
    }

    /**
     * Normalizes this vector to have unit length.
     *
     * @return A new vector representing the normalized vector.
     */
    public Vector normalize(){
        return new Vector(this.xyz.reduce(this.length()));
    }
}