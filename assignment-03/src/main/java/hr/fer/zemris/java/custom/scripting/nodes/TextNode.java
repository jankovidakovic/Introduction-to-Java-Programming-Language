package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents the text part of some document as a node.
 *
 * @author jankovidakovic
 *
 */
public class TextNode extends Node {
	
	//private variables
	private final String text; //text of the node
	
	/**
	 * Constructs the text node with a given text
	 *
	 * @param text content of the node
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * Retrieves the text which this node represents
	 *
	 * @return text of the node
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Returns the string representation of the node, as plain text.
	 *
	 * @return string representation of stored text.
	 */
	public String toString() {
		return getText();
	}
	
	/**
	 * Determines whether this instance is equal to the given object.
	 * Two <code>TextNode</code> instances are considered equal if they
	 * store the same text, as determined by <code>equals</code> method 
	 * of stored strings, with leading and trailing whitespaces ignored.
	 *
	 * @param value object with which the equality of the current instance is checked
	 * @return <code>true</code> if this instance is equal to the given value,
	 * 		<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof TextNode)) { //cannot be equal
			return false;
		}
		TextNode node = (TextNode)value;
		return getText().trim().equals(node.getText().trim());
	}
}
