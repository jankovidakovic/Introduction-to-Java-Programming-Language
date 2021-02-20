package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command that removes the last stored state of the turtle.
 *
 * @author jankovidakovic
 *
 */
public class PopCommand implements Command {
	/**
	 * Removes the top of the context stack.
	 *
	 * @param ctx Context from which the top element of the stack will
	 * be removed
	 * @param painter isn't used in this command
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState(); //remove state
	}
}
