package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Node which represents a single for-loop construct.
 * Such construct consists of a variable, start expression, end expression
 * and step expression. Step expression can be left out, but the other
 * need to be defined for the proper definition of this type of node.
 * @author jankovidakovic
 *
 */
public class ForLoopNode extends Node {
	
	//private variables
	private ElementVariable variable; 	//variable of the loop
	private Element startExpression; 	//starting expression of the loop
	private Element endExpression;		//ending expression of the loop
	private Element stepExpression;		//step expression(can be null)
	
	/**
	 * Constructs a node with given initial values.
	 * @param variable variable of the loop
	 * @param startExpression start expression
	 * @param endExpression end expression
	 * @param stepExpression step expression (can be left out)
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression,
			Element endExpression, Element stepExpression) {
		if (variable == null) { //cannot be left out
			throw new IllegalArgumentException("variable cannot be null.");
		}
		if (startExpression == null) { //cannot be left out
			throw new IllegalArgumentException("Start expression cannot be null.");
		}
		if (endExpression == null) { //cannot be left out
			throw new IllegalArgumentException("End expression cannot be null.");
		}
		
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
		
	}
	
	/**
	 * Getter for variable of the for-loop.
	 * @return variable of the for-loop
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Getter for start expression of the for-loop.
	 * @return start expression of the for-loop.
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Getter for end expression of the for-loop.
	 * @return end expression of the for-loop.
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Getter for step expression of the for-loop.
	 * @return step expression of the for-loop. can be <code>null</code>.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * Returns the string representation of the for-loop, in such form that
	 * when it is parsed again by the SmartScriptParser, it produces the node
	 * which is considered equal to this node.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$ FOR "); //beginning of named tag
		sb.append(variable + " "); //variable 
		sb.append(startExpression + " "); //start expression
		sb.append(endExpression + " "); //end expression
		if (stepExpression != null) { //only add step expression if it exists
			sb.append(stepExpression + " "); 
		}
		sb.append("$}\n"); // end ot the named-tag body
		for (int i = 0; i < numberOfChildren(); i++) {
			sb.append(getChild(i)); //append all children
		}
		sb.append("{$ END $}\n"); //end tag for the corresponding for-loop
		return sb.toString();
	}
	
	/**
	 * Determines whether this instance is equal to given value.
	 * Two instances of <code>ForLoopNode</code> are considered equal
	 * if all their corresponding parameters (variable, start expression, 
	 * end expression and step expression) are equal, they have the same
	 * number of children, and all the corresponding children are equal.
	 * @return <code>true</code> if this instance is equal to the given value,
	 * 		<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object value) {
		if (!(value instanceof ForLoopNode)) { //cannot be equal
			return false;
		}
		ForLoopNode node = (ForLoopNode)value;
		
		//if any of the following if statements execute, the nodes are not equal
		if (variable.equals(node.getVariable()) == false) {
			return false;
		}
		if (startExpression.equals(node.getStartExpression()) == false) {
			return false;
		}
		if (endExpression.equals(node.getEndExpression()) == false) {
			return false;
		}
		if (stepExpression.equals(node.getStepExpression()) == false) {
			return false;
		}
		
		if (numberOfChildren() != node.numberOfChildren()) { 
			return false; //different number of children
		}
		for (int i = 0; i < numberOfChildren(); i++) {
			if (getChild(i).equals(node.getChild(i)) == false) {
				return false; //corresponding children are not equal.
			}
		}
		return true; //all children are equal
		
	}
	
	/**
	 * Processes the current instance of the node using the given visitor.
	 * 
	 * @param visitor visitor used to process the node.
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

}
