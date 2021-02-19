package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeration which represents types of a token that the lexer can create.
 * Whitespaces in the input are ignored and are not tokenized.
 *
 * @author jankovidakovic
 *
 */
public enum TokenType {
	/**
	 * Represents the end of the input string. After token of this type
	 * had been generated, the lexer has parsed the whole input string and
	 * no more tokens will be created.
	 */
	EOF,
	
	/**
	 * Represents the continuous sequence of letters. Letters are all characters 
	 * for which <code>Character.isLetter</code> returns <code>true</code>.
	 */
	WORD,
	
	/**
	 * Represents the number which can be represented by the type <code>Long</code>
	 * Any number which cannot be represented that way is not allowed.
	 */
	NUMBER,
	
	/**
	 * Represents a single character which cannot be described by any of the 
	 * above types. Whitespaces in the input string are ignored and are not
	 * tokenized, not even into this token type.
	 */
	SYMBOL
}
