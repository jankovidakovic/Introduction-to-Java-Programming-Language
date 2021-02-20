package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command which changes color of the current turtle state. Color is
 * accepted as a constructor parameter, and is changed by invocation
 * of the <code>execute()</code> method.
 *
 * @author jankovidakovic
 *
 */
public class ColorCommand implements Command {
	
	private final Color color; //color
	
	/**
	 * Initializes the command to change the turtle's color to the 
	 * given value.
	 *
	 * @param color new color
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}
	
	/**
	 * Sets the color of the turtle to the value passed in the constructor.
	 *
	 * @param ctx isn't used in this command
	 * @param painter isn't used in this command
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color); //change color
	}
}
