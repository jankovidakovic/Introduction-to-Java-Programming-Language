package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.math.Vector2D;

/**
 * Command that draws a line in the given painter object. Line is drawn from
 * the turtle's current position, in the direction of turtle's current angle,
 * and is of length equal to the turtle's unit length, scaled by the step
 * which is passed in the constructor. In the process of drawing, turtle
 * changes the position to the end point of the drawn line, and all the other
 * turtle parameters stay the same.
 *
 * @author jankovidakovic
 *
 */
public class DrawCommand implements Command {

	private final double step;
	
	/**
	 * Initializes the command to draw the line using the given step.
	 *
	 * @param step Step by which the unit length will be scaled when
	 * drawing.
	 */
	public DrawCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Draws a line using the given turtle's context and the given
	 * painter. Line is drawn from the turtle's current position, in the 
	 * direction of turtle's current angle, and is of length equal to the 
	 * turtle's unit length, scaled by the step which is passed in the 
	 * constructor. In the process of drawing, turtle changes the position 
	 * to the end point of the drawn line, and all the other turtle 
	 * parameters stay the same.
	 *
	 * @param ctx context from which the current state is used to draw
	 * @param painter object that draws lines 
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		//calculate the new position
		Vector2D startPosition = ctx.getCurrentState().getPosition();
		
		Vector2D endPosition = startPosition.translated( //translate
				ctx.getCurrentState()	//current state
					.getAngle()			//in the direction of the angle
					.scaled(step)		//scaled by the step
					.scaled(ctx.getCurrentState().getUnitLength()))
					; //scaled also by the unit length 
		
		//draw a line
		painter.drawLine(startPosition.getX(), 
						startPosition.getY(), 
						endPosition.getX(), 
						endPosition.getY(), 
						ctx.getCurrentState().getColor(), 1f);
		
		//set the new position
		ctx.getCurrentState().setPosition(endPosition);
	}
}
