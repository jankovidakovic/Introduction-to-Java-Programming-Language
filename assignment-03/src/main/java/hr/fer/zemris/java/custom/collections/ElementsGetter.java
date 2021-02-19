package hr.fer.zemris.java.custom.collections;

import java.util.NoSuchElementException;

/**
 * Interface which serves as a retriever of the elements of some collection.
 * Elements of the collection are retrieved in order from first to last.
 * Once initialized, instance of <code>ElementsGetter</code> should be able
 * to retrieve the first element of the collection.
 * Each element can only be retrieved once, so one instance of
 * <code>ElementsGetter</code> is only useful for one traversal of the collection.
 * If multiple traversals are needed, or some objects need to be retrieved 
 * more than once, one can create multiple instances of this interface, and they
 * will all be completely independent of each other.
 *
 * @author jankovidakovic
 */
public interface ElementsGetter {
	
	/**
	 * Checks whether collection contains any elements that have not
	 * been retrieved yet. Guaranteed to be idempotent, in other words,
	 * every concurrent call of this method shold return the same value,
	 * if the <code>getNextElement</code> has not been called in between.
	 *
	 * @return <code>true</code> if there are more elements to be retrieved,
	 * <code>false</code> otherwise.
	 */
	boolean hasNextElement();
	
	/**
	 * Retrieves the next element of the collection. Method should be implemented
	 * to use <code>hasNextElement</code> method to determine whether there are
	 * any elements remaining. if <code>hasNextElement</code> returns
	 * <code>false</code>, this method should throw an exception.
	 * @throws NoSuchElementException if there are no more elements that 
	 * can be retrieved(<code>hasNextElement</code> returned <code>false</code>).
	 *
	 * @return next element of the collection which has not been retrieved yet.
	 * 
	 */
	Object getNextElement() throws NoSuchElementException;
	
	/**
	 * Processes all the remaining elements of the collection, the ones not already retrieved.
	 * using the given processor. Remaining elements are consumed by 
	 * this operation, meaning that instance of <code>ElementsGetter</code>
	 * upon which this method was called is unable to retrieve any more
	 * elements.
	 *
	 * @param p Processor which is used to process elements.
	 */
	default void processRemaining(Processor p) {
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
