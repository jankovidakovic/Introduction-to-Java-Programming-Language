package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Implementation of an object that serves as a weak wrapper of some values.
 * Wrapper provides arithmetic operations, however the value type is checked at
 * runtime upon performing such operations, and the only supported values in
 * that case are <code>null</code>, and instances of <code>Integer</code>,
 * <code>String</code> and <code>Double</code>.
 * 
 * 
 * @author jankovidakovic
 *
 */
public class ValueWrapper {

	// TODO - think of something to fix this whole thing
	// TODO - implement tests

	private Object value; // value stored in the wrapper

	/**
	 * Constructs a wrapper for the given object
	 * 
	 * @param value object to be wrapped
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Adds the given value to the one that is currently stored. Before
	 * addition, data type checking is performed. At the moment the method is
	 * called, only allowed types that the wrapper can store are null, Integer,
	 * String or Double. For any other case, the method throws an exception. If
	 * any operand is null, it is treated as an integer with value zero. If an
	 * operand is a String and it is parsable into an integral or decimal value,
	 * then it is treated as such, otherwise the exception is thrown. If either
	 * operand is a <code>Double</code>, operation is performed on doubles and
	 * the result is stored as an instance of <code>Double</code>. In the last
	 * case, if both operands are integers, the operation is performed in the
	 * integer domain and the result is stored as an integer.
	 * 
	 * @param incValue value to be added to the stored value
	 */
	public void add(Object incValue) {
		Operation.setOperands(value, incValue);
		value = Operation.add();
	}

	public void subtract(Object decValue) {
		Operation.setOperands(value, decValue);
		value = Operation.sub();
	}

	public void multiply(Object mulValue) {
		Operation.setOperands(value, mulValue);
		value = Operation.mul();
	}

	public void divide(Object divValue) {
		Operation.setOperands(value, divValue);
		value = Operation.div();
	}

	public int numCompare(Object withValue) {
		Operation.setOperands(value, withValue);
		Object compRes = Operation.sub();
		if (compRes instanceof Double) {
			return Double.compare((Double) compRes, 0.0);
		} else {
			return Integer.compare((Integer) compRes, 0);
		}
	}

	/**
	 * Checks whether a given object has a correct type to enter an arithmetic
	 * evaluation. Such objects must be <code>null</code>, or instances of
	 * <code>Integer</code>, <code>Double</code>, or <code>String</code>.
	 * 
	 * @param  value object whose type is to be checked
	 * @return       <code>true</code> if object is eligible for arithmetic
	 *               operations, <code>false</code> otherwise.
	 */
	public boolean checkType(Object value) {
		return value == null || value instanceof Integer
				|| value instanceof Double || value instanceof String;
	}

	/**
	 * Returns the value that the value wrapper stores. No type conversion is
	 * performed.
	 * 
	 * @return value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Model of a binary operation between two weakly-typed value wrappers.
	 * 
	 * @author jankovidakovic
	 *
	 */
	private static class Operation {

		// operands
		private static Object firstOp;
		private static Object secondOp;

		/**
		 * Sets the operands to given values. Performs type checking and type
		 * conversion.
		 * 
		 * @param firstOp  first operand
		 * @param secondOp second operand
		 */
		private static void setOperands(Object firstOp, Object secondOp) {
			Operation.firstOp = getImplicitCast(firstOp);
			Operation.secondOp = getImplicitCast(secondOp);

		}

		/**
		 * Adds the operands and returns the result as an object of correct
		 * type.
		 * 
		 * @return result of addition
		 */
		private static Object add() {
			if (firstOp instanceof Double) {
				if (secondOp instanceof Double) {
					return (Double) firstOp + (Double) secondOp;
				} else {
					return (Double) firstOp + (Integer) secondOp;
				}
			} else {
				if (secondOp instanceof Double) {
					return (Integer) firstOp + (Double) secondOp;
				} else {
					return (Integer) firstOp + (Integer) secondOp;
				}
			}
		}

		/**
		 * Subtracts the operands and returns the result
		 * 
		 * @return result of subtraction
		 */
		private static Object sub() {
			if (firstOp instanceof Double) {
				if (secondOp instanceof Double) {
					return (Double) firstOp - (Double) secondOp;
				} else {
					return (Double) firstOp - (Integer) secondOp;
				}
			} else {
				if (secondOp instanceof Double) {
					return (Integer) firstOp - (Double) secondOp;
				} else {
					return (Integer) firstOp - (Integer) secondOp;
				}
			}
		}

		/**
		 * Multiplies the operands and returns the result
		 * 
		 * @return result of multiplication
		 */
		private static Object mul() {
			if (firstOp instanceof Double) {
				if (secondOp instanceof Double) {
					return (Double) firstOp * (Double) secondOp;
				} else {
					return (Double) firstOp * (Integer) secondOp;
				}
			} else {
				if (secondOp instanceof Double) {
					return (Integer) firstOp * (Double) secondOp;
				} else {
					return (Integer) firstOp * (Integer) secondOp;
				}
			}
		}

		/**
		 * Divides the operands and returns the result.
		 * 
		 * @return result of division
		 */
		private static Object div() {
			if (firstOp instanceof Double) {
				if (secondOp instanceof Double) {
					return (Double) firstOp / (Double) secondOp;
				} else {
					return (Double) firstOp / (Integer) secondOp;
				}
			} else {
				if (secondOp instanceof Double) {
					return (Integer) firstOp / (Double) secondOp;
				} else {
					return (Integer) firstOp / (Integer) secondOp;
				}
			}
		}

		/**
		 * Performs the type conversion of the given value, so that it is
		 * eligible for arithmetic calculation.
		 * 
		 * @param  value            value to be cast
		 * @return                  cast of the value that is eligible for
		 *                          arithmetics
		 * @throws RuntimeException if the given value cannot be cast to a type
		 *                          that is eligible for arithmetic operations.
		 */
		private static Object getImplicitCast(Object value) {
			if (value == null) { // treat as zero
				return Integer.valueOf(0);
			} else if (value instanceof String) {
				try { // treat as integer
					return Integer.parseInt((String) value);
				} catch (NumberFormatException e1) {
					try { // treat as double
						return Double.parseDouble((String) value);
					} catch (NumberFormatException e2) { // cannot
						throw new RuntimeException("Undefined operation.");
					}
				}
			} else if (value instanceof Double || value instanceof Integer) {
				return value;
			} else {
				throw new RuntimeException("Invalid type.");
			}
		}
	}

}
