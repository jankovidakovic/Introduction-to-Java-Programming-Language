package hr.fer.zemris.java.hw05.db;

/**
 * Defines several concrete strategies for obtaining the field's values
 * from the student record. Concrete strategies are defined for the following
 * attributes: jmbag, lastName and firstName.
 *
 * @author jankovidakovic
 *
 */
public class FieldValueGetters {

	//static getters
	public static final IFieldValueGetter FIRST_NAME; //get value of first name
	public static final IFieldValueGetter LAST_NAME; //get value of last name
	public static final IFieldValueGetter JMBAG; //get value of jmbag
	//getting value of final grade is currently not supported
	
	//initialization of the static getters
	static {
		FIRST_NAME = StudentRecord::getFirstName;
		
		LAST_NAME = StudentRecord::getLastName;
		
		JMBAG = StudentRecord::getJmbag;
	}
}
