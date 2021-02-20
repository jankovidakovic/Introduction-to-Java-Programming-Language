package hr.fer.zemris.java.hw05.db;

/**
 * Interface that can be used to filter student records.
 *
 * @author jankovidakovic
 *
 */
@FunctionalInterface
public interface IFilter {

	/**
	 * Tests a student record using some condition defined in the body of
	 * the method. Must accept the record if the condition is satisfied,
	 * and reject it otherwise.
	 *
	 * @param record Record to be checked against some condition
	 * @return <code>true</code> if the record satisfies the condition,
	 * 		<code>false</code> otherwise.
	 */
	boolean accepts(StudentRecord record);
}
