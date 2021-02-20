package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * Model of an object that remembers the turtle's state. A draw turtle's state
 * consists of the current position of the turtle, current angle relative to
 * the positive part of x-axis, color of the turtle's pen and effective length
 * of unit shift.
 *
 * @author jankovidakovic
 *
 */
public class TurtleState {
	
	//private variables
	private Vector2D position;	//radius-vector 
	private Vector2D angle;		//unit direction vector
	private Color color;		//color of the turtle's ink
	private double unitLength; //length of one unit shift
	
	/**
	 * Initializes the turtle state with given values
	 *
	 * @param position Starting position of the turtle
	 * @param angle Starting angle of the turtle
	 * @param color Drawing color of the turtle
	 * @param unitLength Unit length which the turtle uses.
	 */
	public TurtleState(Vector2D position, Vector2D angle, Color color,
			double unitLength) {
		this.position = position;
		this.angle = angle;
		this.color = color;
		this.unitLength= unitLength;
	}
	
	/**
	 * Returns a hard copy of the turtle state
	 *
	 * @return hard copy of the turtle state.
	 */
	public TurtleState copy() {
		return new TurtleState(position, angle, color, unitLength);
	}

	/**
	 * @return the position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * @return the angle
	 */
	public Vector2D getAngle() {
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(Vector2D angle) {
		this.angle = angle;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the unitLength
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * @param unitLength the unitLength to set
	 */
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}
	
	
}
