package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

/**
 * Represents a parser for a specific structured document format.
 * Parser creates a generative tree of the document, represented by
 * nodes defined in hr.fer.zemris.java.custom.scripting.nodes package.
 * Nodes contain elements defined in hr.fer.zemris.java.custom.scripting.elems
 * Following nodes are supported:
 * 	DocumentNode - represents the root of the tree
 * 	TextNode - holds information about the text in the document
 * 	ForLoopNode - holds information about for-loop in the document,
 * 			including its declaration and all content.
 * 	EchoNode - holds information about the unnamed tag in the document.
 * Document may consist of text and tags. Tags are bounded by {$ and $}, 
 * and text is all outside of tags. Currently, only 3 tag names are 
 * supported: "FOR", which defines a for-loop, "=", which defines a tag
 * without content, and "END", which defines the end of content of the for-loop.
 * Every for-loop content must be close by the corresponding end tag, 
 * 
 * For loop tag should consist of :
 * 	1. tag beginner	-	{$
 * 	2. FOR keyword 	-	"FOR"
 * 	3. valid variable name
 * 	4. valid start expression - can be a variable, function or number
 * 	5. valid stop expression - can be a variable, function or number
 * 	6. valid step expression - can be variable, function or number, and it is 
 * 								not mandatory (can be left out).
 * 	7. tag ender 	-	$}
 * 
 * Tag without content consists of:
 * 	1. tag beginner - {$
 * 	2. sign '=' - parsed as a tag name
 * 	3. variable number of tag parameters, which can be variables, functions,
 * 		numbers, strings or operators.
 * 	4. tag ender - $}
 * 
 * If anything in the document is not in lines with the strict standard which
 * is defined here and in the documentation of the SmartScriptLexer, an
 * instance of the SmartScriptParserException will be thrown. Any error
 * which occurs during parsing will also result in that exception.
 * Parser will throw no other exceptions.
 *
 * @author jankovidakovic
 *
 */
public class SmartScriptParser {

	private final SmartScriptLexer lexer; //lexer used for tokenizing
	private DocumentNode root; //root of the generative tree
	
	/**
	 * Initializes the SmartScriptLexer with given text input, 
	 * and parses the input.
	 *
	 * @param documentBody Document to be parsed.
	 */
	public SmartScriptParser(String documentBody) {
		//private variables
		//input string to be parsed
		lexer = new SmartScriptLexer(documentBody);
		parseDocument();
	}
	
	/**
	 * Parses a document and creates it's generative tree.
	 */
	public void parseDocument() {
		
		root = new DocumentNode(); //root of the tree
		ObjectStack stack = new ObjectStack(); //helper stack
		//the top of the stack is the node which children are
		//currently being parsed. When node should have no more children,
		//it is removed from the stack.
		stack.push(root);
		
		SmartScriptToken token; //stores the last generated token
		
		do {
			
			token = getValidToken(lexer);
			switch (token.getType()) {
			
			case EOF: //end of parsing
				break;
				
			case TEXT: //parse text
				TextNode textNode = new TextNode((String)token.getValue());
				((Node)stack.peek()).addChildNode(textNode);
				break;
				
			case TAG_BEGIN: //parse tag
				
				//ready to read the tag name
				lexer.setState(SmartScriptLexerState.TAG_NAME);
				//get the next token
				token = getValidToken(lexer);
				//check if token type is correct
				if (token.getType() != SmartScriptTokenType.TAG_NAME) {
					throw new SmartScriptParserException("Invalid tag name.");
				}
				
				String tagName = (String)token.getValue();
				//ready to read tag content
				lexer.setState(SmartScriptLexerState.TAG_CONTENT);

				//add as a child of top of the stack
				//push node to the stack since what follows are its children
				//add the created node as child of top of the stack
				//end ot tag was also read, so we need to process it here
				switch (tagName) {
					//named tag - ForLoopNode
					case "FOR" -> {
						ForLoopNode forLoopNode;
						try {
							forLoopNode = setUpForLoopNode(lexer);
						} catch (SmartScriptParserException ex) {
							throw new SmartScriptParserException("Invalid for-loop.");
						}
						((Node) stack.peek()).addChildNode(forLoopNode);
						stack.push(forLoopNode);
						if (forLoopNode.getStepExpression() == null) {
							//end tag token was read, and since the next iteration
							//of loop will overwrite the token, we need to process
							//the end of the tag here
							lexer.setState(SmartScriptLexerState.TEXT);
						}
					}
					//end of for-loop tag
					case "END" -> {
						try {
							stack.pop();
						} catch (EmptyStackException ex) {//invalid document structure
							throw new SmartScriptParserException(
									"Invalid end tag.");
						}
						if (stack.isEmpty()) { //should contain the DocumentNode
							throw new SmartScriptParserException(
									"Invalid end tag.");
						}
					}
					//unnamed tag - echo node
					case "=" -> {
						EchoNode echoNode;
						try { //create EchoNode
							echoNode = setUpEchoNode(lexer, token);
						} catch (SmartScriptParserException ex) {//error
							throw new SmartScriptParserException(
									"Invalid unnamed tag.");
						}
						((Node) stack.peek()).addChildNode(echoNode);
						lexer.setState(SmartScriptLexerState.TEXT);
					}
				}
				
				break;
			case TAG_END: //switch back into text parsing
				lexer.setState(SmartScriptLexerState.TEXT);
				break;
				
			case TAG_NAME: //should never get to any of those	
			case VARIABLE:
			case FUNCTION:
			case STRING:
			case INTEGER:
			case DOUBLE: 
			case OPERATOR:
			default:
				throw new SmartScriptParserException("Invalid document.");
			}
		} while (token.getType() != SmartScriptTokenType.EOF);
		if (!(stack.peek() instanceof DocumentNode)) {
			throw new SmartScriptParserException("Invalid document structure.");
		}
	}
	
