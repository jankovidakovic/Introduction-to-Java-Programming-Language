package hr.fer.zemris.java.gui.calc;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Model of a simple button which performs some simple action when clicked, such
 * as changes the inner state of the calculator.
 * 
 * @author jankovidakovic
 *
 */
public class SimpleButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the simple button with the given name, which performs the
	 * given action when pressed.
	 * 
	 * @param name    Name of the button
	 * @param onClick Action that is performer on click.
	 */
	public SimpleButton(String name, ActionListener onClick) {
		super(name);
		addActionListener(onClick);
	}
}
