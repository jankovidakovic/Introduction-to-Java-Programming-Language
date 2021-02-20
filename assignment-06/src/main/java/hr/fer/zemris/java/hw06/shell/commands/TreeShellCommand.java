package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command which prints a directory tree, starting from the given directory.
 * Each directory level shifts output two characters to the right.
 *
 * @author jankovidakovic
 *
 */
public class TreeShellCommand implements ShellCommand, Recoverable {

	/**
	 * Executes the command in the given environment using the given arguments.
	 * Only a single argument is expected, a path to the directory.
	 * Prints a directory tree from the given directory, recursively.
	 * Each directory level is shifted two spaces towards the right.
	 *
	 * @param env Environment of execution
	 * @param arguments arguments of the command. A single argument is expected,
	 * a directory path.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		Path dir = null;
		if (parser.hasMoreArguments()) {
			try {
				dir = parser.getNextPath();
			} catch (ArgumentsParserException ex) {
				return recoverFromError(env, ex.getMessage());
			}
		} else {
			return recoverFromError(env, "Not enough arguments.");
		}
		if (parser.hasMoreArguments()) {
			return recoverFromError(env, "Too many arguments.");
		}
		if (!Files.isDirectory(dir)) {
			return recoverFromError(env, "Invalid path, directory expected");
		}
		printTree(env, dir, 0);
		return ShellStatus.CONTINUE;
	}
	
	private void printTree(Environment env, Path path, int shift) {
		for (int i = 0; i < shift; i++) {
			env.write(" ");
		}
		env.writeln(path.getFileName().toString());
		if (Files.isDirectory(path)) {
			try {
				Files.list(path)
				.forEach(child -> printTree(env, child, shift+2));
			} catch (IOException ex) {
				env.writeln("Error in accessing files.");
			}
		}
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		description.add("Prints a directory tree.");
		description.add("Takes one argument, path to a directory.");
		return Collections.unmodifiableList(description);
	}

}
