package hr.fer.zemris.java.custom.collections;

/**
 * Model of a processor, which performs some operation on 
 * a given object.
 *
 * @author jankovidakovic
 */
public interface Processor {
	
	/**
	 * Performs some operation on a given object.
	 *
	 * @param value object to be processed
	 */
	void process(Object value);
}
