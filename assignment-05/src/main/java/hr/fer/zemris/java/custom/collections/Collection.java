package hr.fer.zemris.java.custom.collections;

/**
 * Represents the interface of some general collection of objects.
 * Declares some standard methods which a collection is supposed 
 * to have, regardless of its implementation. If some methods
 * are needed that are not declared here, they should be defined
 * in classes which implement this interface.
 *
 * @author jankovidakovic
 *
 * @param <E> type of elements stored in the collection
 */
public interface Collection<E> {
	
	/**
	 * Determines whether a collection is empty(contains no elements).
	 *
	 * @return <code>true</code> if collection contains no elements, 
	 * 			<code>false</code> otherwise.
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Determines how many objects are stored in a collection.
	 *
	 * @return number of currently stored objects (currently al
	 */
	int size();
	
	/**
	 * Adds the given object to the collection. It is not defined where
	 * exactly is object added to the collection, so this detail
	 * is up to the specific implementation of the method in classes
	 * that implement this interface.
	 *
	 * @param value object to be added
	 */
	void add(E value);
	
	/**
	 * Checks whether the collection contains a given object, as determined
	 * by <code>equals</code> method.
	 * It's allowed to check whether the collection contains <code>null</code>.
	 *
	 * @param value object which is being looked for, can be <code>null</code>
	 * @return <code>true</code> if collection contains given object, 
	 * 			<code>false</code> otherwise.
	 */
	boolean contains(Object value);
	
	/**
	 * Removes one occurrence of a given object from the collection (not specified
	 * which one), if the collection contains the object, as determined by 
	 * <code>equals</code> method.
	 *
	 * @param value object to be removed
	 * @return <code>true</code> if collection contains given object 
	 * 			(and  consequently, one occurrence of it is removed),
	 * 			<code>false</code> otherwise.
	 * 
	 */
	boolean remove(Object value);
	
	/**
	 * Allocates new array with size equal to the size of collection,
	 * fills it with content of the collection, and returns the new array.
	 * The order of the elements in the resulting array is not specified.
	 * Should never return a <code>null</code> reference.
	 * 
	 * @throws UnsupportedOperationException if operation of converting
	 * 	the collection to the array is not supported by the specific
	 * 	collection which implements this interface.
	 * @return array which contains elements of the collection
	 * 
	 */
	E[] toArray() throws UnsupportedOperationException;
	
	/**
	 * Processes every element of the collection using the <code>process</code>
	 * method of the given instance of <code>Processor</code>. The order 
	 * in which elements are processed is not specified.
	 *
	 * @param processor processor which is used to process elements.
	 */
	default void forEach(Processor<? super E> processor) {
		ElementsGetter<E> getter = createElementsGetter();
		getter.processRemaining(processor);
	}
	
	/**
	 * Adds all the elements of the given collection to the current 
	 * collection. Order of adding is not specified.
	 *
	 * @param other collection whose elements are to be added
	 */
	default void addAll(Collection<? extends E> other) {
		
		other.forEach(this::add);
	}
	
	/**
	 * Removes all elements from the collection.
	 */
	void clear();
	
	/**
	 * Creates an object which is used to access elements of the collection.
	 * One instance of <code>ElementsGetter</code> can only traverse the
	 * collection once and accesses elements from the beginning to the end.
	 * Once some element has been accessed, it can not be accessed again
	 * with the same instance of <code>ElementsGetter</code>, so new
	 * instance should be created if needed.
	 *
	 * @return instance of <code>ElementsGetter</code> of this collection.
	 */
	ElementsGetter<E> createElementsGetter();
	
	/**
	 * Adds all the elements from the given collection which are considered
	 * accepted by the given tester.
	 *
	 * @param col Collection from which elements will be added
	 * @param tester Tester which determines whether or not element of 
	 * 			given collection is acceptable(and will be added), or not.
	 */
	default void addAllSatisfying(Collection<? extends E> col, 
			Tester<? super E> tester) {
		ElementsGetter<? extends E> getter = col.createElementsGetter();
		Processor<E> adder = new Processor<>() {
			@Override
			public void process(E value) {
				if (tester.test(value)) {
					add(value);
				}
			}
		};
		getter.processRemaining(adder);
	}
}
