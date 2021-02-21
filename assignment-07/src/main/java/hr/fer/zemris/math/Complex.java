package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of an unmodifiable complex number. All operations that result in a new
 * complex number return the result as a new instance of this class.
 * 
 * @author jankovidakovic
 *
 */
public class Complex {

	private double x; // real part of the complex number
	private double y; // imaginary part of the complex number

	// static fields which are often used as values of complex numbers
	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Constructs a zero complex number.
	 */
	public Complex() {
		x = 0;
		y = 0;
	}

	/**
	 * Constructs a complex number with given parameters
	 * 
	 * @param re real part
	 * @param im imaginary part
	 */
	public Complex(double re, double im) {
		x = re;
		y = im;
	}

	/**
	 * Returns the real part of the complx number
	 * 
	 * @return real part of the complex number
	 */
	public double getReal() {
		return x;
	}

	/**
	 * Returns the imaginary part of the complex number.
	 * 
	 * @return Imaginary part of the complex number.
	 */
	public double getImaginary() {
		return y;
	}

	/**
	 * Returns the module of a complex number.
	 * 
	 * @return module of a complex number.
	 */
	public double module() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns this * c
	 * 
	 * @param  c second operand of multiplication
	 * @return   this*c
	 */
	public Complex multiply(Complex c) {
		return new Complex(x * c.getReal() - y * c.getImaginary(),
				x * c.getImaginary() + y * c.getReal());
	}

	/**
	 * Returns this / c
	 * 
	 * @param  c                        second operand of division. Must not be
	 *                                  zero.
	 * @return                          this / c
	 * @throws IllegalArgumentException if the provided divisor is zero
	 */
	public Complex divide(Complex c) {
		if (c == Complex.ZERO) {
			throw new IllegalArgumentException("Divisor cannot be zero!");
		}
		double denominator = c.module();
		Complex rationalizedThis =
				new Complex(x / denominator, y / denominator);
		Complex rationalizedOther = new Complex(c.getReal() / denominator,
				-1 * c.getImaginary() / denominator);
		return rationalizedThis.multiply(rationalizedOther);
	}

	/**
	 * Returns this + c
	 * 
	 * @param  c second operand of addition
	 * @return   this + c
	 */
	public Complex add(Complex c) {
		return new Complex(x + c.getReal(), y + c.getImaginary());
	}

	/**
	 * Returns this - c
	 * 
	 * @param  c second operand of subtraction
	 * @return   this - c
	 */
	public Complex sub(Complex c) {
		return new Complex(x - c.getReal(), y - c.getImaginary());
	}

	/**
	 * Returns -this
	 * 
	 * @return -this
	 */
	public Complex negate() {
		return new Complex(-x, -y);
	}

	/**
	 * Returns this^n, where n is a non-negative integer.
	 * 
	 * @param  n                        exponent of exponentiation, should be a
	 *                                  non-negative integer.
	 * @return                          this^n
	 * @throws IllegalArgumentException if invalid exponent was provided.
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException(
					"Exponent should be a non-negative integer!");
		}
		double resultModule = Math.pow(module(), n);
		double resultArgument = getArgument() * n;
		return fromModuleAndArgument(resultModule, resultArgument);
	}

	private double getArgument() {
		return Math.atan2(y, x);
	}

	private Complex fromModuleAndArgument(double module, double argument) {
		return new Complex(module * Math.cos(argument),
				module * Math.sin(argument));
	}

	/**
	 * Returns the n-th root of <code>this</code>, where n is positive integer.
	 * 
	 * @param  n radical of the root, valid if positive integer
	 * @return   n-th root of <code>this</code>, as a list of complex numbers
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException(
					"Radical should be a positive integer!");
		}
		List<Complex> roots = new ArrayList<>();
		double rootedModule = Math.pow(module(), 1 / n);
		double rootedArgumentStart = getArgument() / n;
		double rootedArgumentIncrement = Math.PI * 2 / n;

		for (int i = 0; i < n; i++) { // find all roots
			roots.add(fromModuleAndArgument(rootedModule,
					rootedArgumentStart + i * rootedArgumentIncrement));
		}

		return roots;
	}

	/**
	 * Returns the string representation of <code>this</code>, in the form (x +
	 * iy).
	 * 
	 * @return string representation of <code>this<code>
	 */
	@Override
	public String toString() {
		return Double.toString(x) + (y >= 0 ? "+" : "-") + "i"
				+ Double.toString(Math.abs(y));
	}

