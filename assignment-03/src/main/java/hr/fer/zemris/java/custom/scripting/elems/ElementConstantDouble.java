package hr.fer.zemris.java.custom.scripting.elems;


/**
 * Element which stores a decimal value. as a read-only property.
 *
 * @author jankovidakovic
 */
public class ElementConstantDouble extends Element {
	
	//private variables
	private final double value; //value of stored double
	
	/**
	 * Constructs an instance with given value
	 *
	 * @param value value of number to be stored
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Retrieves the stored value
	 *
	 * @return stored value of the decimal number.
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * Retrieves the plain-text representation of a stored decimal number, in
	 * the standard digits-dot-digits format.
	 *
	 * @return plain-text representation of a stored number.
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
	
	/**
	 * Returns the stored value represented as a <code>String</code>, which is
	 * useful for printing. Value is represented in the standard digits-dot-digits
	 * format.
	 *
	 * @return <code>String</code> representation of stored value.
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	/**
	 * Determines whether this instance is equal to given object.
	 * Two instances of this class are considered equal if they store
	 * the same values, and decimal values are considered the same if they
	 * are equal up to standard double precision, which is 12 decimals.
	 *
	 * @return <code>true</code> if the given instance stores the same
	 * 		value as this instance, <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ElementConstantDouble)) {
			return false;
		}
		ElementConstantDouble otherDouble = (ElementConstantDouble)value;
		double absDiff = this.value - otherDouble.getValue();
		return absDiff <= 1E-12;
	}
}
