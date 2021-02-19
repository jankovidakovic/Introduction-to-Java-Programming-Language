package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents the token which the lexer creates. One token consists of token type,
 * and token value, both of which are read-only properties. Token types are 
 * defined in <code>TokenType</code> enumeration, and token values are:
 * 	<code>EOF</code>		- <code>null</code>
 * <code>NUMBER</code>		- <code>Long</code> representation of a number
 * <code>WORD</code>		- <code>String</code> representation of a word
 * <code>SYMBOL</code>		- <code>Character</code> representation of the symbol
 * 
 * Even though token value is defined to be of type <code>Object</code>, 
 * values defined above are guaranteed, so token values can be cast down as
 * needed.
 *
 * @author jankovidakovic
 *
 */
public class Token {
	
	//private variables
	private final TokenType type; //type of the token
	private final Object value;	//value that the token stores
	
	/**
	 * Constructs a token with given token type, which stores given value
	 *
	 * @param type token type
	 * @param value value of the token
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Retrieves the value of the token.
	 *
	 * @return value of the token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Retrieves the type of the token.
	 *
	 * @return token type
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * String representation of the token, which consists of type and value
	 * separated by a comma, inside parentheses.
	 */
	@Override
	public String toString() {
		return "(" + type.toString() + ", " + value.toString() + ")";
	}
}
