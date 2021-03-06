package hr.fer.zemris.java.custom.collections;

/**
 * Model of an object which takes in some another object and performs some
 * test to determine whether given object is acceptable, or not.
 *
 * @author jankovidakovic
 * @param <T> type of object that tester can test.
 */
public interface Tester<T> {
	
	/**
	 * Determines whether or not the given object is acceptable.
	 *
	 * @param obj Object which will be tested
	 * @return <code>true</code> 
	 */
	boolean test(T obj);
}
