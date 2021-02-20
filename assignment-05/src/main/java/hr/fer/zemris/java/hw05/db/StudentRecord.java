package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Represents the records of students, as defined with following attributes:
 * jmbag, last name, first name and final grade. There can not exist multiple
 * records for the same student. Two students are deemed equal if they have
 * the same jmbag.
 *
 * @author jankovidakovic
 *
 */
public class StudentRecord {
	
	//private variables
	private final String jmbag; //jmbag;
	private final String lastName; //last name;
	private final String firstName; //first name;
	private final int finalGrade; //final grade
	
	
	/**
	 * @param jmbag student's jmbag
	 * @param lastName student's last name
	 * @param firstName student's first name
	 * @param finalGrade student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, 
			int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	
	/**
	 * Returns the student record's hash code, as determined by his
	 * jmbag and his jmbag only.
	 *
	 * @return student record hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	/**
	 * Determines whether two student records are equal. Since there cannot
	 * exist multiple records of the same student, two students are equal
	 * if their jmbag numbers are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StudentRecord)) {
			return false;
		}
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

	/**
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}
	

	
	/**
	 * @return last lame of the student
	 */
	public String getLastName() {
		return lastName;
	}
	

	/**
	 * @return first name of the student
	 */
	public String getFirstName() {
		return firstName;
	}
	

	
	/**
	 * @return final grade of the student
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

}
