package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Stores a string found inside the tag of the document, as read-only property.
 * It is not guaranteed that stored string is valid, that is the responsibility
 * of client which uses this class.
 *
 * @author jankovidakovic
 *
 */
public class ElementString extends Element {
	
	//private variables
	private final String value; //content of the string
	
	/**
	 * Constructs the instance which stores given string.
	 *
	 * @param value String to be stored.
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	/**
	 * Getter for the stored string.
	 *
	 * @return stored string.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the plain-text representation of stored string.
	 *
	 * @return plain-text representation of stored string.
	 */
	@Override
	public String asText() {
		return value;
	}
	
	/**
	 * Returns the representation of string which is valid in
	 * context of the valid document. Encloses stored string in 
	 * double quotes.
	 *
	 * @return valid representation of stored string
	 */
	@Override
	public String toString() {
		return "\"" + asText() + "\"";
	}
	
	/**
	 * Checks whether current instance is equal to the given object.
	 * Two instances of this class are considered equal if they 
	 * store equal strings, as determined by <code>equals</code>
	 * method upon the values of strings.
	 *
	 * @param value Object with which the equality is checked.
	 * @return <code>true</code> if current instance is equal to given object,
	 * 	<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ElementString)) { //cannot be equal
			return false;
		}
		ElementString other = (ElementString) value;
		return other.asText().equals(asText()); //check equality of stored strings
	}
}
