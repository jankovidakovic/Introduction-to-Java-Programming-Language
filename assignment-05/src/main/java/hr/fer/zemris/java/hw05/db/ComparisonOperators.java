package hr.fer.zemris.java.hw05.db;

/**
 * Provides implementation of comparison operators that are supported by the
 * database. Those include: < (less than), <= (less than or equals), =(equals),
 * !=(not equals), >=(greater than or equal), >(greater), and "LIKE", which 
 * performs the same operation as an equals operator, but the second argument 
 * can contain a single wildcard character '*', which denotes any possible
 * sequence of characters, including an empty one. Multiple wildcards are not 
 * supported, and wildcard can be placed at any position within the operand. 
 * Left side of the comparison operator must be a valid field name, which 
 * include "jmbag", "firstName", "lastName" and "finalGrade". Right side of 
 * the comparison operator must be a string literal. No escaping is supported 
 * in string literals, and they must be enclosed in double quotes. Character '*'
 * is treated as a wildcard only when "LIKE" operator is used, and in other
 * cases it is treated as a normal character.
 *
 * @author jankovidakovic
 *
 */
public class ComparisonOperators {
	
	//operators
	public static final IComparisonOperator LESS;
	public static final IComparisonOperator LESS_OR_EQUALS;
	public static final IComparisonOperator GREATER;
	public static final IComparisonOperator GREATER_OR_EQUALS;
	public static final IComparisonOperator EQUALS;
	public static final IComparisonOperator NOT_EQUALS;
	public static final IComparisonOperator LIKE;
	
	//initialization of the operators
	static {
		LESS = (value1, value2) -> value1.compareTo(value2) < 0;
		
		LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
		
		GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
		
		GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
		
		EQUALS = String::equals;
		
		NOT_EQUALS = (value1, value2) -> !(value1.equals(value2));
		
		LIKE = new IComparisonOperator() {
			
			@Override
			public boolean satisfied(String value1, String value2) {
				if (value2.contains("*")) { //contains a wildcard
					int wildcardIndex = value2.indexOf('*');
					
					//extract substring of value2 before the wildcard and 
					//corresponding substring in value1
					String beforeWildcard1 = value1.substring(0, wildcardIndex);
					String beforeWildcard2 = value2.substring(0, wildcardIndex);
					
					//extract substring of value2 after the wildcard, and
					//corresponding substring in value1
					String afterWildcard2 = value2.substring(wildcardIndex + 1);
					String afterWildcard1 = value1.substring(value1.length() - afterWildcard2.length());
					
					//value1 matches value2 if the substrings match and
					//corresponding substrings in value1 do not overlap
					return beforeWildcard1.equals(beforeWildcard2) &&
							afterWildcard1.equals(afterWildcard2) &&
							beforeWildcard1.length() + afterWildcard1.length()
							<= value1.length();
				} else { //no wildcard
					return value1.equals(value2);
				}
			}
		};
	}
}
