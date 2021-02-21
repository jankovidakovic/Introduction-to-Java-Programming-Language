package hr.fer.zemris.java.webapp2.models;

/**
 * Model of an entry in the trigonometric table. Contains some whole number, its
 * sine value and its cosine value. Trig values are calculated using the degrees
 * instead of radians.
 * 
 * @author jankovidakovic
 *
 */
public class TrigEntry {

	private int num; // number
	private double sinValue; // sine value of number
	private double cosValue; // cosine value of number

	/**
	 * Creates a new trig entry for the given number. Sine and cosine values are
	 * not passed but are calculated upon instantiation.
	 * 
	 * @param num number to create a trig entry for.
	 */
	public TrigEntry(int num) {
		setNum(num);

	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * Sets the number to the given value, and updates sine and cosine values
	 * accordingly.
	 * 
	 * @param num new number to store.
	 */
	public void setNum(int num) {
		this.num = num;
		this.sinValue = Math.sin(num * Math.PI / 180); // in degrees
		this.cosValue = Math.cos(num * Math.PI / 180); // in degrees
	}

	/**
	 * @return the sinValue
	 */
	public String getSinValue() {
		return String.format("%.6f", sinValue);
	}


	/**
	 * @return the cosValue
	 */
	public String getCosValue() {
		return String.format("%.6f", cosValue);
	}

}
