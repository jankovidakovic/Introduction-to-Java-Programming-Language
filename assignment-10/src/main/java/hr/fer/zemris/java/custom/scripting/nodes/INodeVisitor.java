package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Model of a visitor which can visit and process various nodes of the document
 * type that is compatible with the smart script parser.
 * 
 * @author jankovidakovic
 *
 */
public interface INodeVisitor {

	/**
	 * Processes the given TextNode.
	 * 
	 * @param node text node to process
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Processes the given ForLoopNode.
	 * 
	 * @param node for loop node to process
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Processes the given EchoNode.
	 * 
	 * @param node echo node to process
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Processes the given document node
	 * 
	 * @param node document node to process
	 */
	public void visitDocumentNode(DocumentNode node);
}
