package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;

/**
 * Verstion of JButton that is the unary operator in the calculator
 * 
 * @author jankovidakovic
 *
 */
public class UnaryOperationButton extends JButton {

	private static final long serialVersionUID = 1L;

	private DoubleUnaryOperator operation;
	private DoubleUnaryOperator inverseOperation;

	/**
	 * Action listener which deals with button click. When clicked, operation or
	 * it's inverse operation is applied to the current calculator's value, and
	 * the result is set as the new value. Operation of setting a calculator to
	 * some value puts it in an uneditable state, meaning it does not accept
	 * further digits as input, unless something else happens that puts it back
	 * into an editable state.
	 */

	/**
	 * Constructs a new unary operation button with the given name, which
	 * performs the given operation or inverse operation upon click.
	 * 
	 * @param name             Name of the unary operation
	 * @param operation        Operation that is performed
	 * @param inverseOperation Inverse operation
	 */
	public UnaryOperationButton(String name, DoubleUnaryOperator operation,
			DoubleUnaryOperator inverseOperation, ActionListener onClick) {
		super(name);

		this.operation = operation;
		this.inverseOperation = inverseOperation;

		addActionListener(onClick);
	}

	/**
	 * Returns the operation that the button is supposed to perform
	 * 
	 * @return operation performed on the click of a button
	 */
	public DoubleUnaryOperator getOperation() {
		return operation;
	}

	/**
	 * Returns the operation that is performed if the button is clicked, and
	 * inverse checkbox is selected, meaning the inverse operation is applied.
	 * 
	 * @return Operation that is performed when wanting the inverse operation
	 */
	public DoubleUnaryOperator getInverseOperation() {
		return inverseOperation;
	}

}
