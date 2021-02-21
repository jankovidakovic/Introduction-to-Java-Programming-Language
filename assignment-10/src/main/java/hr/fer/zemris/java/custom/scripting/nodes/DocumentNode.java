package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents a root node of the structured document tree. Holds information
 * about the whole document through its child nodes. 
 * @author jankovidakovic
 *
 */
public class DocumentNode extends Node {
	
	/**
	 * Returns the string representation of the node. As the node represents
	 * the entire generative tree, this method returns the string representation
	 * of the whole tree. When result of this method is parsed again with
	 * SmartScriptParser, it should produce a DocumentNode which is considered
	 * equal to this one.
	 * @return String representation of an entire document.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numberOfChildren(); i++) {
			sb.append(getChild(i));
		}
		return sb.toString();
	}
	
	/**
	 * Processes the current instance of the node using the given visitor.
	 * 
	 * @param visitor visitor used to process the node.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}

}
