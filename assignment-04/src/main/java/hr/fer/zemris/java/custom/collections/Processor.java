package hr.fer.zemris.java.custom.collections;

/**
 * Model of a processor, which performs some operation on 
 * a given object.
 *
 * @author jankovidakovic
 * @param <T> type of object that processor can process
 */
public interface Processor<T> {
	
	/**
	 * Performs some operation on a given object.
	 *
	 * @param value object to be processed
	 */
	void process(T value);
}
