package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration of all possible states that the SmartScriptLexer supports.
 * Switching of states is controlled by the client which uses the lexer.
 * @author jankovidakovic
 *
 */
public enum SmartScriptLexerState {
	/**
	 * In this state, lexer will tokenize all the input into either
	 * plain text (token type <code>TEXT</code>),a tag beginner (token
	 * type <code>TAG_BEGIN</code>) or end of input string (token type 
	 * <code>EOF</code>). After first occurence of tag beginner,
	 * the client should put lexer in <code>TAG_NAME</code> state.
	 */
	TEXT,
	
	/**
	 * In this state, lexer can only tokenize the sequence of characters
	 * which forms a valid tag name. After one token of type <code>TAG_NAME<code>
	 * has been extracted, the client shold put the lexer in the 
	 * <code>TAG_CONTENT</code>state.
	 */
	TAG_NAME,
	
	/**
	 * In this state, lexer is supposed to read the inside of the tag.
	 * It can generate tokens of type <code>VARIABLE</code>, 
	 * <code>STRING</code>, <code>FUNCTION</code>, <code>OPERATOR</code>,
	 * <code>INTEGER</code>, <code>DOUBLE</code> or <code>TAG_END</code>.
	 * After the first token of type <code>TAG_END</code> has been extracted,
	 * the client should put the lexer back into <code>TEXT</code> state.
	 */
	TAG_CONTENT,
	
}
