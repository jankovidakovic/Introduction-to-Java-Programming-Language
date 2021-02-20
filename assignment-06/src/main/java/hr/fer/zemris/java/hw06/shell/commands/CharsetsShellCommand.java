package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command "charsets" lists the available charsets on the Java platform
 * on which the shell is running. It lists one charset name per line.
 * It takes no arguments.
 *
 * @author jankovidakovic
 *
 */
public class CharsetsShellCommand implements ShellCommand, Recoverable {

	
	/**
	 * Executes a command in the given environment. Writes each available
	 * charset as a single line.
	 *
	 * @param env Environment of execution
	 * @param arguments since this command takes no arguments, this parameter
	 * is ignored
	 * @return shell status after command execution. Always returns 
	 * ShellStatus.CONTINUE, because there can not occur an error in 
	 * writing or reading operations on files, since no such operations
	 * are performed.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		if (parser.hasMoreArguments()) {
			return recoverFromError(env, "Too many arguments.");
		}
		Set<String> charsets = Charset.availableCharsets().keySet();
		for (String charsetName : charsets) {
			env.writeln(charsetName);
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		description.add(
		"Lists names of supported charsets for the Java platform on which the");
		description.add("MyShell is running. Lists a single charset per line."
				+ " Takes no arguments.");
		return Collections.unmodifiableList(description);
	}

}
