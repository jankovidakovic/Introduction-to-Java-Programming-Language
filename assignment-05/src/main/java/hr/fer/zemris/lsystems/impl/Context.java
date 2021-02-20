package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Model of an object that enables the drawing of fractals.
 * Provides the inner stack to store turtle states and methods for
 * its manipulation.
 *
 * @author jankovidakovic
 *
 */
public class Context {

	//private variables
	private final ObjectStack<TurtleState> stack; //inner stack to store turtle states
	
	public Context() {
		stack = new ObjectStack<>();
	}
	/**
	 * Returns the current state of the turtle, which is the one at the top of 
	 * the stack.
	 *
	 * @return current state of the turtle.
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}
	
	/**
	 * Pushes the given state to the stack.
	 *
	 * @param state State of the turtle to be pushed onto the stack.
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	/**
	 * Removes the state from top of the stack.
	 */
	public void popState() {
		stack.pop();
	}
	
}
