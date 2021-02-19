package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element which stores a variable name. in its <code>String</code>
 * representation, as a read-only property. It is not guaranteed
 * that the variable name stored is valid in the context of the SmartScriptParser,
 * that should be dealt with when constructing the element.
 * @author jankovidakovic
 *
 */
public class ElementVariable extends Element {
	
	//private variables
	private final String name;//name of the variable
	
	/**
	 * Constructs an element with given variable name.
	 * @param name variable name that the element will hold.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the name of the variable.
	 *
	 * @return name of the variable.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the text representation of variable name.
	 *
	 * @return text representation of variable name
	 */
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * Provides the valid representation of variable name for printing.
	 *
	 * @return <code>String</code> representation of variable name.
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	/**
	 * Checks whether current instance is equal to the given object.
	 * Two instances of this class are considered equal if they 
	 * hold variables with equal names, as determined by <code>equals</code>
	 * method upon the names.
	 *
	 * @param value Object with which the equality is checked.
	 * @return <code>true</code> if current instance is equal to given object,
	 * 	<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ElementVariable)) { //cannot be equal
			return false;
		}
		ElementVariable other = (ElementVariable)value;
		return other.getName().equals(getName()); //check if names are equal.
	}
}
