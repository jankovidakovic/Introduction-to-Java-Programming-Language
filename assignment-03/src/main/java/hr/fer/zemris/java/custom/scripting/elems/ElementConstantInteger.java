package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element which stores an integral value. as a read-only property.
 *
 * @author jankovidakovic
 */
public class ElementConstantInteger extends Element {
	
	//private variables
	private final int value; //value of stored integer
	
	/**
	 * Constructs an instance of this class which stores given integer.
	 *
	 * @param value integer to be stored
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Retrieves the stored value
	 *
	 * @return stored value of the integer.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Retrieves the plain-text representation of a stored integer.
	 *
	 * @return plain-text representation of a stored integer.
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}
	
	/**
	 * Returns the stored value represented as a <code>String</code>, which is
	 * useful for printing.
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
	 * the same values.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ElementConstantInteger)) {
			return false;
		}
		return this.value == ((ElementConstantInteger)value).getValue();
	}
}
