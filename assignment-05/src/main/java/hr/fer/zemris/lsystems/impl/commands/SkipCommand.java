package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.math.Vector2D;

/**
 * Command that moves the turtle position. New position is calculated
 * as if the turtle was drawing a line in the same way as with draw
 * command, but not line is actually drawn.
 *
 * @author jankovidakovic
 *
 */
public class SkipCommand implements Command {
	
	private final double step;
	
	/**
	 * Initializes the command to change the position using the given step
	 *
	 * @param step Step used for scaling the turtle shift
	 */
	public SkipCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Changes the given context's current state's position to the one
	 * that is obtained by translating the current position in the same
	 * way as with the draw command.
	 *
	 * @param ctx context from which the current turtle state's position
	 * is changed
	 * @param painter isn't used in this command
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Vector2D endPosition = ctx.getCurrentState().getPosition()
				.translated( //translate the position vector
					ctx.getCurrentState() //current state
					.getAngle() //current angle
					.scaled(step)); //scaled by the size of the shift
		
		ctx.getCurrentState().setPosition(endPosition); //no drawing 
	}
}
