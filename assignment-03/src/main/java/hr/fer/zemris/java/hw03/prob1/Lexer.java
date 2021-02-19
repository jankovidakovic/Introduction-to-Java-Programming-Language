package hr.fer.zemris.java.hw03.prob1;

/**
 * Model of a simple lexer which tokenizes the input string.
 * Input string consists of series of words, numbers and symbols.
 * Whitespaces in input string serve only as separators, and are
 * not tokenized but are taken into account when processing the input.
 * Word is defined as a non-empty continuous sequence of letters, and letters
 * are all characters for which the method <code>Character.isLetter</code>
 * returns <code>true</code>. Escaping mechanism is allowed on digits and 
 * '\' character, which will then be treated as parts of the word. No other escape
 * sequence is allowed. Tokens of words are of <code>WORD</code> type.
 * Number is defined as a non-empty continuous sequence of digits, which is
 * representable by the <code>Long</code> type. If the number is not representable
 * in that way (e.g. it is too big), lexer will throw an exception.
 * Symbol is defined as a single character which does not fit the two
 * categories defined above. Whitespaces are ignored in tokenization, so they
 * do not fit in this category. Also, if lexer encounters a sequence of digits
 * that is not representable by the <code>Long</code> type, the sequence will
 * NOT be tokenized into multiple symbols, but exception will be thrown, because
 * such input is not valid.
 * At the end of input string, lexer generates a special <code>EOF</code> token,
 * which signals to the client that all input has been tokenized. Attempting
 * to use the lexer after this token has been generated will result in an 
 * exception.
 * Lexer has 2 states, and the responsibility of putting a lexer in the 
 * correct state falls to the client who uses the lexer.
 * <code>BASIC</code> state is used for tokenizing all input, until the first
 * occurrence of '#'. After that, the lexer should be put in <code>EXTENDED</code>
 * state, and in that state lexer tokenizes all input into words. When it
 * encounters the '#' character again, it will tokenize it as a symbol, after
 * which it should be put in <code>BASIC</code> state again. This pattern should
 * repeat for every pair of '#' characters.
 *
 * @author jankovidakovic
 *
 */
public class Lexer {
	
	//private variables
	private final char[] data;	//input text
	private Token token;	//current token
	private int currentIndex; 	//index of first unprocessed character
	private LexerState state;	//state of the lexer
	
	/**
	 * Constructs lexer which will be used to tokenize given string
	 *
	 * @param text text which is intended to be tokenized
	 * @throws NullPointerException if given text is null.
	 */
	public Lexer(String text) {
		data = text.toCharArray(); //throws NullPointerException if text is null
		currentIndex = 0;
		token = null;
		state = LexerState.BASIC;
	}
	
	/**
	 * Skips whitespace in input string.
	 */
	private void skipWhitespace() {
		while (currentIndex < data.length && 
				Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}
	
	/**
	 * Creates the next token from the string and returns it.
	 *
	 * @return next created token. If whole string has been tokenized, returns
	 * special token (EOF, null). After that, every next call of this method
	 * will throw an exception, because the end has been reached and nothing
	 * more can be tokenized.
	 * @throws LexerException if the method is called after token (EOF, null)
	 * has already been produced.
	 */
	public Token nextToken() {
		//check if all input has been tokenized
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("No more tokens.");
		}
		
		skipWhitespace();	//ignoring whitespace
		
		
		if (currentIndex >= data.length) {//last token should be created 
			token = new Token(TokenType.EOF, null);
			return token;
		}
		//process each state separately
		StringBuilder currentToken = new StringBuilder(); //for storing extracted characters
		if (state == LexerState.BASIC) {

			boolean escaped = false; //flag which indicates escape attempt
			
			//try to read valid word
			while (currentIndex < data.length) {
				if (escaped) { //treat character as a letter
					if (Character.isDigit(data[currentIndex]) || //digit is valid
							data[currentIndex] == '\\') { //backslash is valid
						currentToken.append(data[currentIndex]);
							escaped = false;	//escape is over
					} else {	//invalid escape
						throw new LexerException("Invalid escape sequence.");
					}
				} else { //there was no attempted escaping
					if (data[currentIndex] == '\\') { //escape initiated
						escaped = true;	//remember for next character
					} else if (Character.isLetter(data[currentIndex])) { //only letters
						currentToken.append(data[currentIndex]); //valid part of word
					} else { //end of the attempted word
						break;
					}
				}
				currentIndex++;
			}
			
			if (escaped && (currentToken.length() == 0)) { //only a backslash was read
				throw new LexerException("Invalid escape sequence.");
			} else if (currentToken.length() > 0) { //non-empty valid word was read
				token = new Token(TokenType.WORD, currentToken.toString());
			} else { //couldn't read non-empty word - try number next
				
				while (currentIndex < data.length &&
						Character.isDigit(data[currentIndex])) {//series of digits
					currentToken.append(data[currentIndex]);
					currentIndex++;
				}
				if (currentToken.length() > 0) { //number was read
					try {
							token = new Token(TokenType.NUMBER, 
								Long.parseLong(currentToken.toString()));
					} catch (NumberFormatException ex) {//does not fit into long
						throw new LexerException("Invalid number in input.");
					}
				} else { //neither word nor number was read, symbol remains
					token = new Token(TokenType.SYMBOL, data[currentIndex]);
					currentIndex++;
				}
			}
		} else { //treat everything as words
			while (currentIndex < data.length && data[currentIndex] != '#'
					&& !Character.isWhitespace(data[currentIndex])) {
				currentToken.append(data[currentIndex]); //doesnt add '#'
				currentIndex++;
			}
			if (currentToken.length() > 0) { //tokenized a word
				token = new Token(TokenType.WORD, currentToken.toString());
			} else { //next character must be '#' since whitespaces were skipped
				token = new Token(TokenType.SYMBOL, data[currentIndex]);
				currentIndex++;
				//after this token, lexer should be put back into basic state
			}

		}
		
		return token;
	}
	
	/**
	 * Retrieves the last generated token.
	 *
	 * @return the last generated token.
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Switches the lexer into the given state. Intended to be controlled
	 * by the client.
	 *
	 * @param state new state of lexer. Must not be <code>null</code>.
	 * @throws NullPointerException if given state is <code>null</code>.
	 */
	public void setState(LexerState state) {
		if (state == null) { //not allowed
			throw new NullPointerException("Invalid state.");
		}
		this.state = state;
	}
	
}
