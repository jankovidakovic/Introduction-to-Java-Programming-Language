/**
 * 
 */
package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Writes a given directory's listing on the output of the MyShell.
 * The output consists of 4 columns. First column indicates if current
 * object is directory (d), readable(r), writable(w) and executable(x).
 * Second column contains object size in bytes.
 * Third column contains file creation date/time.
 * Fourth column contains file name.
 *
 * @author jankovidakovic
 *
 */
public class LsShellCommand implements ShellCommand, Recoverable {

	/**
	 * Executes a command in a given environment and lists the given
	 * directory's content.
	 *
	 * @param env Environment in which the command is going to be executed
	 * @param arguments arguments of the command. Expected is one valid
	 * argument, which is the path to the directory.
	 * @return CONTINUE if the command executed successfully, TERMINATE
	 * if there was an error in writing to the console.
	 * @throws ShellIOException if something went wrong, e.g. wrong number
	 * of arguments, given path doesn't lead to a directory, etc.
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
		if (!Files.isDirectory(dir)) {
			return recoverFromError(env, "Given path is not a directory.");
		}
		try {
			Files.list(dir).forEach(file -> {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				BasicFileAttributeView faView = Files.getFileAttributeView(
				file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
				);
				BasicFileAttributes attributes = null;
				try {
					attributes = faView.readAttributes();
				} catch (IOException e) {
					env.write("Cannot read file attributes.");
				}
				
				StringBuilder fileInformation = new StringBuilder();
				
				fileInformation.append(Files.isDirectory(file) ? 'd' : '-');
				fileInformation.append(Files.isReadable(file) ? 'r' : '-');
				fileInformation.append(Files.isWritable(file) ? 'w' : '-');
				fileInformation.append(Files.isExecutable(file) ? 'x' : '-');
				fileInformation.append(' ');
				
				fileInformation.append(String.format("%10d", attributes.size()));
				fileInformation.append(' ');
				
				FileTime fileTime = attributes.creationTime();
				String formattedDateTime = sdf.format(
						new Date(fileTime.toMillis())
				);
				
				fileInformation.append(formattedDateTime);
				fileInformation.append(' ');
				
				fileInformation.append(file.getFileName().toString());
				
				env.writeln(fileInformation.toString());
			});
		} catch (IOException ex) {
			return recoverFromError(env, "Error in reading "
					+ "content of the directory.");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "ls";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<String>();
		description.add("Lists the content of given directory non-recursively.");
		description.add("Takes one argument, path to the directory.");
		return Collections.unmodifiableList(description);
	}

}
