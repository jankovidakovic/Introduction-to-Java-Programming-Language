package hr.fer.zemris.math;

/**
 * Model of a two-dimensional vector with real components x and y.
 *
 * @author jankovidakovic
 *
 */
public class Vector2D {

	//private variables
	private double x;	//x-component of the vector
	private double y; 	//y-component of the vector
	
	/**
	 * Constructs a new vector and sets its components to given values
	 *
	 * @param x x-component of the new vector
	 * @param y y-component of the new vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter for the x-component of the vector.
	 *
	 * @return x-component of the vector.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter for the y-component of the vector.
	 *
	 * @return y-component of the vector.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translates the vector by the given offset. Modifies the instance of 
	 * the vector upon which this method is called, and does not create
	 * a new one. Vector's x-component is increased by the value of given
	 * offset's x-component, and the analog thing is done for the y-component.
	 *
	 * @param offset offset of translation, represented as an instance of
	 * 		<code>Vector2D</code> class.
	 */
	public void translate(Vector2D offset) {
		x += offset.getX();
		y += offset.getY();
	}
	
	/**
	 * Creates a new instance of the class, which is obtained as a result
	 * of addition between the instance that this method is called upon, and
	 * provided offset vector. Returns the resulting vector.
	 *
	 * @param offset second operand of addition
	 * @return result of addition of two vectors, represented as an instance
	 * of <code>Vector2D</code>.
	 */
	public Vector2D translated(Vector2D offset) {
		return new Vector2D(x + offset.getX(), y + offset.getY());
	}
	
	/**
	 * Rotates the vector by the given angle.Modifies the instance of the vector
	 * upon which the method is called and doesn't create a new one.
	 *
	 * @param angle Angle of rotation, specified in radians.
	 */
	public void rotate(double angle) {
		double newX = x * Math.cos(angle) - y * Math.sin(angle);
		double newY = x * Math.sin(angle) + y * Math.cos(angle);
		x = newX;
		y = newY;
	}
	
	/**
	 * Rotates the vector by given angle and returns the resulting vector.
	 * Does not modify the instance of vector upon which the method is called.
	 *
	 * @param angle Angle of rotation, specified in radians.
	 * @return current vector rotated by the given angle.
	 */
	public Vector2D rotated(double angle) {
		Vector2D rotatedVector = copy();
		rotatedVector.rotate(angle);
		return rotatedVector;
	}
	
	/**
	 * Scales the vector by the given scalar. Modifies the instance upon which
	 * the method is called.
	 *
	 * @param scalar Factor of scaling.
	 */
	public void scale(double scalar) {
		x *= scalar;
		y *= scalar;
	}
	
	/**
	 * Scales the vector by the given scalar. Returns the result as a new vector,
	 * does not modify the instance upon which the method was called.
	 *
	 * @param scalar Factor of scaling.
	 * @return Scaled version of the current vector as a new vector.
	 */
	public Vector2D scaled(double scalar) {
		Vector2D scaledVector = copy();
		scaledVector.scale(scalar); //scale
		return scaledVector;
	}
	
	/**
	 * Returns a copy of the instance upon which the method was called.
	 *
	 * @return Copy of current vector as a new vector.
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
}
