package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command that modifies the current state of the turtle by rotating its
 * direction vector by the given angle. Angle of rotation is provided in 
 * the constructor. Positive direction of x-axis is defined as an angle of
 * zero degrees, and positive direction is counter-clockwise. Angles are
 * specified in degrees.
 *
 * @author jankovidakovic
 *
 */
public class RotateCommand implements Command {
	
	private final double angle; //angle of rotation
	
	/**
	 * Constructor which accepts the angle of rotation and stores it.
	 *
	 * @param angle Angle of rotation
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Rotates the current turtle state of the given context by the angle
	 * that was passed in the constructor.
	 *
	 * @param ctx context from which the current state is rotated
	 * @param painter isn't used in this command
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setAngle( //change the angle
			ctx.getCurrentState() 	//current state
			.getAngle()				//current angle
			.rotated(				//rotate
					angle * Math.PI / 180	//conversion to radians
			) 
		);
	}
}
