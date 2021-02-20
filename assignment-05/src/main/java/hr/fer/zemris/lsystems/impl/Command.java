package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Models a single command which can be passed to the turtle.
 *
 * @author jankovidakovic
 *
 */
@FunctionalInterface
public interface Command {
	
	/**
	 * Executes a command, possibly using given objects as resources.
	 * @param ctx Context which can be used in command execution.
	 * @param painter Painter which can be used in command execution.
	 */
	void execute(Context ctx, Painter painter);
}
