package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Represents a command which generates some textual output dynamically.
 * @author jankovidakovic
 *
 */
public class EchoNode extends Node {
	
	//private variables
	private final Element[] elements; //elements of the command
	
	/**
	 * Constructs a node with given elements
	 * @param elements elements of the node
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}
	
	/**
	 * Retrieves the elements of the node.
	 * @return elements of the node.
	 */
	public Element[] getElements() {
		return elements;
	}
	
	/**
	 * Returns the <code>String</code> representation of this node,
	 * appropriate for printing. String representation is the same
	 * as the input text that ahs been parsed to this node, meaning
	 * that the result of this method would generate the same EchoNode
	 * when parsed again by the SmartScriptParser.
	 * @return String representation of the node.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$= "); //beginning of unnamed tag
		for (int i = 0; i < elements.length; i++) {
			sb.append(elements[i] + " "); //adding elements in order
		}
		sb.append("$}"); //end of the tag
		return sb.toString();
	}
	
	/**
	 * Determines whether this instance of the node is equal to given value.
	 * Two instances of <code>EchoNode</code> are considered equal if 
	 * they store the same number of elements, and all corresponding elements
	 * are equal and in the same order.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof EchoNode)) { //cannot be equal
			return false;
		}
		EchoNode node = (EchoNode) value;	
		Element[] nodeElements = node.getElements();
		if (elements.length != nodeElements.length) {
			return false; //nodes do not store the same number of elements
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].equals(nodeElements[i]) == false) {
				return false; //corresponding elements are not equal
			}
		}
		return true; //all is equal
	}

	/**
	 * Processes the current instance of the node using the given visitor.
	 * 
	 * @param visitor visitor used to process the node.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
