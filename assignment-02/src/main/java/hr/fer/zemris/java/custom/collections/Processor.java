package hr.fer.zemris.java.custom.collections;

/**
 * Model of a processor, which can perform some operation on 
 * some object. This particular implementation doesn't process
 * given object in any way, but is intended to be used as a parent
 * class for actual, useful Processors.
 *
 * @author jankovidakovic
 *
 */
public class Processor {
	
	/**
	 * Default constructor with no parameters.
	 */
	protected Processor() {
		//nothing
	}
	
	/**
	 * Performs some operation on a given object.
	 * This implementation does nothing.
	 *
	 * @param value object to be processed
	 */
	public void process(Object value) {
		//nothing
	}
}
