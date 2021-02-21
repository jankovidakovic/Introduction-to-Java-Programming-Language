package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents the valid token types that the <code>SmartScriptLexer</code>
 * can process. Any sequence of characters that is invalid in the context
 * of the tokens provided below will cause the lexer to throw an exception.
 * @author jankovidakovic
 *
 */
public enum SmartScriptTokenType {
	/**
	 * Represents the end of the input text, there are no more tokens.
	 * This is always the last token generated by the SmartScriptLexer, and
	 * attempts to generate more tokens after this token has been generated
	 * will result in an error and SmartScriptLexer will throw an appropriate
	 * exception.
	 */
	EOF,
	
	/**
	 * Represents plain text in the input document. Plain text is all that
	 * is not inside tag markers, which are "{$" for the beginning of the tag
	 * and "$}" for the end of the tag. Following escape sequences are valid:
	 * 		"\\", which is treated as a single '\', 
	 * 		"\{", which is treated as a single '{' and is not part of the
	 * 			beginning of a tag.
	 * Character '{' is just a regular character unless it is followed by '$',
	 * which then marks the beginning of the tag.
	 */
	TEXT,
	
	
	/**
	 * Represents the sequence "{$", which marks the beginning of a tag.
	 */
	TAG_BEGIN,
	
	/**
	 * Represents a tag name. Valid tag name is '=' (equals sign), or
	 * a valid variable name. Tag names are case-insensitive in the context
	 * of this lexer.
	 */
	TAG_NAME,
	
	/**
	 * Represents the sequence "$}", which marks the end of a tag in input text.
	 */
	TAG_END,
	/**
	 * Represents variable name. Sequence of characters is a valid variable
	 * name if and only if it starts by letter and after follows zero or 
	 * more letters, digits or underscores. 
	 */
	VARIABLE,
	
	/**
	 * Represents sequence of characters inside the tag, enclosed in 
	 * double qoutes. Strings accept following escape sequences:
	 * 		'\\' - represents a single character '\',
	 * and 	'\"' - represents a single character '"' (not the end of the string).
	 * Also, characters '\n', '\r' and '\t' are allowed and have its usual
	 * meaning. Any other escape sequence is invalid and will result in an error.
	 */
	STRING,
	
	/**
	 * Represents a function name. Valid function name starts with '@', after
	 * which follows a valid variable name.
	 */
	FUNCTION,
	
	/**
	 * Represents an operator. Valid operators are: '+'(plus), '-'(minus), 
	 * '*'(multiplication), '/'(division) and '^'(power). 
	 */
	OPERATOR,
	
	/**
	 * Represents a signed integer. Leading plus sign is not allowed.
	 */
	INTEGER,
	
	/**
	 * Represents a signed double. Valid form is a digits-dot-digits format,
	 * and scientific notation is not allowed.
	 */
	DOUBLE;
}