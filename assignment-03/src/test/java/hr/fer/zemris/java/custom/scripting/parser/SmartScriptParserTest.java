package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

class SmartScriptParserTest {

	private String readExample(int n) {
		  try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
		    if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
		    byte[] data = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt").readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		  } catch(IOException ex) {
		    throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		  }
		}
	
	void testNthExample(int n) {
		String text = readExample(n);
		SmartScriptParser parser = new SmartScriptParser(text);
		assertEquals(1, parser.getDocumentNode().numberOfChildren());
		assertEquals(true, parser.getDocumentNode().getChild(0) instanceof TextNode);
	}
	
	@Test
	void testOneTextNode() {
		String text = readExample(1);
		SmartScriptParser parser = new SmartScriptParser(text);
		assertEquals(1, parser.getDocumentNode().numberOfChildren());
		assertEquals(true, parser.getDocumentNode().getChild(0) instanceof TextNode);
	}
	
	@Test
	void testEscapedBracketInText() {
		String text = readExample(2);
		SmartScriptParser parser = new SmartScriptParser(text);
		assertEquals(1, parser.getDocumentNode().numberOfChildren());
		assertEquals(true, parser.getDocumentNode().getChild(0) instanceof TextNode);
	}
	
	@Test
	void testMultipleEscapesInText() {
		String text = readExample(3);
		SmartScriptParser parser = new SmartScriptParser(text);
		assertEquals(1, parser.getDocumentNode().numberOfChildren());
		assertEquals(true, parser.getDocumentNode().getChild(0) instanceof TextNode);
	}
	
	@Test
	void testIllegalEscapeInText() {
		String text = readExample(4);
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
		}
	
	@Test
	void testIllegalEscapeWithinQootesInText() {
		String text = readExample(5);
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testStringMultipleRows() {
		String text = readExample(6);
		SmartScriptParser parser = new SmartScriptParser(text);
		assertEquals(2, parser.getDocumentNode().numberOfChildren());
		assertEquals(true, parser.getDocumentNode().getChild(0) 
				instanceof TextNode);
		assertEquals(true, parser.getDocumentNode().getChild(1) 
				instanceof EchoNode);
		
	}
	
	@Test
	void testStringMultipleRowsAndValidEscapes() {
		String text = readExample(7);
		SmartScriptParser parser = new SmartScriptParser(text);
		assertEquals(2, parser.getDocumentNode().numberOfChildren());
		assertEquals(true, parser.getDocumentNode().getChild(0) 
				instanceof TextNode);
		assertEquals(true, parser.getDocumentNode().getChild(1) 
				instanceof EchoNode);
		
	}
	
	@Test
	void testStringInvalidEscape() {
		String text = readExample(8);
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testEchoNodeWithoutParameters() {
		String text = readExample(9);
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserInvalidEndTag() {
		String text = "Text"
				+ "{$FOR i 1 5 5 $}"
				+ "There should be the end tag here, but there is none.";
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserFullFunctionality() {
		String text = "This is a full test."
				+ "{$FOR i 1 5 1 $}"
				+ "i = {$ = i $}"
				+ "{$END$}";
		SmartScriptParser parser = new SmartScriptParser(text);
		
	}
	
	@Test
	void testParserInvalidForLoopVariableName() {
		String text = "{$ FOR * \"1\" -10 \"1\" $}";
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserInvalidForLoopFunctionElement() {
		String text = "{$ FOR year @sin 10 $}";
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserInvalidForLoopTooManyArguments() {
		String text = "{$ FOR year 1 10 \"1\" \"10\" $}";
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserInvalidForLoopTooFewArguments() {
		String text = "{$ FOR year $}";
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserTooManyEndTags() {
		String text = "{$ FOR i 1 5 1 $}"
				+ "{$END$}"
				+ "{$END$}";
		assertThrows(SmartScriptParserException.class, () -> 
		new SmartScriptParser(text));
	}
	
	@Test
	void testParserUltimateTest() {
		
	}
}
