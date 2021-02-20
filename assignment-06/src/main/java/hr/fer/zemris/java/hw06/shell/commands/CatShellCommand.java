package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Outputs the content of the given file to the console of the shell,
 * using the given charset to decode the file, or the default charset
 * if it was not provided.
 * Takes two arguments. First one is the path to some file.
 * Second one is charset name that should be used to interpret chars 
 * from bytes.
 *
 * @author jankovidakovic
 *
 */
public class CatShellCommand implements ShellCommand, Recoverable {

	/**
	 * Executes a command in the given environment, using provided arguments.
	 *
	 * @param env Environment for the execution of the command
	 * @param arguments arguments of the command. First argument should be
	 * a path to file and is mandatory. Second argument is charset name
	 * that is used to interpret chars from bytes. Second argument can be
	 * omitted. Arguments are given as a single string, which is then
	 * parsed accordingly.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		ArgumentsParser parser = new ArgumentsParser(arguments);
		Path path = null;
		if (parser.hasMoreArguments()) { //extract path
			try {
				path = parser.getNextPath();
			} catch (ArgumentsParserException ex) {
				return recoverFromError(env, ex.getMessage());
			}
		} else {
			recoverFromError(env, "Not enough arguments.");
		}
		Charset charset = null;
		if (parser.hasMoreArguments()) { //extract charset
			try {
				charset = parser.getNextCharset();
			} catch (ArgumentsParserException ex) {
				return recoverFromError(env, ex.getMessage());
			}
		} else {
			charset = Charset.defaultCharset();
		}
		
		if (parser.hasMoreArguments()) { //unexpected argument
			return recoverFromError(env, "Too many arguments.");
		}
		
		BufferedReader input;
		try {
			input = Files.newBufferedReader(path, charset);
		} catch (IOException ex) {
			return recoverFromError(env, "No such file.");
		}
			
		StringBuilder sb = new StringBuilder();
		int currentCharValue;
		while (true) {
			try {
				currentCharValue = input.read();
			} catch (IOException ex) {
				return recoverFromError(env, ex.getMessage());
			}
			if (currentCharValue == -1) {
				break; //EOF
			}
			sb.append((char)currentCharValue);
		}
		env.write(sb.toString());
		try {
			input.close();
		} catch (IOException e) {
			return recoverFromError(env, e.getMessage());
		}
		return ShellStatus.CONTINUE; //everything went well
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<String>();
		description.add("Opens a given file and writes its content to the "
				+ "console of MyShell.");
		description.add("Takes one or two arguments.");
		description.add("First argument is path to some file and is mandatory.");
		description.add("Second argument is charset name that should be used "
				+ "to interpret chars from bytes.");
		description.add("For list of available charset, see command "
				+ "\"charsets\".");
		return Collections.unmodifiableList(description);
	}

}
