package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents the different states of the lexer. States should be controlled
 * by the client which uses the lexer, as the lexer creates different types
 * of tokens in different states.
 *
 * @author jankovidakovic
 *
 */
public enum LexerState {
	/**
	 * Represents the default state of the lexer. In this state, lexer creates
	 * all the defined tokens in <code>TokenType</code> enumeration.
	 */
	BASIC,
	
	/**
	 * Represents the state of reading plain text. In this state, lexer treats
	 * all continuous non-whitespace characters as words, and tokenizes them
	 * into <code>WORD</code> tokens. Also, no escape sequences are supported,
	 * so any occurrence of '\' is treated as an occurrence of any other normal
	 * character. Lexer should be put in this state after it generates the 
	 * <code>SYMBOL</code> token with the value of '#', because that symbol
	 * represents the beginning of an area which is intended to be treated
	 * as plain text. When the lexer encounters the '#' while in this state,
	 * it will tokenize it into <code>SYMBOL</code> token, and at that point
	 * it should be put back into <code>BASIC</code> state.
	 */
	EXTENDED;
}
