package hr.fer.zemris.math;

/**
 * Model of a complex polynomial in the standard polynomial form. Coefficients
 * of the polynomial are passed through the constructor. Coefficients are
 * expected to be passed starting with the one of the zeroeth degree, ending
 * with the one of the highest degree.
 * 
 * @author jankovidakovic
 *
 */
public class ComplexPolynomial {

	private Complex[] factors;

	public ComplexPolynomial(Complex... factors) {
		this.factors = factors;
	}

	// returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	public short order() {
		return (short) (factors.length - (short) 1);
	}

	// computes a new polynomial this*p
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] resultFactors = new Complex[order() * p.order()];
		for (int i = 0; i < order(); i++) {
			for (int j = 0; j < p.order(); j++) {
				resultFactors[i + j] = getFactor(i).multiply(p.getFactor(j));
			}
		}
		return new ComplexPolynomial(resultFactors);
	}

	/**
	 * Returns the polynomial factor of the given degree. If degree is greater
	 * than the number of defined factors, returns zero, as is expected.
	 * 
	 * @param  degree Degree of wanted factor
	 * @return        value of the factor of given degree (zero for degrees
	 *                greater than defined in the constructor)
	 */
	public Complex getFactor(int degree) {
		if (degree > order()) {
			return Complex.ZERO;
		}
		return factors[degree];
	}

	/**
	 * Computes the first derivative of this polynomial.
	 * 
	 * @return first derivetive of this polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] firstDerivativeFactors = new Complex[order()];
		for (int i = 1; i < factors.length; i++) {
			firstDerivativeFactors[i
					- 1] =
							factors[i].multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(firstDerivativeFactors);
	}

	/**
	 * Computes polynomial value at given point z
	 * 
	 * @param  z argument for which the value is wanted
	 * @return   value of the polynomial applied over given argument
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		for (int i = 0; i < factors.length; i++) {
			result = result
					.add(factors[i].multiply(z.power(i)));
		}
		return result;
	}

	/**
	 * Returns the string representation of the polynomial, in the form
	 * (zn)*z^n+(zn-1)*z^n-1+...+(z0)*z^0
	 * 
	 * @return string representation of the polynomial
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = factors.length - 1; i >= 0; i--) {
			sb.append("(" + factors[i] + ")");
			if (i == 0) {
				continue;
			}
			sb.append("*z^" + i);
			if (i != 0) {
				sb.append("+");
			}
		}
		return sb.toString();
	}

}
