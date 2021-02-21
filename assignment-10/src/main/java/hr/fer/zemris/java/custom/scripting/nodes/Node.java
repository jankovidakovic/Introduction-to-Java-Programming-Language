package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Most general representation of a node. Contains only a list of its
 * children, and nothing else. Can be used for representation of 
 * structured documents. Specific instances of this class will be used
 * to represent the generative tree of parsed document.
 * @author jankovidakovic
 *
 */
public abstract class Node {
	//TODO - add visitor design pattern support
	
	//private variables
	private ArrayIndexedCollection children; //children nodes of this node
	//this implementation does not allocate the collection of children until
	//the first child is added.
	
	/**
	 * Adds the given node to the collection of children.
	 * @param child Child node which is to be added
	 */
	public void addChildNode(Node child) {
		if (children == null) {	//because children collection is not
						//allocated before first call of this method
			children = new ArrayIndexedCollection();
		}
		children.add(child); //add a new child
	}
	
	/**
	 * Returns the number of children of the node.
	 * @return number of children of the node
	 */
	public int numberOfChildren() {
		try { //if there are no children, then the inner collection is still null
			return children.size();
		} catch (NullPointerException ex) { //inner collection was not initialized
			return 0;	//there are no children
		}
	}
	
	/**
	 * Retrieves the child which is stored at the given index.
	 * @param index Index of the child. Valid indexes are from 0 to
	 * 		<code>numberOfChildren()-1</code>
	 * @return Node which represents a child at given index.
	 * @throws IndexOutOfBoundsException if invalid index is given.
	 */
	public Node getChild(int index) {
		try {	//if there are no children, then inner collection is still null
			return (Node)children.get(index);
		} catch (NullPointerException ex) {
			throw new IndexOutOfBoundsException("Invalid index.");
		}
	}
	
	/**
	 * Determines whetver or not two nodes are equal.
	 * Nodes are considered equal if they have the same number of children,
	 * and all the corresponding children are equal.
	 * @return <code>true</code> if this instance is equal to the given value,
	 * 		<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof Node)) { //not castable to Node
			return false;
		}
		Node node = (Node) value;
		if (numberOfChildren() != node.numberOfChildren()) { 
			return false; //different number of children
		}
		for (int i = 0; i < numberOfChildren(); i++) {
			if (getChild(i).equals(node.getChild(i)) == false) {
				return false; //current child is different
			}
		}
		return true; //all children are equal
	}
	
	/**
	 * Support for the visitor design pattern. Classes that inherit this class
	 * should implement the method which can process their instances using the
	 * visitor.
	 * 
	 * @param visitor visitor used to process the nodes that inherit this class.
	 */
	public abstract void accept(INodeVisitor visitor);

}
