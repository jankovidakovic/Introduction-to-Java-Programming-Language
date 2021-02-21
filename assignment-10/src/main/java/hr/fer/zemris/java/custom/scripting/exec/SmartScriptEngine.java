package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Engine capable of executing the smart script document.
 * 
 * @author jankovidakovic
 *
 */
public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();

	// visitor which executes the appropriate operations upon visiting nodes
	private INodeVisitor visitor = new INodeVisitor() {

		/**
		 * Executes the text node by writing its content to the request object
		 * passed in the constructor.
		 * 
		 * @param node node which content is to be written
		 */
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException ex) {
				throw new RuntimeException("Cannot execute text node.");
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			// initialize the for loop variable to start expression
			multistack.push(node.getVariable().asText(),
					new ValueWrapper(node.getStartExpression().asText()));
			// execute the for loop
			while (multistack.peek(node.getVariable().asText()).numCompare(
					node.getEndExpression().asText()) <= 0) {
				// single iteration - call accept on children
				for (int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
				// increment the variable by the step expression
				multistack.peek(node.getVariable().asText()).add(
						node.getStepExpression().asText());
			}
			// remove the variable from the stack
			multistack.pop(node.getVariable().asText());

			// done
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			// temporary stack of objects
			ObjectStack tempStack = new ObjectStack();

			// go through every token in the echo node
			for (int i = 0; i < node.getElements().length; i++) {
				Element currElem = node.getElements()[i];
				
				if (currElem instanceof ElementVariable) {
					//push latest variable value to the temporary stack
					tempStack.push(multistack.peek(currElem.asText()).getValue());
				} else if (currElem instanceof ElementOperator) {


					// TODO - check operator order

					// TODO - maybe refactor into helper methods

					// retrieve operators from the temp stack
					ValueWrapper op2 = new ValueWrapper(tempStack.pop());
					ValueWrapper op1 = new ValueWrapper(tempStack.pop());

					// apply operation
					// supported are only +, -, *, /
					switch (currElem.asText()) {
						case "+" -> op1.add(op2.getValue());
						case "-" -> op1.subtract(op2.getValue());
						case "*" -> op1.multiply(op2.getValue());
						case "/" -> op1.divide(op2.getValue());
						default -> throw new UnsupportedOperationException(
								"Unsupported operation");
					}

					// push result back to the stack
					tempStack.push(op1.getValue());

				} else if (currElem instanceof ElementFunction) {
					// 1. pop the required number of arguments from the stacc
					// 2. apply the function
					// 3. push the result back to the stack

					// only some functions are supported

					switch (currElem.asText()) {
					// calculates the sine of a single argument
					case "sin":
						// get argument
						Object arg = tempStack.pop();
						// calculate result and push back
						if (arg instanceof Integer) {
							tempStack.push(Math.sin((Integer) arg));
						} else if (arg instanceof Double) {
							tempStack.push(Math.sin((Double) arg));
						} else {
							throw new RuntimeException(
									"Invalid function argument.");
						}
						break;
					// formats the single numeric argument x using given format
					// f which is compatible with DecimalFormat, produces a
					// string.
					case "decfmt":
						// get formatter
						DecimalFormat f =
								new DecimalFormat((String) tempStack.pop());
						// get value to format
						Object x = tempStack.pop();
						// format and push back
						tempStack.push(f.format(x));
						break;

					// duplicates the last value (on the top of temp stack)
					case "dup":
						tempStack.push(tempStack.peek());
						break;

					// swaps the order of the last two values
					case "swap":
						Object a = tempStack.pop();
						Object b = tempStack.pop();
						tempStack.push(a);
						tempStack.push(b);
						break;

					// sets the mime type of the request to the given value
					case "setMimeType":
						Object mimeType = tempStack.pop();
						requestContext.setMimeType((String) mimeType);
						break;

					// retrieves the parameter from the request context. If
					// parameter with the given name isn't defined, pushes given
					// value instead. Takes two arguments - name and value
					case "paramGet":
						// get placeholder value
						Object paramDefValue = tempStack.pop();
						// get param name
						Object paramName = tempStack.pop();
						// retrieve the value associated with the name
						String paramValue =
								requestContext.getParameter((String) paramName);
						// push value to temp stack
						tempStack.push(paramValue == null ? paramDefValue
								: paramValue);
						break;

					// same as paramGet, but works with persistent parameters
					// instead
					case "pparamGet":
						// get placeholder value
						Object pParamDefValue = tempStack.pop();
						// get param name
						Object pParamName = tempStack.pop();
						// retrieve the value associated with the name
						String pParamValue =
								requestContext
										.getPersistentParameter(
												(String) pParamName);
						// push value to temp stack
						tempStack.push(
								pParamValue == null ? pParamDefValue
										: pParamValue);
						break;

					// stores a value into request persistent parameters
					case "pparamSet":
						Object newPParamName = tempStack.pop();
						Object newPParamValue = tempStack.pop();
						requestContext.setPersistentParameter(
								newPParamName.toString(),
								newPParamValue.toString());
						break;

					// deletes a persistent parameter with given name
					case "pparamDel":
						Object delPParamName = tempStack.pop();
						requestContext.removePersistentParameter(
								(String) delPParamName);
						break;

					// same as paramGet, but works with temporary parameters
					// instead
					case "tparamGet":
						// get placeholder value
						Object tParamDefValue = tempStack.pop();
						// get param name
						Object tParamName = tempStack.pop();
						// retrieve the value associated with the name
						String tParamValue =
								requestContext
										.getTemporaryParameter(
												(String) tParamName);
						// push value to temp stack
						tempStack.push(tParamValue == null ? tParamDefValue
								: tParamValue);
						break;

					// stores a value into request temporary parameters
					case "tparamSet":
						Object newTParamName = tempStack.pop();
						Object newTParamValue = tempStack.pop();
						requestContext.setTemporaryParameter(
								newTParamName.toString(),
								newTParamValue.toString());
						break;

					// deletes a temporary parameter with given name
					case "tparamDel":
						Object delTParamName = tempStack.pop();
						requestContext.removeTemporaryParameter(
								(String) delTParamName);
						break;
					default:
						throw new RuntimeException("Undefined function");
					}
				} else { // remains a constant
					// push constant value to the temporary stack
					if (currElem instanceof ElementConstantInteger) {
						tempStack.push(
								((ElementConstantInteger) currElem).getValue());
					} else if (currElem instanceof ElementConstantDouble) {
						tempStack.push(
								((ElementConstantDouble) currElem).getValue());
					} else if (currElem instanceof ElementString) {
						tempStack.push(
								((ElementString) currElem).getValue());
					} else {
						throw new RuntimeException("Undefined entitiy.");
					}
				}
			}
			// reverse the elements on the stack
			ObjectStack revStack = new ObjectStack();
			while (!tempStack.isEmpty()) {
				revStack.push(tempStack.pop());
			}

			// write the remaining elements to the request context output
			while (!revStack.isEmpty()) {
				try {
					requestContext.write(revStack.pop().toString());
				} catch (IOException ex) {
					throw new RuntimeException(
							"Unable to write to request context.");
				}

			}

			// finally done
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}

		}
		
	};

	/**
	 * Creates an instance of the engine, set to execute the smart script given
	 * by the document node, and write results to request context.
	 * 
	 * @param documentNode   root node of the parsed script to be executed
	 * @param requestContext request which will be used for writing the script
	 *                       result
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		super();
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes the script represented by the document node.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
