package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;

/**
 * Model of a button that represents a binary operation, such as addition,
 * subtraction, multiplication, division or exponentiation. When pressed, an
 * action that was passed through the constructor is performed.
 * 
 * @author jankovidakovic
 *
 */
public class BinaryOperationButton extends JButton {

	private final DoubleBinaryOperator operation;

	/**
	 * Constructs a button object with given name, representing the given
	 * operation, and able to execute the given action when pressed.
	 * 
	 * @param name      name of the button
	 * @param operation operation performed
	 * @param onClick   action executed upon pressing
	 */
	public BinaryOperationButton(String name, DoubleBinaryOperator operation,
			ActionListener onClick) {

		super(name);

		this.operation = operation;

		addActionListener(onClick);
	}

	/**
	 * Returns the conceptual operation that the button performs.
	 * 
	 * @return operation
	 */
	public DoubleBinaryOperator getOperation() {
		return operation;
	}
}
