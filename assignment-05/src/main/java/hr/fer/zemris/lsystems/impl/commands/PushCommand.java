package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command that pushes a new state to the stack. Newly pushed state is a 
 * copy of the last state.
 *
 * @author jankovidakovic
 *
 */
public class PushCommand implements Command {
	
	/**
	 * Pushes the current turtle state of the given context, to the
	 * top of the context stack.
	 *
	 * @param ctx context from which the current turtle state is extracted
	 * and then pushed to the top of the stack.
	 * @param painter isn't used in this command
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.pushState(ctx.getCurrentState().copy()); //push a copy of last state
	}
}
