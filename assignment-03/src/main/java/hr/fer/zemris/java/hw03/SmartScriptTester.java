package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {
	
	public static void main(String[] args) {
		
		String documentBody = "This is sample text."
				+ "{$ FOR i 1 10 1 $}  "
				+ "This is {$= i $}-th time this message is generated."
				+ "{$END$}"
				+ "{$FOR i 0 10 2 $}  "
				+ "sin({$=i$}^2) = {$= i i * @sin  \"0.000\" @decfmt $}"
				+ "{$END$}";
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(documentBody);
		} catch (SmartScriptParserException ex) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception ex) {
			System.out.println("Critical fail");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		
		System.out.println(document);
		System.out.println(document2);
		
		boolean same = document.equals(document2);
		System.out.println(same);
	}
}
