package hr.fer.zemris.java.custom.collections;

/**
 * Represents some general collection of objects. It defines some 
 * standard methods which a general collection is expected to have.
 * Most of the methods should be overridden by child classes, as the way
 * they are defined here does not provide much functionality.
 *
 * @author jankovidakovic
 */
public class Collection {
	
	/**
	 * Default constructor.
	 */
	protected Collection() {
		
	}

	/**
	 * Determines whether a collection is empty(contains no elements).
	 *
	 * @return <code>true</code> if collection contains no elements, 
	 * 			<code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Determines how many objects are stored in a collection.
	 * Always returns 0, so it should probably be overridden in 
	 * child classes.
	 *
	 * @return number of currently stored objects (currently always 0)
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object to the collection. This particular
	 * implementation does nothing, so it should probably be overridden
	 * in child classes.
	 *
	 * @param value object to be added
	 */
	public void add(Object value) {
		//nothing
	}
	
	/**
	 * Checks whether the collection contains a given object, as determined
	 * by <code>equals</code> method.
	 * It's allowed to check whether the collection contains <code>null</code>.
	 * This particular implementation always returns <code>false</code>, so it 
	 * should probably be overridden in child classes.
	 *
	 * @param value object which is being looked for, can be <code>null</code>
	 * @return <code>true</code> if collection contains given object, 
	 * 			<code>false</code> otherwise.
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes one occurrence of a given object from the collection (not specified
	 * which one), if the collection contains the object, as determined by 
	 * <code>equals</code> method.
	 * This implementation always returns <code>false</code>, so it should 
	 * probably be overridden in child classes.
	 *
	 * @param value object to be removed
	 * @return <code>true</code> if collection contains given object 
	 * 			(and  consequently, one occurrence of it is removed),
	 * 			<code>false</code> otherwise.
	 * 
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array with size equal to the size of collection,
	 * fills it with content of the collection, and returns the new array.
	 * The order of the elements in the resulting array is not specified.
	 * Should never return a <code>null</code> reference.
	 * This implementation always throws <code>UnsupportedOperationException</code>,
	 * so it should probably be overridden in child classes.
	 * 
	 * @throws UnsupportedOperationException always(intended to override)
	 * @return array which contains elements of the collection
	 * 
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Processes every element of the collection using the <code>process</code>
	 * method of the given instance of <code>Processor</code> class. The order 
	 * in which elements are processed is not specified. This implementation 
	 * does nothing, and should probably be overridden in child classes.
	 *
	 * @param processor processor which is used to process elements.
	 */
	public void forEach(Processor processor) {
		//nothing
	}
	
	/**
	 * Adds all the elements of the given collection to the current 
	 * collection.
	 *
	 * @param other collection whose elements are to be added
	 */
	public void addAll(Collection other) {
		
		/*
		  Local class that represents a processor which adds an
		  object to the current collection.
		 */
		class AddToCollectionProcessor extends Processor {
			
			/**
			 * Default constructor
			 */
			protected AddToCollectionProcessor() {
				super();
			}
			
			/**
			 * Processes given object by adding it to the current collection.
			 *
			 * @param value object to be added
			 */
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		
		AddToCollectionProcessor adderProcessor = new AddToCollectionProcessor();
		
		other.forEach(adderProcessor);
	}
	
	/**
	 * Removes all elements from the collection. This implementation does
	 * nothing, so it should probably be overridden in child classes.
	 */
	public void clear() {
		//nothing
	}
}
