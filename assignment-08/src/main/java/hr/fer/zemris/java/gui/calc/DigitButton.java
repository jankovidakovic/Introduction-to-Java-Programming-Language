package hr.fer.zemris.java.gui.calc;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Version of JButton that is a digit in the calculator.
 * 
 * @author jankovidakovic
 *
 */
public class DigitButton extends JButton {

	private static final long serialVersionUID = 1L;

	private int digit;

	/**
	 * Constructs a button object that represents a given digits, and executes
	 * the given action upon pressing.
	 * 
	 * @param digit digit that the button displays
	 * @param onClick action which is performed on click of the button
	 */
	public DigitButton(int digit, ActionListener onClick) {
		super(Integer.toString(digit));

		this.digit = digit;
		super.setFont(super.getFont().deriveFont(30f));

		addActionListener(onClick);
	}

	/**
	 * Returns the digit that the button represents.
	 * 
	 * @return digit of the button
	 */
	public int getDigit() {
		return digit;
	}

}
