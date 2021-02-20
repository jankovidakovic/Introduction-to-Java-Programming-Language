package hr.fer.zemris.java.hw05.db;

/**
 * Model of a comparison operator that compares the two strings using some
 * condition.
 *
 * @author jankovidakovic
 *
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Compares the given strings using some condition that should be defined
	 * in the body of the method. If the condition is satisfied, returns
	 * <code>true</code>, and if it is not, returns <code>false</code>
	 *
	 * @param value1 First string to be checked
	 * @param value2 Second string to be checked
	 * @return <code>true</code> if input strings satisfy the condition,
	 * 			<code>false</code> if they don't.
	 */
	boolean satisfied(String value1, String value2);
}
