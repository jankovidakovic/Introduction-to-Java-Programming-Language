package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;


/**
 * Program that demonstrates the capabilities of ObjectStack class.
 * Program accepts one command line argument enclosed in double quotes,
 * as form of a valid postfix expression. Program uses ObjectStack to
 * calculate expression and prints the result on standard output.
 * Valid expression consists of integer numbers and operators, separated
 * by at least one whitespace character. Supported operators are
 * +(addition), -(subtraction) /(division), *(multiplication) and %(remainder of
 * integer division).
 * Usage example:
 * 		java -cp target/classes hr.fer.zemris.java.custom.collections.demo.StackDemo "8 -2 / -1 *"
 * 		output: "Expression evaluates to 4."
 * 		
 * @author jankovidakovic
 */
public class StackDemo {

	/**
	 * Main method which is called when program executes.
	 *
	 * @param args command line arguments, valid is one string in double quotes.
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) { //wrong number of arguments supplied
			System.out.println("Program accepts the expression as "
					+ "one command line argument enclosed in double quotes.");
			return;
		}
		
		String expression = args[0];
		String[] expressionElements = expression.split("\\s+");//operands and operators
		ObjectStack stack = new ObjectStack(); //used for postfix calculations

		//current element
		for (String expressionElement : expressionElements) {
			try {    //checking if it's a number
				int number = Integer.parseInt(expressionElement);
				stack.push(number);
			} catch (NumberFormatException notANumber) { //must be an operand
				int firstOperand, secondOperand;
				try {    //extracting the 2 operands
					secondOperand = (Integer) stack.pop(); //second is on the to
					firstOperand = (Integer) stack.pop();

				} catch (EmptyStackException | ClassCastException invalidExpression) {
					System.out.println("Invalid expression.");
					return; //the program cannot recover from this error
				}
				int result; //result of the calculation
				//check the operand
				switch (expressionElement) {
					case "+" -> result = firstOperand + secondOperand;
					case "-" -> result = firstOperand - secondOperand;
					case "*" -> result = firstOperand * secondOperand;
					case "/" -> {
						if (secondOperand == 0) { //cannot divide by zero
							System.out.println("Invalid expression: division by zero.");
							return; //critical error
						}
						result = firstOperand / secondOperand;
					}
					case "%" -> {
						if (secondOperand == 0) { //cannot calculate that
							System.out.println("Invalid expression: remainder of integer"
									+ " division by zero.");
							return; //critical error
						}
						result = firstOperand % secondOperand;
					}
//something went wrong
					default -> {
						System.out.println("Invalid postfix notation.");
						return;
					}
				}
				stack.push(result);

			}
		}
		int finalResult;
		try {
			finalResult = (Integer)stack.pop();	//should be on top of the stack
		} catch (EmptyStackException|NumberFormatException exception) {
			System.out.println("Invalid expression.");
			return; //something went wrong during the calculation
		}
		if (stack.isEmpty()) { //stack should be empty
			System.out.println("Expression evaluates to " + finalResult + ".");
		} else { //something went wrong
			System.out.println("Invalid expression");
		}
		
	}
}
