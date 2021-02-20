package hr.fer.zemris.java.custom.collections;

/**
 * Model of a list-backed collection.
 *
 * @author jankovidakovic
 * @param <E> type of elements stored in the list.
 */
public interface List<E> extends Collection<E> {
	
	/**
	 * Retrieves the element of the collection at the given index.
	 *
	 * @param index index of the element. Valid indices are all
	 * 	non-negative integers smaller than the size of the list.
	 * @return Object which is stored in the list at the given index.
	 * @throws IndexOutOfBoundsException if invalid index is specified.
	 */
	E get(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Inserts the given value to the given position in the collection.
	 * Elements that are at greater positions will be shifter one place
	 * towards the end of the collection.
	 *
	 * @param value object which will be inserted
	 * @param position position at which the object will be inserted. Valid
	 * 	positions are all non-negative integers which are not greater
	 * 	than the number of elements stored in the list.
	 * @throws IndexOutOfBoundsException if invalid position is given.
	 */
	void insert(E value, int position) throws IndexOutOfBoundsException;
	
	/**
	 * Determines the index of the first occurrence of the given element
	 * within the collection. Equality is determined using <code>equals</code>
	 * method. If no such element is found, returns the appropriate value.
	 *
	 * @param value object which index is wanted. Valid indices are all
	 * 	non-negative integers smaller than the size of the list.
	 * @return index of the first occurrence, or <code>-1</code> if collection
	 * contains no given element.
	 * @throws IndexOutOfBoundsException if invalid index is given.
	 */
	int indexOf(Object value) throws IndexOutOfBoundsException;
	
	/**
	 * Removes the element of the position which is stored at given index.
	 * Elements at greater indices are shifted one place towards the 
	 * beginning.
	 *
	 * @param index Index of element which is to be removed. Valid indices are 
	 * all non-negative integers smaller than the size of the list.
	 * @throws IndexOutOfBoundsException if invalid index is given.
	 */
	void remove(int index) throws IndexOutOfBoundsException;
}
