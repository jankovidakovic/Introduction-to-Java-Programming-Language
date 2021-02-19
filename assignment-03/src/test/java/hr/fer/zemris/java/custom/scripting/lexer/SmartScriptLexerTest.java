package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SmartScriptLexerTest {

	@Test
	void testLexerOnlyText() {
		String inputText = "This is a text."
				+ "This is also a text.\nThis \t  is a \rtext.";
		SmartScriptLexer lexer = new SmartScriptLexer(inputText);
		SmartScriptToken token = lexer.nextToken();
		assertEquals(inputText, (String)token.getValue());
		token = lexer.nextToken();
		assertEquals(null, token.getValue());
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void testLexerTextInvalidEscape() {
		String inputText = "This is a \\$ test. This should \\\"crash.";
		SmartScriptLexer lexer = new SmartScriptLexer(inputText);
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void testLexerTextValidEscape() {
		String inputText = "This is a text which \\\\ contains valid \\{$escape sequences.";
		SmartScriptLexer lexer = new SmartScriptLexer(inputText);
		String resultText = "This is a text which \\ contains valid {$escape sequences.";
		assertEquals(resultText, (String)lexer.nextToken().getValue());
		
	}
	
	@Test
	void testLexerFullFunctionality() {
		String inputText = "This is sample text."
				+ "{$ FOR i 1 10 1 $}"
				+ "Content"
				+ "{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(inputText);
		SmartScriptToken token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, token.getType());
		assertEquals("This is sample text.", token.getValue());
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG_BEGIN, token.getType());
		assertEquals("{$", token.getValue());
		
		lexer.setState(SmartScriptLexerState.TAG_NAME);
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG_NAME, token.getType());
		assertEquals("FOR", token.getValue());
		
		lexer.setState(SmartScriptLexerState.TAG_CONTENT);
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.VARIABLE, token.getType());
		assertEquals("i", token.getValue());
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.INTEGER, token.getType());
		assertEquals(1, token.getValue());
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.INTEGER, token.getType());
		assertEquals(10, token.getValue());
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.INTEGER, token.getType());
		assertEquals(1, token.getValue());
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG_END, token.getType());
		assertEquals("$}", token.getValue());
		
		lexer.setState(SmartScriptLexerState.TEXT);
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TEXT, token.getType());
		assertEquals("Content", token.getValue());
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG_BEGIN, token.getType());
		assertEquals("{$", token.getValue());
		
		lexer.setState(SmartScriptLexerState.TAG_NAME);
		
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG_NAME, token.getType());
		assertEquals("END", token.getValue());
		
		lexer.setState(SmartScriptLexerState.TAG_CONTENT);
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG_END, token.getType());
		assertEquals("$}", token.getValue());
		
		lexer.setState(SmartScriptLexerState.TEXT);
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.EOF, token.getType());
		assertEquals(null, token.getValue());
		
		assertThrows(SmartScriptLexerException.class, () -> 
				lexer.nextToken());
		
	}
}
