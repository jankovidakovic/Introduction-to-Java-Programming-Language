package hr.fer.zemris.java.hw02;

/**
 * Class that represents a complex number, represented by its
 * real and imaginary part. Offers static methods for conversion
 * between complex number formats, and non-static methods for
 * elementary operations with the complex numbers, which include:
 * addition, subtraction,, multiplication, division and exponentiation
 * (both powers and roots).
 *
 * @author jankovidakovic
 *
 */
public class ComplexNumber {

	//private variables
	private final double real;		//real part of the complex number
	private final double imaginary;	//imaginary part
	
	/**
	 * Constructs a complex number with given real and imaginary parts.
	 * @param real Real part of complex number
	 * @param imaginary Imaginary part of the complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Converts pure real number to the complex number format of this class.
	 *
	 * @param real Real number
	 * @return instance of ComplexNumber class with real part equal to the
	 * 			given value, and imaginary part equal to zero.
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0);
	}
	
	/**
	 * Converts pure imaginary number with given value to the complex
	 * number format of this class.
	 *
	 * @param imaginary value of imaginary number
	 * @return instance of ComplexNumber with real part equal to zero,
	 * 			and imaginary part equal to the given value.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0.0, imaginary);
	}
	
	/**
	 * Creates a complex number in its algebraic representation,
	 * from given magnitude and angle of its trigonometric representation
	 *
	 * @param magnitude Magnitude of trigonometric representation of a complex number.
	 * @param angle Angle of polar representation of a complex number
	 * @return instance of <code>ComplexNumber</code> which has the same 
	 * 		magnitude as a given value, and same angle as a given value.
	 */
	public static ComplexNumber fromMagnitudeAndAngle (double magnitude, double angle) {
		
		//z = r(cos(phi) + i sin(phi))
		
		//x = r * cos(phi)
		double real = magnitude * Math.cos(angle);
		//y = r * sin(phi)
		double imaginary = magnitude * Math.sin(angle);
		return new ComplexNumber(real, imaginary);
	}
	
	//the following private entities serve as helpers for the method
	//which parses a string to produce a complex number
	
	/**
	 * Enumeration which represents the states of the parser.
	 * State corresponds to the last read character in parsing.
	 */
	private enum parserState {
		BEGIN, 	//beginning of parsing
		SIGN,	//last read was '+' or '-'
		DIGIT,	//last read was some digit
		DOT,	//last read was a dot
		END,	//end of string
		ERROR;	//error state
	}
	
	/**
	 * Helper method which determines whether a given character
	 * is a digit.
	 *
	 * @param c Character to be checked
	 * @return <code>true</code> if given character is a digit,
	 * 			<code>false</code> otherwise.
	 */
	private static boolean isDigit(char c) {
		return switch (c) {
			//it is a digit
			case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
			//it is something else
			default -> false;
		};
	}
	
	/**
	 * Helper method which determines whether a given character
	 * is a sign (+ or -)
	 *
	 * @param c Character to be checked.
	 * @return <code>true</code> if given character is a sign,
	 * 			<code>false</code> otherwise.
	 */
	private static boolean isSign(char c) {
		return (c == '+' || c == '-');
	}
	
	/**
	 * Helper method which determines whether a given character
	 * is a dot.
	 *
	 * @param c Character to be checked.
	 * @return <code>true</code> if given character is a dot,
	 * 			<code>false</code> otherwise.
	 */
	private static boolean isDot(char c) {
		return c == '.';
	}
	
	/**
	 * Helper method which determines whether a given character
	 * is an imaginary unit, as represented with 'i'.
	 *
	 * @param c Character to be checked.
	 * @return <code>true</code> if given character is an 'i',
	 * 			<code>false</code> otherwise.
	 */
	private static boolean isImaginaryUnit(char c) {
		return c == 'i';
	}
	
