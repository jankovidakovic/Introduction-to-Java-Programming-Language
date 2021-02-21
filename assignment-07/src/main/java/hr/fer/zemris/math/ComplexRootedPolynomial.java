package hr.fer.zemris.math;

import java.math.BigInteger;

/**
 * Model of a rooted complex polynomial, in the form of z0 * (z - z1) * (z - z2)
 * * ... * (z - zn), where z1 through zn are polynomial's roots, and z0 is a
 * constant.
 * 
 * @author jankovidakovic
 *
 */
public class ComplexRootedPolynomial {

	private Complex constant;// constant of the polynomial
	private Complex[] roots; // roots of the polynomial

	/**
	 * Constructs a polynomial with given parameters
	 * 
	 * @param constant constant by which the normal polynomial is multiplied.
	 * @param roots    roots of the polynomial
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = constant;
		this.roots = roots;
	}

	/**
	 * Computes the polynomial at given point z
	 * 
	 * @param  z point at which the polynomial will be computed
	 * @return   value of the polynomial at given point, as a complex number.
	 */
	public Complex apply(Complex z) {
		Complex result = constant;
		for (int i = 0; i < roots.length; i++) {
			result = result.multiply(z.sub(roots[i]));
		}
		return result;
	}

	// converts this representation to ComplexPolynomial type
	public ComplexPolynomial toComplexPolynom() {
		Complex[] factors = new Complex[roots.length + 1];
		for (int i = 0; i < factors.length; i++) {
			factors[i] = Complex.ZERO;
		}
		int rootSubsets = (int) Math.pow(2, roots.length);
		// iterates through all subsets of roots
		for (long bitmask = 0; bitmask < rootSubsets; bitmask++) {
			Complex temporaryResult = Complex.ONE;
			// checking which roots are in current subse
			for (int j = 0; j < roots.length; j++) { //check all bits
				boolean isFactor = BigInteger.valueOf(bitmask).testBit(j);
				if (isFactor) {
					temporaryResult = temporaryResult.multiply(roots[j]);
				}
			}
			int subsetSize = Long.bitCount(bitmask);
			if (subsetSize % 2 == 0) {
				temporaryResult = temporaryResult.multiply(Complex.ONE_NEG);
			}
			factors[subsetSize] =
					factors[subsetSize]
					.add(temporaryResult.multiply(constant));
		}
		return new ComplexPolynomial(factors);
	}

	/**
	 * Returns the string representaion of this polynomial, in the form
	 * (z0)*(z-(z1))*(z-(z2))*...*(z-(zn)).
	 * 
	 * @return string representation of this polynomial
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + constant + ")");
		sb.append("*");
		for (int i = 0; i < roots.length; i++) {
			sb.append("(z-(" + roots[i] + "))");
			if (i != roots.length - 1) {
				sb.append("*");
			}
		}
		return sb.toString();
	}

	/**
	 * Finds index of closest root for given complex number z that is within
	 * treshold; if there is no such root, returns -1 first root has index 0,
	 * second index 1, etc
	 * 
	 * @param  z                        argument of the polynomial
	 * @param  treshold                 maximum distance of the closest root
	 * @return                          index of closest root within treshold,
	 *                                  of -1 if there is no such root.
	 * @throws IllegalArgumentException if treshold is not a non-negative
	 *                                  number.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		if (treshold < 0) {
			throw new IllegalArgumentException(
					"Treshold should be a non-negative number!");
		}
		double minDistance = 0;
		int minDistanceIndex = -1;
		for (int i = 0; i < roots.length; i++) {
			double xDistance = z.getReal() - roots[i].getReal();
			double yDistance = z.getImaginary() - roots[i].getImaginary();
			double distance =
					Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
			if (i == 0) {
				minDistance = distance;
			}
			if (distance <= treshold && (distance  < minDistance || i == 0)) {
				minDistance = distance;
				minDistanceIndex = i;
			}
		}
		return minDistanceIndex;
	}



}
