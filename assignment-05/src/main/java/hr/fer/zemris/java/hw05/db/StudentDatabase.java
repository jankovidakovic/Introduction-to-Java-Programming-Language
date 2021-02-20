package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model of a simple database that consists of student records. A single
 * student record consists of student's jmbag, first name, last name and 
 * final grade. This model can perform the simple operation of filtering
 * the records, using an instance of <code>IFilter</code> interface, or 
 * retrieve the record using it's jmbag in O(1) time.
 *
 * @author jankovidakovic
 *
 */
public class StudentDatabase {

	//private variables
	private final List<StudentRecord> records; //list of student records
	private final Map<String, Integer> jmbagIndex; //map used for O(1) retrieval by jmbag
	
	public StudentDatabase(List<String> records) {
		//initialize the data structures
		this.records = new ArrayList<>();
		this.jmbagIndex = new HashMap<>();
		
		//insert the given records into the database
		records.forEach(record -> {
			//try to parse record, exception could be thrown
			StudentRecord parsedRecord = parseRecord(record);
			
			//check if entry is valid
			if (jmbagIndex.containsKey(parsedRecord.getJmbag())) { //duplicate
				throw new IllegalArgumentException("There can not exist "
						+ "multiple entries with the same jmbag.");
			}
			if (parsedRecord.getFinalGrade() < 1 || 
					parsedRecord.getFinalGrade() > 5) { //invalid grade
				throw new IllegalArgumentException("Invalid grade for entry "
						+ "at index " + this.records.size());
			}
			
			//everything is fine, insert the entry into the database
			jmbagIndex.put(parsedRecord.getJmbag(), this.records.size());
			this.records.add(parsedRecord);
		});
	}
	
	/**
	 * Parses a given string and turns it into a student record. Does not
	 * check whatever a given record is valid in regards to the existing
	 * database content, just checks if it has the correct number of 
	 * attributes and of the correct type.
	 *
	 * @param record String representation of student record to be parsed.
	 * @return instance of <code>StudentRecord</code> containing the same
	 * 			information as the given input string.
	 */
	private StudentRecord parseRecord(String record) {
		String[] recordTokens = record.split("\t"); //tokens are split by tab
		
		if (recordTokens.length != 4) { //needs 4 attributes to be valid
			throw new IllegalArgumentException("Wrong number of attributes.");
		}
		
		try {
			return new StudentRecord(recordTokens[0], recordTokens[1], 
					recordTokens[2], Integer.parseInt(recordTokens[3]));
		} catch (NumberFormatException ex) { //grade needs to be an integer
			throw new IllegalArgumentException("Given grade is not an integer.");
		}
	}

	/**
	 * Obtains the student record using the given jmbag in constant time.
	 *
	 * @param jmbag Jmbag of the wanted record
	 * @return student record that has the same jmbag, or <code>null</code>
	 * 		if such record does not exist
	 */
	public StudentRecord forJMBAG(String jmbag) {
		Integer index = jmbagIndex.get(jmbag); //find record by index
		if (index == null) { //no such record
			return null;
		} else { //record exists
			return records.get(index);
		}
	}
	
	/**
	 * Filters the student records in the database using the given filter.
	 * Returns the filtered content as a new list of student records.
	 *
	 * @param filter filter by which the database will be filtered
	 * @return list of student records which are accepted by the given
	 * 		filter.
	 */
	public List<StudentRecord> filter(IFilter filter) {
		return records.stream()
					.filter(filter::accepts)
					.collect(Collectors.toList());
	}
}