	/**
	 * Converts a string representation of a complex number to the 
	 * actual instance of this class.
	 *
	 * @param s Possible string representation of a complex number.
	 * Valid string representation consists of at most two double numbers, 
	 * separated by the sign + or - . Second double number must be trailed 
	 * by character 'i' which represents an imaginary unit, the square root of -1.
	 * Leading plus sign is allowed. If real or imaginary part is equal to zero,
	 * it can be left out. String must contain no spaces.
	 * Example of valid Strings: "1+2i", "-i", "+2.3".
	 * Example of invalid Strings: "-1-2", "+1+-2i".
	 * @return instance of <code>ComplexNumber</code> class representing the
	 * 		given complex number.
	 * @throws IllegalArgumentException if given string is not valid
	 */
	public static ComplexNumber parse(String s) {
		String realPart = "";	//stores the real part of the number
		String imaginaryPart = "";//stores the imaginary part of the number
		String tempString = "";	//stores the parsed string when it is uncertain
								//what it contains yet
		
		parserState state = parserState.BEGIN;	//beginning of the parsing
		boolean possibleRealPart = true;	//true if it is possible to be currently
											//reading the real part of the number
		for (int i = 0; i < s.length(); i++) {
			char currentChar = s.charAt(i);
			if (state == parserState.BEGIN) {		//beginning of the string
				if (isDigit(currentChar)) {			//next is digit
					tempString += currentChar;		//it could be real or imaginary
					state = parserState.DIGIT;		
				} else if (isSign(currentChar)) {	//next is sign
					tempString += currentChar;		//could be real or imag
					state = parserState.SIGN;
				} else if (isImaginaryUnit(currentChar)) { //read 'i'
					imaginaryPart = "1.0";		//given string was "i" which is 1i
					state = parserState.END;	//if string is valid, this is the end
				} else {
					state = parserState.ERROR;	//invalid character
				}
			} else if (state == parserState.SIGN) {
				if (isDigit(currentChar)) {			//next is digit
					tempString += currentChar;		//could be real or imaginary
					state = parserState.DIGIT;
				} else if (isImaginaryUnit(currentChar)) { //next is 'i'
					if (tempString.endsWith("+")) {	//last read was '+'
						imaginaryPart = "1.0";		//string ends with "+i"
					} else {
						imaginaryPart = "-1.0";		//string ends with "-i"
					}
					state = parserState.END;	//if string is valid, this is the end
				} else {
					state = parserState.ERROR;	//invalid character
				}
				
			} else if (state == parserState.DIGIT) {
				if (isDigit(currentChar)) {	//continue reading digits
					tempString += currentChar;
				} else if (isDot(currentChar)) {
					tempString += currentChar;
					state = parserState.DOT;	//switch state
				} else if (isSign(currentChar)) {
					if (possibleRealPart) {	//sign is the beginning of imaginary part
						realPart = tempString;	//save the real part
						tempString = "";
						tempString += currentChar;
						possibleRealPart=false;	//real part is over
						state = parserState.SIGN;
					} else {	//something went wrong
						state = parserState.ERROR;
					}
				} else if (isImaginaryUnit(currentChar)) {
					imaginaryPart = tempString;	//save the imaginary part
					state = parserState.END;	//should be the end
				}
			} else if (state == parserState.DOT) {
				if (isDigit(currentChar)) {	//only a digit can come after the dot
					tempString += currentChar;
					state = parserState.DIGIT;
				} else {	//otherwise string is not valid
					state = parserState.ERROR;
				}
			} else if (state == parserState.END) {	//should never come to this
				state = parserState.ERROR;	//if its the end state, whatever
					//character read represents an error in the given string
			} else if (state == parserState.ERROR) { //no need in parsing further
				break;
			}
		}
		
		if (state == parserState.ERROR) {
			throw new IllegalArgumentException("Invalid string specified.");
		} else {
			if (realPart.isEmpty()) { //parseDouble cannot accept empty string
				if (!imaginaryPart.isEmpty()) {	//pure imaginary
					realPart = "0.0";
				} else if (!tempString.isEmpty()) { //parser ended in non-END state
					realPart = tempString;
				}
			}
			if (imaginaryPart.isEmpty()) {
				if (!realPart.isEmpty()) {	//pure real
					imaginaryPart = "0.0";
				} //other case is covered in the other if block
			}
			try {
				return new ComplexNumber( Double.parseDouble(realPart)
								, Double.parseDouble(imaginaryPart));
			} catch (NumberFormatException ex) { //for any unaccounted errors
				throw new IllegalArgumentException ("Invalid string specified.");
			}
		}
		
	}
	
	
	/**
	 * Retrieves the real part of the complex number.
	 *
	 * @return real part of the complex number.
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Retrieves the imaginary part of the complex number.
	 *
	 * @return imaginary part of the complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Retrieves the magnitude of trigonometric representation of a complex number.
	 *
	 * @return Magnitude 
	 */
	public double getMagnitude() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Retrieves the angle of trigonometric representation of a complex number.
	 *
	 * @return Angle from 0 to 2Pi
	 */
	public double getAngle() {
		if (real == 0) { //number is pure imaginary
			if (imaginary == 0) return Double.NaN; //0 has no defined angle
			else {
				double angle = Math.PI / 2; //positive segment of imaginary line
											//has scope of Pi/2
				return (imaginary > 0 ? angle : -angle); //negative is -Pi/2
			}
		} else if (imaginary == 0) { //number is pure real
			return (real > 0 ? 0 : Math.PI);//if positive, the angle is 0
											//if negative, the angle is Pi
		} else { //number is fully complex
			
			double angle = Math.atan(imaginary / real); //[-Pi/2, Pi/2]
			if (real < 0) { //correction for 2nd and 3rd quadrant
				angle += Math.PI;
			} else if (imaginary < 0) { //correction for 4th quadrant
				angle += 2 * Math.PI;
			}
			return angle;
		}
		
	}
	
	/**
	 * Performs the operation of addition with the given complex number.
	 * First operand is the object on which the method is being called.
	 *
	 * @param secondOperand Second operand of addition.
	 * @return Result of the addition, as represented by a complex number.
	 */
	public ComplexNumber add(ComplexNumber secondOperand) {
		return new ComplexNumber(real + secondOperand.getReal(),
						imaginary + secondOperand.getImaginary());
	}
	
