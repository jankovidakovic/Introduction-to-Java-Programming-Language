package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Stores an operator as a read-only <code>String</code> instance. 
 * Not guaranteed that provided operator is valid, this is the responisibility 
 * of the client who uses this class.
 * @author jankovidakovic
 *
 */
public class ElementOperator extends Element {
	
	//private variables
	private final String symbol; //String representation of an operator
	
	/**
	 * Constructs the instance which stores given string.
	 * @param symbol given string to be stored.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Retrieves the stored operator.
	 * @return stored operator.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Returns the text representation of the operator.
	 * @return text representation of the operator.
	 */
	@Override
	public String asText() {
		return symbol;
	}
	
	/**
	 * Provides the valid representation of operator for printing.
	 * @return <code>String</code> representation of the operator.
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	/**
	 * Checks whether current instance is equal to the given object.
	 * Two instances of this class are considered equal if they 
	 * hold same operators, as determined by <code>equals</code>
	 * method upon the stored strings.
	 * @param value Object with which the equality is checked.
	 * @return <code>true</code> if current instance is equal to given object,
	 * 	<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ElementOperator)) { //cannot be equal
			return false;
		}
		ElementOperator element = (ElementOperator)value;
		return getSymbol().equals(element.getSymbol()); //check equality of strings
	}
}
