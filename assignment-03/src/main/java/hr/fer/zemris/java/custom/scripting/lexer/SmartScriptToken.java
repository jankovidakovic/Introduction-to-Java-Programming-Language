package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents one token of the lexer, which consists of type and value.
 * SmartScriptLexer defines a finite set of token types, which are described 
 * in detail in the documentation of <code>SmartScriptTokenType</code> 
 * enumeration. Values of the tokens will be as follows:
 * 
 * 	EOF 		- null
 * 	TEXT		- <code>String</code> which represents plain text.
 * 	TAG_BEGIN 	- "{$"
 * 	TAG_NAME	- <code>String</code> which contains upper-case version of 
 * 					tag name (because they are defined to be case-insensitive).
 * 	TAG_END		- "$}"
 * 	VARIABLE	- <code>String</code> which contains the name of the variable.
 * 	FUNCTION	- <code>String</code> which contains the name of the function,
 * 					without the leading '@' which is provided in input text.
 * 	STRING		- <code>String</code> which contains the content of the string,
 * 					without the enclosing double quotes.
 * 	OPERATOR	- <code>Character</code> which contains the operator.
 * 	INTEGER		- <code>Integer</code> with value of given integer.
 * 	DOUBLE		- <code>Double</code> with value of given double.
 * 
 * SmartScriptLexer can only generate the tokens provided above, and they are
 * guaranteed to have the values as specified, which means they can be
 * cast-down into their corresponding types if need be. Although all tokens
 * are able to be generated, SmartScriptLexer has different states which can generate
 * different subset of those tokens, so the client should be aware of that.
 * @author jankovidakovic
 *
 */
public class SmartScriptToken {
	
	//private variables
	private final SmartScriptTokenType type; //type of the token
	private final Object value;	//value that the token stores
	
	/**
	 * Constructs a token with given token type, which stores given value
	 * @param type token type
	 * @param value value of the token
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Retrieves the value of the token.
	 * @return value of the token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Retrieves the type of the token.
	 * @return token type
	 */
	public SmartScriptTokenType getType() {
		return type;
	}
}