	/**
	 * Parses the complex number in the given string.
	 * 
	 * @param  input complex number in its string representation. valid syntax
	 *               is a+ib or a-ib. Zero parts can be dropped, but not
	 *               both(empty string is not valid). If there is 'i' present
	 *               but no b was given, it is assumed that it is equal to one.
	 * @return
	 */
	public static Complex parseComplex(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != ' ') {
				sb.append(input.charAt(i));
			}
		}

		String inputWithoutSpaces = sb.toString();
		sb = new StringBuilder();
		int pos = 0;
		double real = 0;
		
		boolean parsingReal = false;
		boolean parsingImaginary = false;
		
		boolean parsedReal = false;
		boolean parsedRealDot = false;
		boolean parsedImaginaryDot = false;
		boolean parsedImaginaryUnit = false;

		while (pos < inputWithoutSpaces.length()) {
			char c = inputWithoutSpaces.charAt(pos);
			if (pos == 0) {
				if (c == 'i') { // pure imaginary
					if (input.length() == 1) { // imaginary unit
						return Complex.IM;
					}
					parsingReal = false;
					parsedReal = true;
					parsingImaginary = true;
				} else if (c == '-') {
					if (inputWithoutSpaces.length() > 1) {
						if (inputWithoutSpaces.charAt(1) == 'i') { // neg imag
							if (inputWithoutSpaces.length() == 2) {
								return Complex.IM_NEG;
							}
							sb.append(c);
							parsingReal = false;
							parsedReal = true;
							parsingImaginary = true;
						} else { // negative real
							sb.append(c);
							parsingReal = true;
							parsingImaginary = false;
						}
					} else { // just minus, invalid
						throw new IllegalArgumentException("Invalid syntax.");
					}
				} else if (Character.isDigit(c)) {
					sb.append(c);
					parsingReal = true;
					parsingImaginary = false;
				}
			} else { // not the first character
				if (parsingReal) {
					if (Character.isDigit(c)) { // digit
						sb.append(c);
					} else if (c == '.') { // dot
						if (!parsedRealDot) {
							parsedRealDot = true;
							sb.append(c);
						} else { // double dot
							throw new IllegalArgumentException(
									"Invalid syntax.");
						}
					} else if (c == '+' || c == '-') { // sign
						parsingReal = false;
						try {
							real = Double.parseDouble(sb.toString());
						} catch (NumberFormatException ex) {
							throw new IllegalArgumentException(
									"Invalid syntax.");
						}
						parsedReal = true;
						sb = new StringBuilder();
						sb.append(c);
						parsingImaginary = true;
					} else { // nothing more can appear
						throw new IllegalArgumentException("Invalid syntax.");
					}
				} else if (parsingImaginary && parsedReal) {
					if (c == 'i') {
						if (parsedImaginaryUnit == false) {
							String sbValue = sb.toString();
							if (!(sbValue.endsWith("+")
									|| sbValue.endsWith("-"))) {// invalid
								throw new IllegalArgumentException(
										"Invalid syntax.");
							}
							parsedImaginaryUnit = true;
							if (pos == inputWithoutSpaces.length() - 1) {
								sb.append("1");
							}
						} else {
							throw new IllegalArgumentException(
									"Invalid syntax.");
						}
					} else if (c == '.') {
						if (parsedImaginaryDot == false) {
							parsedImaginaryDot = true;
							sb.append(c);
						} else { // double dot
							throw new IllegalArgumentException(
									"Invalid syntax.");
						}
					} else if (Character.isDigit(c)) {
						sb.append(c);
					}
				} else { // undefined state
					throw new IllegalArgumentException("Invalid syntax.");
				}
			}
			pos++;
		}
		if (parsingReal) { // pure real
			try {
				return new Complex(Double.parseDouble(sb.toString()), 0);
			} catch (NumberFormatException ex) { // invalid format
				throw new IllegalArgumentException("Invalid syntax.");
			}
		} else if (parsingImaginary) {
			try {
				return new Complex(real, Double.parseDouble(sb.toString()));
			} catch (NumberFormatException ex) { // double dot
				throw new IllegalArgumentException("Invalid syntax.");
			}
		} else { // undefined state
			throw new IllegalArgumentException("Invalid syntax.");
		}
	}

	public static void main(String[] args) {
		System.out.println("bok");
	}
}
