package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {
	
	public static void main(String[] args) {
		
		String documentBody = "{$= \"text/plain\" @setMimeType $}\n"
				+ "Prvih 10 fibonaccijevih brojeva je:\n"
				+ "{$= \"0\" \"a\" @tparamSet \"1\" \"b\" @tparamSet \"0\\r\\n1\\r\\n\" $}\n"
				+ "{$FOR i 3 10 1$}\n"
				+ "{$= \"b\" \"0\" @tparamGet @dup \"a\" \"0\" @tparamGet + \"b\" @tparamSet \"a\" @tparamSet \"b\" \"0\" @tparamGet \"\\r\\n\"$}\n"
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
