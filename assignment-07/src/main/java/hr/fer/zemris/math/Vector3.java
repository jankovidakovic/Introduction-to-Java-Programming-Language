package hr.fer.zemris.math;

/**
 * Model of an unmodifiable three-dimensional vector. All operations that result
 * in a new vector return the result as a new instance of this class.
 * 
 * @author jankovidakovic
 *
 */
public class Vector3 {

	private double x;
	private double y;
	private double z;

	/**
	 * Constructs a new vector with given components.
	 * 
	 * @param x x-component of the vector
	 * @param y y-component of the vector
	 * @param z z-component of the vector
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the euclidean norm of the vector, which is the vector's length.
	 * 
	 * @return length of the vector
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns the normalized version of the vector, which is the vector with
	 * the same direction but unit length;
	 * 
	 * @return normalized vector
	 */
	public Vector3 normalized() {
		double norm = norm();
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Returns the result of the addition between the instance of vector upon
	 * which the method was called, and the provided vector.
	 * 
	 * @param other second operand of addition
	 * @return result of the addition as a new vector
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.getX(), y + other.getY(),
				z + other.getZ());
	}

	/**
	 * Returns the result of the subtraction between the instance of vector upon
	 * which the method was called, and the provided vector.
	 * 
	 * @param other second operand of subtraction
	 * @return result of the subtraction as a new vector
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.getX(), y - other.getY(),
				z - other.getZ());
	}

	/**
	 * Returns the value of dot product between the instance of vector upon
	 * which the method was called, and the provided vector.
	 * 
	 * @param other second operand of dot product
	 * @return value of dot product
	 */
	public double dot(Vector3 other) {
		return x * other.getX() + y * other.getY() + z * other.getZ();
	}

	/**
	 * Returns the cross product between the instance of vector upon which the
	 * method was called, and provided vector.
	 * 
	 * i  j  k
	 * x1 y2 z1
	 * x2 y2 z2
	 * @param other second operand of cross product
	 * @return result of the cross product as a new vector
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.getZ() - z * other.getY(),
				-1 * (x * other.getZ() - z * other.getX()),
				x * other.getY() - y * other.getX());
	}

	/**
	 * Scales the vector by the given scaling factor and returns the result as a
	 * new vector.
	 * 
	 * @param s scaling factor
	 * @return scaled vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Returns the value of the cosine of the angle between the instance of the
	 * vector upon which the method was called, and provided vector.
	 * 
	 * @param other second operand
	 * @return value of the cosine of the angle between the two vectors.
	 */
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (norm() * other.norm());
	}

	/**
	 * Returns the x-component of the vector
	 * 
	 * @return x component of the vector.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y-component of the vector.
	 * 
	 * @return y-component of the vector.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the z-component of the vector.
	 * 
	 * @return z-component of the vector.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns the vector in its matrix representation, as an array of its three
	 * components.
	 * 
	 * @return matrix representation of the vector, as an array of its
	 *         components.
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	/**
	 * Returns the string representation of the vector, in the form (x, y, z).
	 * Components are rounded to 6 decimal places.
	 */
	@Override
	public String toString() {
		return "(" + formatComponent(x) + ", " + formatComponent(y) + ", "
				+ formatComponent(z) + ")";
	}

	private String formatComponent(double c) {
		return String.format("%.6f", c);
	}
}
