package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * A simple program that calculates the circumference and the area of a rectangle with given length and height
 * parameters. Parameters can be set as command-line arguments, or entered after the program starts.
 *
 * @author jankovidakovic
 */
public class Rectangle {
	
	/**
	 * Prints the circumference and the diameter of a rectangle with given length and height parameters.
	 *
	 * @param a rectangle length
	 * @param b rectangle height
	 */
	public static void printCircumferenceAndArea(double a, double b) {
		double circumference = 2*a + 2*b;
		double area = a*b;
		
		System.out.printf("Pravokutnik širine %s i visine %s ima površinu %s "
				+ "te opseg %s.%n", Double.toString(a), Double.toString(b),
				Double.toString(area), Double.toString(circumference)
		);
	}
	
	/**
	 * Ensures that the parameters are set correctly.
	 *
	 * @param prompt Prompt which is displayed to user when expecting an input
	 * @param sc Scanner object which reads from standard input
	 * @return input value
	 */
	public static double inputParameter(String prompt, Scanner sc) {
		while (true) { //while input is not proper
			
			System.out.printf("%s", prompt);
			String input = sc.nextLine();
			
			try {
				double a = Double.parseDouble(input.trim());
				
				if (a < 0) {
					System.out.println("Rectangle dimension cannot be negative!");
				} else { //correct value inputted
					return a; 
				}
				
			} catch (NumberFormatException ex) { //could not be parsed into double
				System.out.printf("'%s' cannot be parsed as a number.%n", input);
			}
		}
	}
	
	/**
	 * Main method which takes optional command line arguments as rectangle parameters.
	 *
	 * @param args args[0] represents rectangle length, args[1] represents the rectangle height
	 */
	public static void main(String[] args) {
		if (args.length == 2) { 
			try {
				double a = Double.parseDouble(args[0].trim()), 
						b = Double.parseDouble(args[1].trim());
				
				if (a > 0 && b > 0) { //parameters are inputted properly
					printCircumferenceAndArea(a, b);
				} else { //at least one parameters is not a positive number
					System.out.println("Both parameters need to be positive numbers!");
				}
				
			} catch (NumberFormatException ex) {
				System.out.println(args[0] + "and" + args[1] + "cannot be parsed as rectangle parameters.");
			}
		} else if (args.length == 0) { //no command-line arguments - prompt the user for input
			Scanner sc = new Scanner(System.in);
			
			double a = inputParameter("Rectangle length > ", sc),
					b = inputParameter("Rectangle height > ", sc); //input

			printCircumferenceAndArea(a, b); 
			sc.close();
		} else { //wrong number of arguments
			System.out.println("Expected 2 arguments, got " + args.length + "instead.");
		}
	}
}
