package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that copies given source file to the given destination file. 
 * If the destination file is a directory, source file is copied inside
 * the directory, and keeps the same name.
 * Command takes two arguments. First one is path to source file, and second
 * one is path to the destination file(or a directory)
 *
 * @author jankovidakovic
 *
 */
public class CopyShellCommand implements Recoverable, ShellCommand {

	/**
	 * Executes a command in the given environment using the given arguments.
	 * Copies a given source file to the destination file, or inside the 
	 * destination directory.
	 *
	 * @param env Environment of execution
	 * @param arguments arguments of the command. Two arguments are expected.
	 * First one is path to source file, second one is path to the destination
	 * file or destination directory.
	 * @return ShellStatus.CONTINUE, allowing shell to continue running.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		Path src = null; //source file
		Path dest = null; //destination file or folder
		if (parser.hasMoreArguments()) { //source file expected
			try {
				src = parser.getNextPath();
			} catch (ArgumentsParserException ex) { //something went wrong
				return recoverFromError(env, ex.getMessage());
			}
		} else { //no source file provided
			return recoverFromError(env, "Not enough arguments.");
		}
		
		if (parser.hasMoreArguments()) { //destination file expected
			try {
				dest = parser.getNextPath();
			} catch (ArgumentsParserException ex) { //something went wrong
				return recoverFromError(env, ex.getMessage());
			}
		} else { //no destination file provided
			return recoverFromError(env, "Not enough arguments.");
		}
		
		if (parser.hasMoreArguments()) { //only 2 expected
			return recoverFromError(env, "Too many arguments.");
		}
		
		if (Files.isDirectory(src)) { //source must be a file
			return recoverFromError(env, "Cannot copy a directory.");
		}
		
		if (Files.isDirectory(dest)) { //destination is a directory
			String destPath = dest.toString()
					+ File.separator
					+ src.getFileName().toString();
			dest = Paths.get(destPath);
		}
		
		if (Files.exists(dest)) { //destination file exists
			env.write("Destination file already exists. Overwrite? [Y/n]");
			String response = env.readLine();
			if (response.toUpperCase().equals("N")) {
				return recoverFromError(env, "Destination file shall not be overwritten.");
			}
		} else { //destination file does not exist
			try {
				dest.toFile().createNewFile();
			} catch (IOException ex ) {
				return recoverFromError(env, ex.getMessage());
			}
		}
		
		InputStream input = null; //reading from source file
		try {
			input = new BufferedInputStream(Files.newInputStream(src));
		} catch (IOException ex) {
			return recoverFromError(env, "Cannot read from source file.");
		}
		
		OutputStream output = null; //writing to destination file
		try {
			output = new BufferedOutputStream(Files.newOutputStream(dest));
		} catch (IOException ex) {
			return recoverFromError(env, "Cannot read from source file.");
		}
		
		byte[] copyBuffer = new byte[4096]; //buffer array
		int availableBytes = 0;
		
		try {
			availableBytes = input.available();
		} catch (IOException e) {
			return recoverFromError(env, e.getMessage());
		}
		
		while (availableBytes > 0) { //there is more bytes to copy
			try {
				int bytesRead = input.read(copyBuffer); //fill buffer
				availableBytes = input.available();
				output.write(Arrays.copyOf(copyBuffer, bytesRead)); //copy
				output.flush();
			} catch (IOException e) {
				return recoverFromError(env, e.getMessage());
			}
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "copy";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<String>();
		description.add("Copies a source file to the destination file.");
		description.add("If the destination file exists, asks to overwrite.");
		description.add("If the destination is a directory, copies the source file "
				+ "into the directory.");
		description.add("Takes two arguments. ");
		description.add("First is path to source file, which must not be a directory.");
		description.add("Second is path to designation file, which may be a directory.");
		return Collections.unmodifiableList(description);
	}

}