	/**
	 * Performs the operation of subtraction with the given complex number.
	 * First operand is the object on which the method is being called.
	 *
	 * @param secondOperand Second operand of subtraction.
	 * @return Result of the subtraction, as represented by a complex number.
	 */
	public ComplexNumber sub(ComplexNumber secondOperand) {
		return new ComplexNumber(real - secondOperand.getReal(),
				imaginary - secondOperand.getImaginary());
	}
	
	
	/**
	 * Performs the operation of multiplication with the given complex number.
	 * First operand is the object on which the method is being called.
	 *
	 * @param secondOperand Second operand of multiplication.
	 * @return Result of multiplication, as represented by a complex number.
	 */
	public ComplexNumber mul(ComplexNumber secondOperand) {
		//(a+bi)*(c+di) = (ac-bd) + (ad+bc)i
		double realPart = real * secondOperand.getReal()
				- imaginary * secondOperand.getImaginary();
		double imaginaryPart = real * secondOperand.getImaginary()
				+ imaginary * secondOperand.getReal();
		return new ComplexNumber(realPart, imaginaryPart);
	}
	
	/**
	 * Performs the operation of division with the given complex number.
	 * First operand is the object on which the method is being called.
	 *
	 * @param secondOperand Second operand of division. Shall not be zero.
	 * @return Result of division, as represented by a complex number.
	 * @throws IllegalArgumentException if given divisor is zero.
	 */
	public ComplexNumber div(ComplexNumber secondOperand) {
		// (a + bi) / (c + di) = (a + bi)(c - di) / (c^2 + d^2)
		if (secondOperand.getReal() == 0.0
			&& secondOperand.getImaginary() == 0.0) { //given number is zero
			throw new IllegalArgumentException("Can't divide by zero.");
		}
		double rationalizedDenominator = secondOperand.getMagnitude();
		
		// (a+bi)/(c^2+d^2) 
		ComplexNumber firstMultiplicand = new ComplexNumber(
			real / rationalizedDenominator, imaginary / rationalizedDenominator);
		
		// (c-di)/(c^2+d^2)
		ComplexNumber secondMultiplicand = new ComplexNumber(
				secondOperand.getReal() / rationalizedDenominator,
				-secondOperand.getImaginary() / rationalizedDenominator);
		
		return firstMultiplicand.mul(secondMultiplicand);
	}
	
	/**
	 * Performs the exponentiation of a given complex number.
	 *
	 * @param n The exponent. Must be a non-negative integer.
	 * @return Complex number raised to the power of n.
	 * @throws IllegalArgumentException If exponent is invalid.
	 */
	public ComplexNumber power(int n) {
		if (n < 0) { //invalid exponent
			throw new IllegalArgumentException("Invalid exponent.");
		}
		// |z^n| = |z|^n
		double powerMagnitude = Math.pow(this.getMagnitude(), n);
		
		// arg(z^n) = n*arg(z)
		double powerAngle = this.getAngle() * n;
		
		return ComplexNumber.fromMagnitudeAndAngle(
				powerMagnitude, powerAngle);
				
	}
	
	/**
	 * Finds all the n-th roots of a complex number.
	 *
	 * @param n degree of root. Must be a natural number.
	 * @return Array of n-th roots of a complex number. Numbers are
	 * 			ordered by their angle, starting from the smallest.
	 * @throws IllegalArgumentException if degree of root is invalid.
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Invalid root degree.");
		}
		
		ComplexNumber[] roots = new ComplexNumber[n]; //stores the roots 
		
		// |z^(1/n)| = |z|^(1/n)
		double rootMagnitude = Math.pow(getMagnitude(), (double)1/n);
		
		// arg(z^(1/n)) = {2kPI / n, k € [0,1,...,n-1]}
		double rootStartingAngle = this.getAngle() / n;
		double rootAngleIncrement = 2 * Math.PI / n;
		for (int k = 0; k < n; k++) {	//generate all roots
			roots[k] = ComplexNumber.fromMagnitudeAndAngle(
					rootMagnitude, rootStartingAngle + k * rootAngleIncrement);
		}
		return roots;
	}
	
	/**
	 * Creates a string representation of a complex number.
	 *
	 * @return String representation of a complex number.
	 */
	public String toString() {
		String resultString;
		if (Math.abs(real - 0) <= 1E-15) { //real is 0
			if (Math.abs(imaginary - 0) <= 1E-15) { //imaginary is 0
				resultString = Double.toString(0.0);
			} else { //pure imaginary
				resultString = Double.toString(imaginary) + "i";
			}
		} else {
			resultString = Double.toString(real);
			if (imaginary > 1E-15) { //positive imaginary
				resultString += "+";
			} //minus sign will be added by Double.parseDouble()
			if (Math.abs(imaginary - 0) > 1E-15) { //imaginary not 0
				resultString += Double.toString(imaginary);
				resultString += "i";
			}
		}
		return resultString;
	}
	
}

