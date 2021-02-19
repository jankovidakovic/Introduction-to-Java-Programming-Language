package hr.fer.zemris.java.custom.scripting.elems;


/**
 * Represents the element which stores the function name as a read-only property.
 * It is not guaranteed that stored content is a valid function name, that 
 * should be taken care of by the client which uses this class.
 *
 * @author jankovidakovic
 */
public class ElementFunction extends Element {
	
	//private variables
	private final String name; //name of the function
	
	/**
	 * Constructs an instance of this class with given function name.
	 *
	 * @param name function name
	 */
	public ElementFunction(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the name of the function.
	 *
	 * @return name of the function.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the text representation of the function name.
	 *
	 * @return text representation of the function name.
	 */
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * Returns the function name as should be defined in the document
	 * which is parsed.
	 *
	 * @return valid function name as it should look in the document.
	 */
	public String toString() {
		return "@" + asText();
	}
	
	/**
	 * Checks whether this instance is equal to the given value.
	 * <code>ElementFunction</code> instances are considered equal
	 * if they store the same function name, as determined by the
	 * <code>equals</code> method on their respective names.
	 *
	 * @param value value to be checked for equality
	 * @return <code>true</code> if the objects are equal, <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ElementFunction)) { //cannot be equal
			return false;
		}
		ElementFunction otherFunction = (ElementFunction)value;
		return getName().equals(otherFunction.getName()); //check if names are equal
	}
}
