package hr.fer.zemris.java.hw05.db;

/**
 * Strategy which is responsible for obtaining a requested field value from
 * given <code>StudentRecord</code>.
 *
 * @author jankovidakovic
 *
 */
@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Obtains a field value from given student record. The field which is 
	 * being obtained is defined by the concrete implementation of this
	 * strategy interface.
	 *
	 * @param record Student record which field is to be obtained
	 * @return value of the specific field of the student record, as
	 * 		defined in the implementation of the strategy
	 */
	String get(StudentRecord record);
}
