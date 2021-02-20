package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command which deals with special symbols in MyShell.
 * If started with a single argument, the name of the special symbol is expected,
 * and the current symbol configuration is displayed.
 * If started with two arguments, first argument should be the name of the 
 * special symbol, and second argument should be new value for the symbol.
 * Command then sets the symbol to the new value.
 *
 * @author jankovidakovic
 *
 */
public class SymbolShellCommand implements Recoverable, ShellCommand {

	/**
	 * Executes the command in the given environment. If Started with a single
	 * argument, displays the value of the requested symbol.
	 * If started with two arguments, changes the value of the symbol to the 
	 * given new value.
	 *
	 * @param env Environment of execution.
	 * @param arguments arguments of the command. If only a single argument is
	 * provided, it must be a symbol name. Symbol names are: PROMPT, MORELINES
	 * and MULTILINE. If two arguments are provided, first one must be a 
	 * symbol name, and second one must be a single character, a new value
	 * of the symbol.
	 * @return ShellStatus.CONTINUE, allowing shell to continue running.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		String symbolName = null;
		if (parser.hasMoreArguments()) { //try to get symbol name
			try {
				symbolName = parser.getNextString();
			} catch (ArgumentsParserException e) {
				return recoverFromError(env, e.getMessage());
			}
		} else {
			return recoverFromError(env, "Not enough arguments.");
		}
		Character newSymbolValue = null;
		if (parser.hasMoreArguments()) { //try to get new symbol value
			try {
				newSymbolValue = parser.getNextSymbol();
			} catch (ArgumentsParserException e) {
				return recoverFromError(env, e.getMessage());
			}
			Character oldSymbolValue;
			switch (symbolName) {
				case "PROMPT":
					oldSymbolValue = env.getPromptSymbol();
					env.setPromptSymbol(newSymbolValue);
					env.writeln("Symbol for PROMPT changed from '"
							+ oldSymbolValue + "' to '" + newSymbolValue + "'");
					break;
				case "MORELINES":
					oldSymbolValue = env.getMorelinesSymbol();
					env.setMorelinesSymbol(newSymbolValue);
					env.writeln("Symbol for MORELINES changed from '"
							+ oldSymbolValue + "' to '" + newSymbolValue + "'");
					break;
				case "MULTILINE":
					oldSymbolValue = env.getMultilineSymbol();
					env.setMultilineSymbol(newSymbolValue);
					env.writeln("Symbol for MULTILINE changed from '"
							+ oldSymbolValue + "' to '" + newSymbolValue + "'");
					break;
				default:
					return recoverFromError(env, "No such symbol.");
			}
		} else { //only a single argument is provided
			switch (symbolName) {
				case "PROMPT":
					env.writeln("Symbol for PROMPT is '"
							+ env.getPromptSymbol() + "'");
					break;
				case "MORELINES":
					env.writeln("Symbol for MORELINES is '"
							+ env.getMorelinesSymbol() + "'");
					break;
				case "MULTILINE":
					env.writeln("Symbol for MULTILINE is '"
							+ env.getMultilineSymbol() + "'");
					break;
				default:
					return recoverFromError(env, "No such symbol");
			}
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		description.add("Displays or changes the special symbols.");
		description.add("Special symbols are: PROMPT, MULTILINE, and MORELINES.");
		description.add("Takes one or two arguments.");
		description.add("First argument is the name of the special symbol.");
		description.add("Second argument is new value for the special symbol.");
		description.add("Second argument is optional.");
		description.add("If not present, command prints the current value "
				+ "of requested symbol");
		description.add("If present, command changes the value of symbol to the "
				+ "value of the second argument.");
		return Collections.unmodifiableList(description);
	}

}