	/**
	 * Encapsulates the process of retrieving the valid token.
	 * Throws exception if token was not retrieved successfully.
	 *
	 * @param lexer SmartScriptLexer used for tokenization
	 * @return retrieved token
	 * @throws SmartScriptParserException if an error occurred.
	 */
	private SmartScriptToken getValidToken(SmartScriptLexer lexer) {
		SmartScriptToken token;
		
		try {
			token = lexer.nextToken();	
		} catch (SmartScriptLexerException ex) {
			throw new SmartScriptParserException("Invalid document.");
		}
		
		return token;
	}
	
	/**
	 * Sets up and returns the valid ForLoopNode. If the document is
	 * structured in a way that makes the declaration of the for-loop invalid,
	 * exception is thrown.
	 *
	 * @param lexer SmartScriptLexer used for tokenization
	 * @return instance of ForLoopNode with all the defined properties.
	 * @throws SmartScriptLexerException if for-loop was not declared properly.
	 */
	private ForLoopNode setUpForLoopNode(SmartScriptLexer lexer) {
		
		//variable
		SmartScriptToken token = getValidToken(lexer);
		if (token.getType() != SmartScriptTokenType.VARIABLE) {
			throw new SmartScriptParserException(
					"Invalid variable in for-loop.");
		}
		ElementVariable variable = 
				new ElementVariable((String) token.getValue());
		
		//startExpression
		token = getValidToken(lexer);

		Element startExpression = switch (token.getType()) {
			case VARIABLE -> new ElementVariable((String) token.getValue());
			case STRING -> new ElementString((String) token.getValue());
			case INTEGER -> new ElementConstantInteger((Integer) token.getValue());
			case DOUBLE -> new ElementConstantDouble((Double) token.getValue());
			default -> throw new SmartScriptParserException("Invalid start expression.");
		};
		
		//endExpression
		token = getValidToken(lexer);
		Element endExpression = switch (token.getType()) {
			case VARIABLE -> new ElementVariable((String) token.getValue());
			case STRING -> new ElementString((String) token.getValue());
			case INTEGER -> new ElementConstantInteger((Integer) token.getValue());
			case DOUBLE -> new ElementConstantDouble((Double) token.getValue());
			default -> throw new SmartScriptParserException("Invalid start expression.");
		};
		
		//stepExpression - optional
		token = getValidToken(lexer);
		Element stepExpression = switch (token.getType()) {
			case VARIABLE -> new ElementVariable((String) token.getValue());
			case STRING -> new ElementString((String) token.getValue());
			case INTEGER -> new ElementConstantInteger((Integer) token.getValue());
			case DOUBLE -> new ElementConstantDouble((Double) token.getValue());
			//end of tag is read, step expression is null
			//end of tag will be processed in parent method
			case TAG_END -> null;
			default -> throw new SmartScriptParserException("Invalid start expression.");
		};
		return new ForLoopNode(variable, startExpression, 
				endExpression, stepExpression);
	}
	
	/**
	 * Sets up the EchoNode representation of an empty tag. If structure
	 * of the tag is not valid, exception will be thrown.
	 *
	 * @param lexer SmartScriptLexer used for tokenizing the input
	 * @param token last created token
	 * @return EchoNode representation of the empty tag
	 * @throws SmartScriptParserException if the empty tag's structure is not right,
	 * 		or if any other error occurred.
	 */
	private EchoNode setUpEchoNode(SmartScriptLexer lexer, SmartScriptToken token) {
		//array for storing elements
		int size = 100;
		Element[] elements = new Element[size];
		
		int i = 0;
		while (token.getType() != SmartScriptTokenType.TAG_END) {
			
			if (i >= size) { //reallocate the array dynamically
				Element[] newElements = new Element[size*2];
				System.arraycopy(elements, 0, newElements, 0, size);
				elements = newElements;
			}
			
			token = getValidToken(lexer);
			switch(token.getType()) { //process tokens by type
			case VARIABLE:
				elements[i] = new ElementVariable((String)token.getValue());
				i++;
				break;
			case FUNCTION:
				elements[i] = new ElementFunction((String)token.getValue());
				i++;
				break;
			case STRING:
				elements[i] = new ElementString((String)token.getValue());
				i++;
				break;
			case OPERATOR:
				elements[i] = new ElementOperator((String)token.getValue());
				i++;
				break;
			case INTEGER:
				elements[i] = new ElementConstantInteger((Integer)token.getValue());
				i++;
				break;
			case DOUBLE:
				elements[i] = new ElementConstantDouble((Double)token.getValue());
				i++;
				break;
			case TAG_END:
				break;
			default:
				throw new SmartScriptParserException("Invalid empty tag.");
			}
		}
		//create the final array used for EchoNode
		Element[] newElements = new Element[i];
		if (i >= 0) System.arraycopy(elements, 0, newElements, 0, i);
		return new EchoNode(newElements);
	}
	
	/**
	 * Returns the root of the generative tree, as represented by an instance of
	 * <code>DocumentNode</code>
	 *
	 * @return root of the generative tree
	 */
	public DocumentNode getDocumentNode() {
		return root;
	}
	
}
