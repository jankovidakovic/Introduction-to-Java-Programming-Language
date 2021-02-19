package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Generic element. Intended to be inherited and used to store values
 * of tokens that the lexer generates.
 *
 * @author jankovidakovic
 *
 */
public class Element {
	//TODO - add visitor design pattern support
	public String asText() {
		return new String("");
	}

}
