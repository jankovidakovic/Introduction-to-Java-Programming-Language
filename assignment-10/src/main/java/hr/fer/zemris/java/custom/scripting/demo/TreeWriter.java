package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program that demonstrates the functionality of the smart script parser.
 * Program takes path to smart script file from the commmand line, parses it,
 * and produces a document model. Then, using the visitor design patter, program
 * recreates the original document from the model, and outputs its content to
 * the standard output.
 * 
 * @author jankovidakovic
 *
 */
public class TreeWriter {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println(
					"Expecting a single argument - path to some smart script.");
			System.exit(-1);
		}
		InputStream is = Files.newInputStream(Paths.get(args[0]));
		String docBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		try {
			SmartScriptParser p = new SmartScriptParser(docBody);
			WriterVisitor visitor = new WriterVisitor();
			p.getDocumentNode().accept(visitor);
		} catch (SmartScriptParserException e) {
			System.out.println("Invalid smart script format.");
		}

		is.close();
	}

	/**
	 * Visitor used to recreate the original document from document model.
	 * 
	 * @author jankovidakovic
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.printf(node.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.printf(node.toString());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.printf(node.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}

	}

}
