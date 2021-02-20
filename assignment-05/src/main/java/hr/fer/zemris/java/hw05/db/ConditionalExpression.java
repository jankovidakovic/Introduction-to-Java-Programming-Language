package hr.fer.zemris.java.hw05.db;

/**
 * Model of a conditional expression which can be used on the database.
 * An expression consists of a student record's field, string literal to
 * compare it to, and the comparison operator.
 *
 * @author jankovidakovic
 *
 */
public class ConditionalExpression {

	private final IFieldValueGetter fieldValueGetter; //retrieves the field's value
	private final String stringLiteral; //string literal which the field is compared to
	private final IComparisonOperator comparisonOperator; //comparison operator
	
	/**
	 * Initializes the expression and sets all the parameters to given values.
	 *
	 * @param fieldValueGetter Getter for value of the needed field
	 * @param stringLiteral String literal which the field value is compared to
	 * @param comparisonOperator operator by which the comparison is done
	 */
	public ConditionalExpression(IFieldValueGetter fieldValueGetter,
			String stringLiteral, IComparisonOperator comparisonOperator) {
		this.fieldValueGetter = fieldValueGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * @return the fieldValueGetter
	 */
	public IFieldValueGetter getFieldValueGetter() {
		return fieldValueGetter;
	}

	/**
	 * @return the stringLiteral
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * @return the comparisonOperator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
	
	/**
	 * Evaluates the expression upon the given record. Returns the result
	 * of the evaluation.
	 *
	 * @param record Record to be checked using the expression
	 * @return <code>true</code> if expression is true for the given record,
	 * <code>false</code> otherwise.
	 */
	public boolean evaluate(StudentRecord record) {
		return comparisonOperator.satisfied(
				getFieldValueGetter().get(record), getStringLiteral());
	}
	
}
