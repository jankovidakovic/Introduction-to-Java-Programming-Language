package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;

/**
 * Model of a button that can perform exponentiation and take roots of numbers.
 * Such button is a binary operator, one operand being the base of the
 * calculation, and the other being the exponent.
 * 
 * @author jankovidakovic
 *
 */
public class ExpButton extends JButton {

	private static final long serialVersionUID = 1L;

	private DoubleBinaryOperator operation; // exponentiation
	private DoubleBinaryOperator inverseOperation; // radicalisation


	/**
	 * Constructs a new exponential button object with given name, that can
	 * perform both the given operation and the given inverse operation,
	 * depending on the state of the calculator. When pressed, given action is
	 * executed.
	 * 
	 * @param name             Name of the button
	 * @param operation        Normal operation that the button performs
	 * @param inverseOperation Inverse operation
	 * @param onClick          Action that is executed on the click
	 */
	public ExpButton(String name, DoubleBinaryOperator operation,
			DoubleBinaryOperator inverseOperation, ActionListener onClick) {
		super(name);

		this.operation = operation;
		this.inverseOperation = inverseOperation;

		addActionListener(onClick);
	}

	/**
	 * Returns the operation that the button performs when the calculator is in
	 * its normal state.
	 * 
	 * @return normal operation
	 */
	public DoubleBinaryOperator getOperation() {
		return operation;
	}

	/**
	 * Returns the operation taht the button performs when the calculator is in
	 * inverse state.
	 * 
	 * @return inverse operation
	 */
	public DoubleBinaryOperator getInverseOperation() {
		return inverseOperation;
	}

}
