package hr.fer.zemris.java.hw06.shell;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.*;

/**
 * Program that simulates a simple shell. MyShell implements the following
 * commands:
 * 		cat - outputs content of file in textual format, using the given charset
 * 		charsets - lists all the available charsets
 * 		copy - copies the source file to the destination file or inside directory
 * 		exit - exits the MyShell
 * 		help - displays helpful information about commands
 * 		hexdump - displays content of file in hexadecimal format.
 * 		ls - lists content of given directory
 * 		mkdir - creates an ew directory with all the necessary parent directories
 * 		symbol - manages the special symbols
 * 		tree - lists the directory structure starting from the given directory.
 * For a more detailed description of the commands, run "help" followed by the
 * command name.
 * Shell supports entering commands across multiple lines, for which one should
 * terminate each line of the command with the special MORELINES symbol.
 *
 * @author jankovidakovic
 *
 */
public class MyShell {

	private final SortedMap<String, ShellCommand> commands;

	/**
	 * Implementation of MyShell environment
	 *
	 * @author jankovidakovic
	 *
	 */
	private class MyShellEnvironment implements Environment {

		private final Scanner sc; //scanner used to read user input
		private char prompt; //prompt symbol
		private char multiline; //multiline symbol
		private char morelines; //morelines symbol

		
		/**
		 * Creates an environment with given parameters
		 *
		 * @param sc Scanner to read from
		 * @param prompt prompt symbol
		 * @param multiline multiline symbol
		 * @param morelines morelines symbol
		 */
		public MyShellEnvironment(Scanner sc, char prompt, char morelines, 
				char multiline) {
			this.sc = sc;
			this.prompt = prompt;
			this.multiline = multiline;
			this.morelines = morelines;
		}

		@Override
		public String readLine() throws ShellIOException {
			try {
				return sc.nextLine();
			} catch (NoSuchElementException | IllegalStateException e) {
				throw new ShellIOException("Unable to read line.");
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			System.out.printf("%s", text);

		}

		@Override
		public void writeln(String text) throws ShellIOException {
			System.out.println(text);

		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Character getMultilineSymbol() {
			return multiline;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multiline = symbol;

		}

		@Override
		public Character getPromptSymbol() {
			return prompt;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			prompt = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return morelines;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			morelines = symbol;
		}

	}
	
	public MyShell() {
		
		//initialize the commands
		commands = new TreeMap<>();

		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("tree", new TreeShellCommand());

		//other private fields are initialized at the shell startup("lazy")
	}
	
	public void start() {
		
		Scanner sc = new Scanner(System.in);
		//build an environment
		Environment env = new MyShellEnvironment(sc, '>', '\\', '|');
		
		//start the shell
		env.writeln("Welcome to MyShell v 1.0");
		ShellStatus status = ShellStatus.CONTINUE;
		do {
			
			env.write(env.getPromptSymbol().toString() + " "); //prompt
			
			StringBuilder nextCommandSB = new StringBuilder();
			String nextLine;
			while (true) {
				nextLine = env.readLine(); //read next line
				if (nextLine.endsWith(env.getMorelinesSymbol().toString())) {
					//more lines follow
					nextCommandSB.append(
							nextLine,
							0, nextLine.length() - 1);
					env.write(env.getMultilineSymbol().toString() + " ");
					
				} else { //no more lines
					nextCommandSB.append(nextLine);
					break;
				}
			}
			
			String nextCommand = nextCommandSB.toString();
			//extract command parts
			ArgumentsParser parser = new ArgumentsParser(nextCommand);
			String commandName;
			String commandArgs = "";
			if (parser.hasMoreArguments()) { //command name expected
				try {
					commandName = parser.getNextString();
				} catch (ArgumentsParserException e) { //error
					env.write("Error in reading command name.");
					continue;
				}
			} else { //empty command
				continue;
			}
			if (parser.hasMoreArguments()) { //some arguments are provided
				commandArgs = parser.getAllRemaining();
			}
			
			if (commands.containsKey(commandName)) { //command exists
				ShellCommand command = commands.get(commandName); //fetch
				status = command.executeCommand(env, commandArgs);//execute
			} else {
				env.writeln("Unknown command.");
			}
		} while (status != ShellStatus.TERMINATE);
	}
	
	
	public static void main(String[] args) {
		
		MyShell shell = new MyShell();
		shell.start();
		
	}
}
