package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that calculates the factorial of an integer in the range of [3, 20]. Program is terminated when the
 * user enters the keyword "end".
 *
 * @author jankovidakovic
 */
public class Factorial {

	/**
	 * Calculates the factorial of a given number.
	 *
	 * @param n given number
	 * @return n!
	 * @throws IllegalArgumentException If the given number is non-positive or its factorial is not representable by a
	 * 64-bit long.
	 * @author jankovidakovic
	 */
	public static long calculateFactorial(int n) {
		if (n < 0) { //Cannot be negative
			throw new IllegalArgumentException("Factorial is undefined for negative numbers!");
		} else {
			long result = 1;
			for (int i = 2; i <= n; i++) {
				result *= i;
				if (result < 0) {//Overflow
					throw new IllegalArgumentException("Factorial of a given number overflows the 64-bit " +
							"representation.");
				}
			}
			return result;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Please enter an integer not smaller than 3 and not greater than 20 > ");
			String input = sc.nextLine();
			
			if (input.equals("end")) { //program termination
				System.out.println("Goodbye!");
				break;
			} else {
				try {
					int n = Integer.parseInt(input.trim()); //remove the whitespace
					if (n < 3 || n > 20) { // out of range
						System.out.println("'" + n + "' is not in the allowed range of [3, 20]!");
					} else { //within range
						System.out.printf("%d! = %d%n", n, calculateFactorial(n));
					}
				} catch (NumberFormatException ex) { //Integer.parseInt() threw an exception
					System.out.printf("'%s' is not an integer.%n", input);
				}
			}
		}
		
		sc.close();
	}
}
