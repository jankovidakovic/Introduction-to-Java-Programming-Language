package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A program which emulates a simple database of students. Database is loaded
 * from the current directory, from the file "database.txt". A file should 
 * contain multiple rows, each row consisting of a single database entry.
 * 
 * Database entry is entry which has the following attributes: jmbag,
 * lastName, firstName and finalGrade. Attributes should be separated by the
 * special character '\t' (tab).
 * 
 * Program prompts user for commands, and executes the given command upon the
 * database. The only command currently supported is "query". A query consists
 * of one or multiple conditional expressions, separated by the logical operators.
 * 
 * Conditional expression consists of an attribute name, comparison operator,
 * and a string literal. Supported attribute names are jmbag, lastName and
 * firstName. Supported comparison operators are "=" (equals), "!=" (not equals),
 * "<" (less than), "<=" (less or equal), ">" (greater), ">=" (greater or equal),
 * and operator "LIKE", which is used to match a field's value to the string
 * literal which can contain a single wildcard character '*', denoting any
 * possible sequence of characters, including an empty one. 
 * 
 * Currently, only the "AND" logical operator is supported. Logical operator
 * is case insensitive, so it can be written as "aND", "ANd", etc.
 * 
 *  
 * @author jankovidakovic
 *
 */
public class StudentDB {

	private static StudentDatabase db = null;

	public static void main(String[] args) {
		try {
			db = new StudentDatabase(
					Files.readAllLines(Paths.get("./database.txt"))
			);
		} catch (IOException ex) { //database file is missing
			System.out.println("ERROR: Missing database file. Provide "
					+ "\"database.txt\" in the program's current directory and"
					+ "then run the program again.");
			System.exit(0);
		}
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.println("> "); //prompt for query
			String nextLine = sc.nextLine(); //read next query
			
			if (nextLine.equals("exit")) { //end of program
				System.out.println("Goodbye!");
				sc.close();
				System.exit(0);
			}
			if (!nextLine.startsWith("query ")) { //invalid syntax
				System.out.println("Invalid syntax. Such command is not"
						+ " supported.");
			} else {
				try {
					QueryParser qp = new QueryParser(nextLine.substring(6));
					
					List<StudentRecord> records = db.filter( //filter records
							new QueryFilter(qp.getQuery())
					);
					
					if (records.isEmpty()) { //all were filtered
						System.out.println("Records selected: 0");
					} else { //some were filtered, but some remained
						List<String> output = format(records);
						if (qp.isDirectQuery()) { //print appropriate message
							System.out.println("Using index for record "
									+ "retrieval.");
						}
						output.forEach(System.out::println); //show records
						System.out.println("Records selected: " + records.size());
					}
					
				} catch (QueryParserException ex) { //invalid query syntax
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	/**
	 * Formats the given records for output to the console. Format used is 
	 * a table defined as follows:
	 * 	  ->top and bottom of the table are bounded by lines +=+=+=+=+,
	 * 		where each plus sign appears only once as a marker of attribute
	 * 		separation, and each equals sign repeats as many times as is 
	 * 		necessary to fit into it the value of the corresponding attribute,
	 * 		which is the longest of all provided records, and is surrounded
	 * 		by single spaces.
	 * 	  ->single record is represented as a line containing the following:
	 * 			| jmbag | lastName | firstName | finalGrade |
	 * 		where instead of attribute names, values of the record are provided.
	 * 		
	 * @param records Records to be formatted for output
	 * @return List of strings, each string being one line ready to output.
	 */
	private static List<String> format(List<StudentRecord> records) {
		List<String> output = new ArrayList<>();
		
		int jmbagFieldSize = 0; //longest jmbag
		int lastNameFieldSize = 0; //longest lastName
		int firstNameFieldSize = 0; //longest firstName
		int finalGradeFieldSize = 1; //longest finalGrade
		
		for (StudentRecord r : records) { //find the length of longest values
			jmbagFieldSize = Math.max(jmbagFieldSize, r.getJmbag().length());
			lastNameFieldSize = Math.max(lastNameFieldSize, 
					r.getLastName().length());
			firstNameFieldSize = Math.max(firstNameFieldSize, 
					r.getFirstName().length());
		}
		
		//top border
		output.add(tableBorder(jmbagFieldSize, lastNameFieldSize, 
				firstNameFieldSize, finalGradeFieldSize));
		
		StringBuilder sb; //build an output of the record
		
		for (StudentRecord r : records) {
			sb = new StringBuilder();
			sb.append("| ");
			sb.append(r.getJmbag());
			sb.append(" ".repeat(Math.max(0, jmbagFieldSize - r.getJmbag().length()))); //resize to match the longest jmbag
			sb.append(" | ");
			sb.append(r.getLastName());
			sb.append(" ".repeat(Math.max(0, lastNameFieldSize
					- r.getLastName().length()))); //resize to match the longest last name
			sb.append(" | ");
			sb.append(r.getFirstName());
			sb.append(" ".repeat(Math.max(0, firstNameFieldSize
					- r.getFirstName().length()))); //resize to match the longest first name
			sb.append(" | ");
			sb.append(r.getFinalGrade()).append(" |");
			output.add(sb.toString());
		}
		
		//bottom border
		output.add(tableBorder(jmbagFieldSize, lastNameFieldSize, 
				firstNameFieldSize, finalGradeFieldSize));
		
		return output;
	}
	
	/**
	 * Creates a table border of appropriate size according to the given
	 * parameters. 
	 * @param jmbagFieldSize Size of the jmbag field
	 * @param lastNameFieldSize Size of the last name field
	 * @param firstNameFieldSize Size of the first name field
	 * @param finalGradeFieldSize Size of the final grade field
	 * @return string representation of table border
	 */
	private static String tableBorder(int jmbagFieldSize, int lastNameFieldSize,
			int firstNameFieldSize, int finalGradeFieldSize) {

		return "+=" +
				"=".repeat(Math.max(0, jmbagFieldSize)) +
				"=+=" +
				"=".repeat(Math.max(0, lastNameFieldSize)) +
				"=+=" +
				"=".repeat(Math.max(0, firstNameFieldSize)) +
				"=+=" +
				"=".repeat(Math.max(0, finalGradeFieldSize)) +
				"=+";
	}
}
