package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Implementation of an object that can create a graphical representation
 * of some Lindermayer System, which usually appear in the form of fractals.
 *
 * @author jankovidakovic
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	//private variables
	private double unitLength; //unit length in drawing window
	private double unitLengthDegreeScaler; //scale according to the fractal
	private Vector2D origin; //origin of drawing window
	private double angle; //starting angle of the turtle
	private String axiom; //axiom of the lindermayer system
	private final Dictionary<Character, String> registeredProductions; //productions
	private final Dictionary<Character, Command > registeredCommands; //commands
	
	/**
	 * Constructs a default environment for the drawing of 
	 * one Lindermayer system.
	 */
	public LSystemBuilderImpl() {
		unitLength = 0.1;
		unitLengthDegreeScaler = 1;
		origin = new Vector2D(0, 0);
		angle = 0;
		axiom = "";
		registeredProductions = new Dictionary<>();
		registeredCommands = new Dictionary<>();
		
	}
	
	/**
	 * Constructs a builder and sets all the parameters to the given values.
	 *
	 * @param unitLength unit length in the drawing window
	 * @param unitLengthDegreeScalar can be used for scaling the unit length
	 * @param origin origin in the drawing window
	 * @param angle starting angle 
	 * @param axiom first production
	 * @param registeredProductions available productions
	 * @param registeredCommands available commands
	 */
	private LSystemBuilderImpl(double unitLength, double unitLengthDegreeScalar,
			Vector2D origin, double angle, String axiom, 
			Dictionary<Character, String> registeredProductions,
			Dictionary<Character, Command > registeredCommands) {
		this.unitLength = unitLength;
		this.unitLengthDegreeScaler = unitLengthDegreeScalar;
		this.origin = origin;
		this.angle = angle;
		this.axiom = axiom;
		this.registeredProductions = registeredProductions;
		this.registeredCommands = registeredCommands;
	}
	
	/**
	 * Creates a copy of this instance of the builder and returns it.
	 * All inner values have the same value, but are different objects
	 * so the returned instance is completely independent of the one
	 * upon which the method is called.
	 *
	 * @return copy of the current instance of the LSystemBuilder
	 */
	private LSystemBuilderImpl copy() {
		return new LSystemBuilderImpl(unitLength, unitLengthDegreeScaler,
				origin, angle, axiom, registeredProductions, registeredCommands);
	}
	
	/**
	 * Implementation of Lindermayer system model. Can be used to generate
	 * sequences starting from the axiom up to the desired level. Can also be 
	 * used to draw such sequences, which appear in the form of fractals.
	 *
	 * @author jankovidakovic
	 *
	 */
	private class LSystemImpl implements LSystem {
		
		/**
		 * Generates a sequence from the starting axiom up to the given
		 * level. Each next level is the result of parallel production 
		 * applying on the whole sequence of the previous level. Axiom
		 * is considered level zero.
		 *
		 * @param level level of depth used for generating the sequence
		 * @return sequence generated from the axiom up to the given
		 * level.
		 */
		@Override
		public String generate(int level) {
			StringBuilder sb;
			String currentLevelString = axiom; //level zero
			for (int i = 0; i < level; i++) { //for every level
				sb = new StringBuilder();
				//process the current level
				for (int j = 0; j < currentLevelString.length(); j++) {
					//candidate for the left side of the production
					char productionLeftSide = currentLevelString.charAt(j);
					
					//result of the production as stored in the dictionary
					String productionRightSide = registeredProductions.get(
							productionLeftSide);
					
					if (productionRightSide == null) { //no such production
						sb.append(productionLeftSide);
					} else {
						sb.append(productionRightSide); //generate
					}
				}
				currentLevelString = sb.toString(); //store the current level
			}
			return currentLevelString;
		}
		
		/**
		 * Draws a Lindermayer system in the graphical interface provided
		 * by the painter object. Generates the system up to the given
		 * level.
		 *
		 * @param level level up to which the system will be generated
		 * @param painter Painter used to draw the system
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context ctx = new Context(); //new context for commands
			
			Vector2D angleUnitVector = new Vector2D(1, 0); //default angle
			angleUnitVector.rotate(angle); //rotate so it matches
			
			//create a starting turtle state with default values
			TurtleState beginState = new TurtleState(origin, angleUnitVector, 
					Color.black, unitLength * 
					Math.pow(unitLengthDegreeScaler, level));
			ctx.pushState(beginState);
			
			String sequence = generate(level); //generate the system tree
			for (int i = 0; i < sequence.length(); i++) {
				Command command = registeredCommands.get(sequence.charAt(i));
				if (command != null) { //command exists
					command.execute(ctx, painter); //execute the command
				}
				//command doesnt exist, skip the current character
			}
		}
	}
	
	/**
	 * Returns a new instance of the Lindermayer system.
	 *
	 * @return New instance of the Lindermayer system.
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}
	
	/**
	 * Configures the builder from the given array of strings.
	 * Each string in the array corresponds to the one operation supported
	 * by the builder. Such operations are:
	 * 	origin x y - sets the origin of the drawing interface to the 
	 * 				point which coordinates match the given values.
	 * 				X and Y are decimal numbers not smaller than zero
	 * 				and not greater than one.
	 *  angle a - sets the starting angle of the builder to the given value.
	 *  			Angle is given in degrees.
	 *  unitLength d - sets the unit length of the drawing interface to the
	 *  			given value. D should be not smaller than zero and 
	 *  			not greater than one, as it represents the length
	 *  			relative to the size of the window.
	 *  unitLengthDegreeScaler s - Used for scaling the fractal to the
	 *  			appropriate proportions when drawing different layers
	 *  			of it. 
	 */
	@Override
	public LSystemBuilder configureFromText(String[] arg0) {
		for (String s : arg0) {
			parseDirective(s);
		}
		return copy();
	}

	/**
	 * Parses a given array of strings into a valid turtle command.
	 * Valid turtle commands are as follows:
	 * 		"draw x" - draws a line starting from the turtle's
	 * 					current position, of length x. Changes turtle's
	 * 					position to the end point of the drawn line.
	 * 					X is relative to the drawing window's size.
	 * 		"skip x" - Changes turtle's position to the end point of 
	 * 					the line which starts at turtle's current
	 * 					position and has length X. Does not draw line,
	 * 					just changes position.
	 * 		"scale x" - scales the turtle's unit length by X.
	 * 		"rotate a" - rotates the turtle by the given angle in the
	 * 					positive direction, which is counter-clockwise.
	 * 					Angle is given in degrees.
	 * 		"push" - pushes a current turtle's context to the stack.
	 * 		"pop" - removes the top of the stack containing the previously
	 * 					saved contexts.
	 * 		"color rrggbb" - changes turtle's drawing color to the given
	 * 					value. Value should be given in hexadecimal.
	 *
	 * @param commandTokens Array of strings that contains the command
	 * 	elements, which are tokens separated by whitespace
	 * 					
	 * @return Instance of <code>Command</code> that represents the given
	 * array of strings.
	 * @throws IllegalArgumentException if the given command has invalid
	 * syntax or an unknown command was given.
	 */
	private Command parseCommand(String[] commandTokens) {
		if (commandTokens.length == 0) //no tokens
			return null;
		
		switch(commandTokens[0]) {
		case "draw":
			return new DrawCommand(Double.parseDouble(commandTokens[1]));
		case "skip":
			return new SkipCommand(Double.parseDouble(commandTokens[1]));
		case "scale":
			return new ScaleCommand(Double.parseDouble(commandTokens[1]));
		case "rotate":
			return new RotateCommand(Double.parseDouble(commandTokens[1]));
		case "push":
			return new PushCommand();
		case "pop":
			return new PopCommand();
		case "color":
			try { //maybe it is not given correctly
				return new ColorCommand(new Color(Integer.parseInt(
						commandTokens[1], 16)));
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Cannot parse color.");
			}
			
		default: //not a defined command
			throw new IllegalArgumentException("Unknown command.");
		}
	}
	
	/**
	 * Parses the given array of elements and returns the scalar that is
	 * represented by the array. Array can consist of one element, in which
	 * case it represents a scalar in itself, or it can consist of 3
	 * elements, which represent the scalar in the form x / y (slash being
	 * the second element of the array).
	 * @param scalar Array of strings that represent the scalar
	 * @return Value of the parsed scalar as a double precision number.
	 * @throws IllegalArgumentException if the scalar was not specified
	 * correctly.
	 */
	private double parseScaler(String[] scalar) {

		StringBuilder fullScaler = new StringBuilder();
		for (String s : scalar) {
			fullScaler.append(s); //concatenate
		}
		
		StringBuilder firstDouble = new StringBuilder(); //extract the first number
		
		int i = 0;
		while (fullScaler.charAt(i) != '/') {
			firstDouble.append(fullScaler.charAt(i));
			i++;
		}
		
		i++; //skip the forward slash
		
		StringBuilder secondDouble = new StringBuilder(); //extract the second double
		
		while (i < fullScaler.length()) {
			secondDouble.append(fullScaler.charAt(i));
			i++;
		}
		//remove surrounding whitespace
		firstDouble = new StringBuilder(firstDouble.toString().trim());
		secondDouble = new StringBuilder(secondDouble.toString().trim());
		
		if (secondDouble.toString().isBlank()) { //there was only 1 number
			try {
				return Double.parseDouble(firstDouble.toString());
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Couldn't parse scalar.");
			}
			
		} else { //scalar is in form x / y
			try {
				return Double.parseDouble(firstDouble.toString()) /
						Double.parseDouble(secondDouble.toString());
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Couldn't parse scalar.");
			} catch (ArithmeticException ex2) {
				throw new IllegalArgumentException("Cannot divide by zero!");
			}
			
		}
	}
	
	/**
	 * Parses a given string as a system's configuration directive.
	 * Valid directives are as follows:
	 * 		"origin x y" - sets the drawing window's origin to the point
	 * 			with the given values as cartesian coordinates. Values
	 * 			should be given as a double, and it would make sense (
	 * 			but is not required) to provide values between 0 and 1
	 * 			as the origin is set relative to the window size.
	 * 		"angle a" - sets the starting angle of the first turtle 
	 * 			to the given value. Angle is given in degrees.
	 * 		"unitLength d" - sets the unit length in the drawing window
	 * 			to the given value. Unit length is set relative to the
	 * 			size of the window, e.g. unitLength 0.5 will set the 
	 * 			unit length to the half of the drawing window.
	 * 		"unitLengthDegreeScaler {s | x/y}" - sets the degree scalar
	 * 			to the given value. Value can be given as a single
	 * 			double number, or as a fraction x / y, where x and y
	 * 			are double numbers, and y is not zero. Scaler is used
	 * 			to scale unit length when drawing the different levels
	 * 			of the fractal.
	 * 		"command commandName commandContent" - defines a new turtle
	 * 			command, named "commandName", which does what is specified
	 * 			in commandContent. For valid turtle commands, see 
	 * 			{@link #parseCommand(String[])}. 
	 * 		"axiom a" - sets the axiom to the given string. 
	 * 		"production character production" - defines a new production
	 * 			which will be used to generate the tree starting from the
	 * 			axiom. Each occurrence of "character" within the current
	 * 			level of generation will be replaced by the "production",
	 * 			in each level of generation.
	 * 		"" - directive can be empty, in which case it is skipped.
	 *
	 * @param directive Directive to be parsed
	 * @throws IllegalArgumentException if the syntax of the directive is 
	 * not valid or an unknown directive is given.
	 */
	private void parseDirective(String directive) {
		String[] directiveTokens = directive.split("\\s+");
		switch(directiveTokens[0]) {
		case "origin":
			try {
				origin = new Vector2D(Double.parseDouble(directiveTokens[1]),
						Double.parseDouble(directiveTokens[2]));
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Illegal origin.");
			}
			
			break;
		case "angle":
			try {
				angle = Double.parseDouble(directiveTokens[1]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Illegal angle.");
			}
			break;
		case "unitLength":
			try {
				unitLength = Double.parseDouble(directiveTokens[1]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Illegal unit length.");
			}
			break;
		case "unitLengthDegreeScaler":
			//parseScaler will throw IllegalArgumentException for illegal arg.
			unitLengthDegreeScaler = parseScaler(Arrays.copyOfRange(
					directiveTokens, 1, directiveTokens.length));
			break;
		case "command":
			//parseCommand will throw IllegalArgumentException for illegal arg.
			registeredCommands.put(directiveTokens[1].charAt(0), 
					parseCommand(Arrays.copyOfRange(directiveTokens, 2, 
							directiveTokens.length)));
			break;
		case "axiom":
			axiom = directiveTokens[1];
			break;
		case "production":
			registeredProductions.put(directiveTokens[1].charAt(0),
					directiveTokens[2]);
			break;
		case "":
			break;
		default:
			throw new IllegalArgumentException("Unknown directive.");
		}
	}
	
	/*
	 * Following methods are another way to give directives to the system.
	 * They all return the hard copy of the system where every parameter
	 * is the same but the new directive is also registered and applied.
	 * This mechanism allows command chaining, such as :
	 * 		LSystemBuilderImpl builder = new LSystemBuilderImpl;
	 * 		builder.setOrigin(0, 0)
	 * 			.setAngle(0)
	 * 			.setAxiom(F)
	 * 			.setProduction(F FF);
	 */
	
	/**
	 * Adds the given command into the list of registered commands that can
	 * be passed to the turtle.
	 * Returns a hard copy of the system, with the new command registered.
	 *
	 * @param arg0 name of the command
	 * @param arg1 content of the command 
	 * @return hard copy of the system with the new command registered
	 * @throws IllegalArgumentException if invalid command was specified.
	 */
	@Override
	public LSystemBuilder registerCommand(char arg0, String arg1) {
		//parseCommand will throw IllegalArgumentException for invalid command
		registeredCommands.put(arg0, parseCommand(arg1.split("\\s+")));
		return copy();
	}

	/**
	 * Adds the given production to the list of registered productions
	 * that can be performed when generating the ending sequence from
	 * the axiom. Returns a hard copy of the system containing the 
	 * registered production.
	 *
	 * @param arg0 left side of the production
	 * @param arg1 right side of the production
	 * @return hard copy of the system with the given production registered.
	 */
	@Override
	public LSystemBuilder registerProduction(char arg0, String arg1) {
		registeredProductions.put(arg0, arg1);
		return copy();
	}

	/**
	 * Sets the angle to the given value. Returns a hard copy with the 
	 * angle set to the given value.
	 *
	 * @param arg0 angle to be set
	 * @return hard copy of the system with the angle set to the given
	 * value.
	 */
	@Override
	public LSystemBuilder setAngle(double arg0) {
		angle = arg0;
		return copy();
	}

	/**
	 * Sets the axiom to the given string. Returns a hard copy of the system
	 * with the new axiom set.
	 *
	 * @param arg0 new axiom to be set
	 * @return hard copy of the system with axiom set to the given value.
	 */
	@Override
	public LSystemBuilder setAxiom(String arg0) {
		axiom = arg0;
		return copy();
	}

	/**
	 * Sets the origin to the point with given cartesian coordinates.
	 * Returns a hard copy of the system with new origin.
	 *
	 * @param arg0 x-coordinate of the new origin
	 * @param arg1 y-coordinate of the new origin
	 * @return hard copy of the system with the new origin set.
	 */
	@Override
	public LSystemBuilder setOrigin(double arg0, double arg1) {
		origin = new Vector2D(arg0, arg1);
		return copy();
	}

	/**
	 * Sets the unit length to the given value. Returns a hard copy of the
	 * system with new unit length set.
	 *
	 * @param arg0 new unit length
	 * @return hard copy of the system with new unit length set.
	 */
	@Override
	public LSystemBuilder setUnitLength(double arg0) {
		unitLength = arg0;
		return copy();
	}

	/**
	 * Sets the unit length degree scalar to the given value. Returns
	 * a hard copy of the system with the new scalar set.
	 * @param arg0 new unit length degree scalar
	 * @return hard copy of the system with the new unit length degree scalar
	 * set. 
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double arg0) {
		unitLengthDegreeScaler = arg0;
		return copy();
	}

}
