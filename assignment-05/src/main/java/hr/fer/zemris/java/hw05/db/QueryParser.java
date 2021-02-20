package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser which accepts a single query and parses its content, which can
 * then be retrieved by the methods defined in the parser. If query does
 * not fit the database's specification, the exception is thrown which
 * should be caught in the client that uses the parser.
 *
 * @author jankovidakovic
 *
 */
public class QueryParser {

	private final QueryLexer lexer; //lexer used to tokenize query
	List<ConditionalExpression> query; //final representation of parsed query
	
	/**
	 * Constructs a parser and parses the given query. Parsing is not
	 * done in the constructor but is delegated to another method, which
	 * provides better modularity.
	 *
	 * @param query Query to be parsed.
	 * @throws QueryParserException if given query is invalid.
	 */
	public QueryParser(String query) {
		//query to be parsed
		lexer = new QueryLexer(query); //initialize a lexer
		this.query = new ArrayList<>();
		
		try {
			parseQuery(); //will throw QueryParserException if parser failed
		} catch (QueryLexerException ex) { //lexer was unable to tokenize
			throw new QueryParserException("Invalid query.");
		}
	}
	
	/**
	 * Parses a query passed through the constructor using the query lexer.
	 *
	 * @throws QueryParserException if the parser failed to parse the query
	 */
	private void parseQuery() {
		QueryToken token = getNextToken();
		
		
		//elements of the conditional expression
		IFieldValueGetter field = null;
		IComparisonOperator operator = null;
		String stringLiteral = null;
		
		//parsing
		while (token.getType() != QueryTokenType.EOF) {
			switch (token.getType()) {
			case FIELD_NAME: //should be the first element of the expression
				if (field == null && operator == null && stringLiteral == null) {
					field = parseFieldValue(token.getValue());
					break;
				} else {
					throw new QueryParserException("Invalid syntax. First "
							+ "element of the query should be the field name.");
				}
			case COMPARISON_OPERATOR: //should follow the field name
				if (field != null && operator == null && stringLiteral == null) {
					operator = parseComparisonOperator(token.getValue());
					break;
				} else {
					throw new QueryParserException("Invalid syntax. "
							+ "Comparison operator must follow after "
							+ "field name.");
				}
			case STRING_LITERAL: //should follow the comparison operator
				if (field != null && operator != null && stringLiteral == null) {
					stringLiteral = token.getValue();
					if (operator == ComparisonOperators.LIKE) {
						long wildcardCount = stringLiteral.chars()
											.filter(c -> c == '*')
											.count();
						if (wildcardCount > 1) {
							throw new QueryParserException("Invalid syntax. "
									+ "When using LIKE, string literal "
									+ "can contain at most one wildcard "
									+ "character.");
						}
					}
					break;
				} else {
					throw new QueryParserException("Invalid syntax. "
							+ "String literal must follow after the expression "
							+ "operator.");
				}
			case LOGICAL_OPERATOR: //should follow the string literal
				if (field != null && operator != null && stringLiteral != null) {
					query.add(new ConditionalExpression(field, stringLiteral, 
							operator));
					field = null;
					operator = null;
					stringLiteral = null;
					break;
				} else { //invalid query syntax
					throw new QueryParserException("Invalid syntax. Logical "
							+ "operator can only be placed after the "
							+ "string literal.");
				}
			case OTHER:
				throw new QueryParserException("Invalid syntax. Unknown "
						+ "query element. Available query elements are: "
						+ "field name, comparison operator, string literal"
						+ "and logical operator.");
			case EOF:
				//impossible since it is the condition at which the loop breaks
			}
			token = getNextToken(); //will throw 
		}
		if (field != null && operator != null && stringLiteral != null ) {
			query.add(new ConditionalExpression(field, stringLiteral, 
					operator));
		} else {
			throw new QueryParserException("Invalid query. Not enough arguments.");
		}
		
	}
	
	/**
	 * Parses the token into the field value.
	 * @param token Token to be parsed
	 * @return instance of <code>IFieldValueGetter</code> that corresponds
	 * 			to the given token.
	 * @throws QueryParserException if token is not a field name token, 
	 * 			or parsed field name is not supported by the database
	 * 			query.
	 */
	private IFieldValueGetter parseFieldValue(String token) {
		return switch (token) {
			case "firstName" -> FieldValueGetters.FIRST_NAME;
			case "lastName" -> FieldValueGetters.LAST_NAME;
			case "jmbag" -> FieldValueGetters.JMBAG;
			default -> throw new QueryParserException("Unsupported field name.");
		};
	}
	
	private IComparisonOperator parseComparisonOperator(String operator) {

		return switch (operator) {
			case "<" -> ComparisonOperators.LESS;
			case "<=" -> ComparisonOperators.LESS_OR_EQUALS;
			case "=" -> ComparisonOperators.EQUALS;
			case "!=" -> ComparisonOperators.NOT_EQUALS;
			case ">" -> ComparisonOperators.GREATER;
			case ">=" -> ComparisonOperators.GREATER_OR_EQUALS;
			case "LIKE" -> ComparisonOperators.LIKE;
			default -> throw new QueryParserException("Invalid comparison operator.");
		};
	}
	
	/**
	 * Obtains the next token created by the lexer. Catches any exceptions
	 * by the lexer and rethrows them as <code>QueryParserException</code>.
	 *
	 * @return next token
	 */
	private QueryToken getNextToken() {
		try {
			return lexer.nextToken();
		} catch (QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
	}
	
	/**
	 * Checks if the parsed query is a direct query. Direct queries are ones
	 * that consist of a single conditional expression, which itself consists
	 * of a field value "jmbag", comparison operator "=" and some string literal.
	 *
	 * @return <code>true</code> if parsed query is a direct query,
	 * 			<code>false</code> otherwise.
	 */
	public boolean isDirectQuery() {
		if (query.size() == 1) { // only one expression
			ConditionalExpression exp = query.get(0); 
			
			return exp.getFieldValueGetter() == FieldValueGetters.JMBAG
					&& exp.getComparisonOperator() == ComparisonOperators.EQUALS;
		} else { //multiple conditional expressions, can not be a direct query
			return false;
		}
	}
	
	/**
	 * Obtains the value of the string literal of the direct query.
	 *
	 * @return Value of the string literal of the direct query.
	 * @throws IllegalStateException if the parsed query was not a direct query.
	 */
	String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Not a direct query");
		}
		return query.get(0).getStringLiteral();
	}
	
	/**
	 * Returns the parsed query, as a list of conditional expressions.
	 * @return list of conditional expressions that represents the parsed query.
	 */
	List<ConditionalExpression> getQuery() {
		return query;
	}
	
}
