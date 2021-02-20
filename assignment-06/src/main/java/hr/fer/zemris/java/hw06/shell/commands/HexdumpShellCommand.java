package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that displays the file's content in hexadecimal format.
 * Takes one argument, a path to the file.
 *
 * @author jankovidakovic
 *
 */
public class HexdumpShellCommand implements Recoverable, ShellCommand {

	/**
	 * Executes the command in the given environment. Writes the file's
	 * content in hexadecimal format.
	 *
	 * @param env Environment of execution
	 * @param arguments arguments of the command. A single argument is 
	 * expected, a path to the file.
	 * @return ShellStatus.CONTINUE, allowing shell to continue running.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		Path file = null;
		if (parser.hasMoreArguments()) { //source file expected
			try {
				file = parser.getNextPath();
			} catch (ArgumentsParserException e) {
				return recoverFromError(env, e.getMessage());
			}
		} else { //no argument 
			return recoverFromError(env, "Not enough arguments.");
		}
		if (parser.hasMoreArguments()) { //only one argument expected
			return recoverFromError(env, "Too many arguments.");
		}
		if (Files.isDirectory(file)) {
			return recoverFromError(env, "Unable to hexdump a directory.");
		}
		
		InputStream input = null; //reading a file
		try {
			input = new BufferedInputStream(Files.newInputStream(file));
		} catch (IOException e) { //cannot open file for reading
			return recoverFromError(env, e.getMessage());
		}
		
		byte[] hexdumpBuffer = new byte[16]; //bytes of a file
		int availableBytes = 0;
		try {
			availableBytes = input.available();
		} catch (IOException e) {
			return recoverFromError(env, e.getMessage());
		}
		
		int totalBytesRead = 0;
		while (availableBytes > 0) { //there are more bytes to read
			try {
				int bytesRead = input.read(hexdumpBuffer); //read bytes
				
				env.write(String.format("%08X: ", totalBytesRead)); //row marker
				
				for (int i = 0; i < 16; i++) { //output bytes as hex
					if (i < bytesRead) {
						env.write(String.format("%02X", hexdumpBuffer[i]));
					} else {
						env.write("  ");
					} 
					if (i == 7) { //first |
						env.write("|");
					} else if (i == 15) { //second |
						env.write(" | ");
					} else {
						env.write(" "); //space between bytes
					}
				}
				
				for (int i = 0; i < bytesRead; i++) { //output bytes as text
					if (hexdumpBuffer[i] < 32 || hexdumpBuffer[i] > 127) {
						env.write(".");
					} else {
						env.write(Character.toString((char)hexdumpBuffer[i]));
					}
				}
				
				availableBytes = input.available(); //update the available
				totalBytesRead += 16; //row marker
				env.writeln(""); //new line
				
			} catch (IOException e) {
				return recoverFromError(env, e.getMessage());
			}
		}
		return ShellStatus.CONTINUE;
	}
	
	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<String>();
		description.add("Displays a file content in hexadecimal format.");
		description.add("ASCII characters of bytes are also displayed.");
		description.add("Only the standard subset of characters is shown.");
		description.add("For all other characters, a dot is printed instead.");
		description.add("Takes one argument, path to file to be hexdumped.");
		return Collections.unmodifiableList(description);
	}

}
