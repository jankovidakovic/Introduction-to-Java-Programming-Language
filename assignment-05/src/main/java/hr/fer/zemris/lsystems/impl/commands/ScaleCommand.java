package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command that scales the unit length of the turtle by the given factor.
 * Factor of scaling is provided in the constructor.
 *
 * @author jankovidakovic
 *
 */
public class ScaleCommand implements Command {

	private final double factor;	//scaling factor
	
	public ScaleCommand(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Scales the unit length of the current state of the given context
	 * by the scalar that was passed in the constructor.
	 *
	 * @param ctx context from which the unit length of the current state
	 * is scaled
	 * @param painter isn't used in this command
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setUnitLength(	//modifies the current unit length
				ctx.getCurrentState().getUnitLength() * factor);
	}
}
