package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used for parsing given string of arguments. String of arguments is passed
 * through the constructor of the parser. Arguments in string are expected
 * to be separated by whitespace.
 *
 * @author jankovidakovic
 *
 */
public class ArgumentsParser {

	private final String args; //arguments to parse
	private int pos; //position in the argument string
	
	/**
	 * Initializes the parser to parse given argument string.
	 *
	 * @param args arguments to parse
	 */
	public ArgumentsParser(String args) {
		this.args = args;
		this.pos = 0;
	}
	
	/**
	 * Tries to extract a valid path from string of arguments.
	 * Returns the path if successfully extracted.
	 *
	 * @return path from string of arguments
	 * @throws ArgumentsParserException if path was not extracted successfully
	 */
	public Path getNextPath() throws ArgumentsParserException {
		if (!hasMoreArguments()) {
			throw new ArgumentsParserException("No more arguments.");
		}
		
		boolean isQuotedPath = false; //denotes that a path is quoted
		
		skipWhitespace();
		int startingPosition = pos; //starting position of the argument;
		StringBuilder sb = new StringBuilder();
		
		while (pos < args.length()) {
			if (args.charAt(pos) == '\"') {
				if (pos == startingPosition) { //First character 
					isQuotedPath = true; //path is enclosed in double quotes
				} else if (isQuotedPath) { //ending quote
					if (pos+1 >= args.length() ||  
						Character.isWhitespace(args.charAt(pos+1))) {
						//valid ending of the argument
						isQuotedPath = false;
						pos++;
						break;
					}
				} else { //just an ordinary quote
					sb.append(args.charAt(pos));
				}
			} else if (args.charAt(pos) == '\\') {
				if (isQuotedPath) { //possibly escape sequence
					if (pos+1 < args.length()) { 
						if (args.charAt(pos+1) == '\"' 
						|| args.charAt(pos+1) == '\\') { //valid escape
							pos++; //skip backslash
						}
						sb.append(args.charAt(pos));
					} else {
						throw new ArgumentsParserException("Invalid escape.");
					}
				} else { //ordinary backslash
					sb.append(args.charAt(pos));
					
				}
			} else if (args.charAt(pos) == ' ') {
				if (isQuotedPath) { //part of the path
					sb.append(args.charAt(pos));
				} else { //end of the argument
					break;
				}
			} else { //regular character
				sb.append(args.charAt(pos));
			}
			pos++;
		}
		try {
			return Paths.get(sb.toString());
		} catch (InvalidPathException ex) {
			throw new ArgumentsParserException("Invalid path.");
		}
	}
	
	/**
	 * Skips the whitespace in the string that is being parsed
	 */
	private void skipWhitespace() {
		while (pos < args.length() && Character.isWhitespace(args.charAt(pos))) 
			pos++;
	}
	
	/**
	 * Tries to extract the valid charset name from the arguments, starting
	 * from the current position. Returns the charset which corresponds to 
	 * the given name.
	 *
	 * @return Charset from extracted charset name
	 * @throws ArgumentsParserException if no such charset exists or there was
	 * an error in reading
	 */
	public Charset getNextCharset() throws ArgumentsParserException {
		if (!hasMoreArguments()) {
			throw new ArgumentsParserException("No more arguments.");
		}
		skipWhitespace(); //skips leading whitespace
		StringBuilder sb = new StringBuilder();
		
		while (pos < args.length()) {
			if (Character.isWhitespace(args.charAt(pos))) {
				break; //end of the charset name
			} else {
				sb.append(args.charAt(pos));
				pos++;
			}
		}
		pos++;
		try {
			return Charset.forName(sb.toString());
		} catch (IllegalArgumentException ex) {
			throw new ArgumentsParserException("couldn't parse charset.");
		}
	}
	
	/**
	 * Returns the next sequence of non-whitespace characters from the
	 * arguments string.
	 *
	 * @return next sequence of non-whitespace characters from the 
	 * arguments string.
	 * @throws ArgumentsParserException if there are no more strings.
	 */
	public String getNextString() {
		if (!hasMoreArguments()) {
			throw new ArgumentsParserException("No more strings.");
		}
		skipWhitespace(); //skips leading whitespace
		StringBuilder sb = new StringBuilder();
		while (pos < args.length()) {
			if (Character.isWhitespace(args.charAt(pos))) { //end of string
				break;
			} else {
				sb.append(args.charAt(pos));
				pos++;
			}
		}
		pos++;
		return sb.toString();
	}
	
	/**
	 * Returns the next non-whitespace character from the arguments string.
	 *
	 * @return next non-whitespace character from the arguments string.
	 * @throws ArgumentsParserException if there are no more non-whitespace
	 * characters.
	 */
	public Character getNextSymbol() throws ArgumentsParserException {
		if (!hasMoreArguments()) {
			throw new ArgumentsParserException("No more characters.");
		}
		skipWhitespace();
		return args.charAt(pos++);
	}
	
	/**
	 * Checks whether parser could extract more arguments.
	 *
	 * @return <code>true</code> if there are more arguments to be extracted,
	 * <code>false</code>otherwise.
	 */
	public boolean hasMoreArguments() {
		return pos < args.length() && !args.substring(pos).isBlank();
	}
	
	/**
	 * Returns all the remaining characters in the provided argument string.
	 * Skips leading whitespace
	 *
	 * @return All remaining characters, starting from the first non-whitespace.
	 */
	public String getAllRemaining() {
		skipWhitespace();
		StringBuilder sb = new StringBuilder();
		while (pos < args.length()) {
			sb.append(args.charAt(pos));
			pos++;
		}
		return sb.toString();
	}
}
