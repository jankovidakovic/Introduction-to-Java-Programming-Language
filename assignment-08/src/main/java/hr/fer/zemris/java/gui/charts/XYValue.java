package hr.fer.zemris.java.gui.charts;

/**
 * Implementation of a pair of integer values.
 * 
 * @author jankovidakovic
 *
 */
public class XYValue {

	private int x; // first value
	private int y; // second value

	/**
	 * Constructs a new pair that stores given values.
	 * 
	 * @param x first value
	 * @param y second value
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the first value.
	 * 
	 * @return The first value.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the second value.
	 * 
	 * @return The second value.
	 */
	public int getY() {
		return y;
	}
}
