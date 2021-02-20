package hr.fer.zemris.java.hw05.db;

/**
 * Model of a token which the lexer generates. Token consists of token type and
 * token value. Token types are defined in the <code>QueryTokenType</code>
 * enumeration, and possible values for the defined types are as follows:
 * 	FIELD_NAME - "jmbag", "firstName", "lastName", "finalGrade"
 * 	CONDITIONAL_OPERATOR - "<", "<=", "=", "!=", ">", ">=", "LIKE"
 * 	STRING_LITERAL - any sequence of characters which is bounded by double
 * 					quotes.
 * 	LOGICAL_OPERATOR - "AND"
 * 	OTHER - any sequence of characters which does not fit any above type.
 *
 * @author jankovidakovic
 *
 */
public class QueryToken {

	//private variables
	private final QueryTokenType type; //type of the token
	private final String value;		//value of the token
	
	/**
	 * Constructs a token with given type and value
	 *
	 * @param type type of the token
	 * @param value value of the token
	 */
	public QueryToken(QueryTokenType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public QueryTokenType getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	
}
