package hr.fer.zemris.java.hw05.db;

/**
 * Lexer which tokenizes the given query into tokens.
 *
 * @author jankovidakovic
 *
 */
public class QueryLexer {

	private final char[] data; //characters that make the query
	private QueryToken token; //next generated token
	private int index; //position of the lexer in the query string
	
	/**
	 * Initializes the lexer to tokenize given string.
	 *
	 * @param data String to be tokenized
	 */
	public QueryLexer(String data) {
		this.data = data.toCharArray();
		index = 0;
	}
	
	/**
	 * Skips the concurrent whitespace starting from the current index.
	 * Moves index to the first non-whitespace character.
	 */
	private void skipWhitespace() {
		while (index < data.length && Character.isWhitespace(data[index])) {
			index++;
		}
	}
	
	/**
	 * Obtains the next token from the string that was passed in the 
	 * constructor.
	 *
	 * @return next token from the string that the lexer is tokenizing.
	 * @throws QueryLexerException if the EOF token was already obtained
	 * or the next sequence of characters that is candidate for the new
	 * token does not fit into any token type.
	 */
	public QueryToken nextToken() {
		
		//check if there are more tokens to obtain
		if (token != null && token.getType() == QueryTokenType.EOF) {
			throw new QueryLexerException("No more tokens.");
		}
		skipWhitespace(); //whitespace is not tokenized
		if (index >= data.length) { //eof token should be generated
			token = new QueryToken(QueryTokenType.EOF, null);
		} else {
			switch (data[index]) {
			case '<': //could be '<' or "<="
				if (index + 1 < data.length && data[index + 1] == '=') { // <=
					token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR,
							"<=");
					index += 2;
				} else { // <
					token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR,
							"<");
					index++;
				}
				break;
			case '=':
				token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "=");
				index++;
				break;
			case '!': //could be "!="
				if (index + 1 < data.length && data[index+1] == '=') {
					token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR,
							"!=");
					index += 2;
				} else { //not an operator
					token = new QueryToken(QueryTokenType.OTHER, "!");
					index++;
				}
				break;
			case '>': //could be '>' or ">="
				if (index + 1 < data.length && data[index + 1] == '=') { // >=
					token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR,
							">=");
					index += 2;
				} else { // >
					token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR,
							">");
					index++;
				}
				break;
			case '\"': //could be a string literal
				StringBuilder stringLiteral = new StringBuilder();
				index++;
				//tokenize until the next double quote
				while (index < data.length) {
					if (data[index] == '\"') { //end of string literal
						token = new QueryToken(QueryTokenType.STRING_LITERAL,
								stringLiteral.toString());
						index++;
						return token; //breaks the loop
					} else {
						stringLiteral.append(data[index++]);
					}
				}
				//if thread of execution gets to this part of the code,
				//it means that there was no closing double quote in the input
				throw new QueryLexerException("String literal was not closed.");
				
			default: //what remains must be a letter
				if (Character.isLetter(data[index])) {
					StringBuilder tokenValue = new StringBuilder(Character.toString(data[index]));
					index++;
					while (index < data.length && Character.isLetter(data[index])) {
						tokenValue.append(data[index]);
						index++;
					}
					if (tokenValue.toString().equalsIgnoreCase("AND")) { //logical operator
						token = new QueryToken(QueryTokenType.LOGICAL_OPERATOR, 
								"AND");
					} else if (tokenValue.toString().equals("jmbag")
							|| tokenValue.toString().equals("firstName")
							|| tokenValue.toString().equals("lastName")
							|| tokenValue.toString().equals("finalGrade")) {
						//field name
						token = new QueryToken(QueryTokenType.FIELD_NAME,
								tokenValue.toString());
					} else if (tokenValue.toString().equals("LIKE")) {
						token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR,
								tokenValue.toString());
					} else { //something unspecified
						token = new QueryToken(QueryTokenType.OTHER, tokenValue.toString());
					}
				} else { //something unspecified
					token = new QueryToken(QueryTokenType.OTHER, 
							Character.toString(data[index]));
					index++;
				}
			}
		}
		return token;
	}
}
