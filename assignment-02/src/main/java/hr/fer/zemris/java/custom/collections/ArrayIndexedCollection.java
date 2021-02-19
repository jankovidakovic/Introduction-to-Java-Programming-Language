package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Implementation of resizeable array-backed collection of objects.
 * Methods from parent class <code>Collection</code> which do not have
 * adequate implementation are overridden in this class.
 * Provides all expected methods of a random access collection.
 * Some methods are implemented to throw exceptions on unsupported operations,
 * but this exceptions are not declared in method declaration, since none
 * of them are checked exceptions. Furthermore, all exceptions are thoroughly
 * documented in corresponding JavaDocs of the methods.
 * Duplicate elements are allowed, storage of <code>null</code> references
 * is not allowed.
 *
 * @author jankovidakovic
 */
public class ArrayIndexedCollection extends Collection {

	//private variables
	private int size; 		//number of elements stored in the collection
	Object[] elements;		//array of object references
	
	/**
	 * Constructs an empty collection with capacity to store 16 elements.
	 */
	public ArrayIndexedCollection() {
		super();							//constructor of parent class
		final int capacity = 16;			//initial capacity
		elements = new Object[capacity];	//allocation of memory
		size = 0;							//contains no actual elements yet
	}
	
	/**
	 * Constructs empty collection with the given initial capacity.
	 *
	 * @param initialCapacity initial capacity of the collection.
	 * 			Valid capacity is a positive whole number.
	 * @throws IllegalArgumentException if given capacity is invalid.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) { 				//capacity can't be smaller than 1
			throw new IllegalArgumentException("Capacity specified is not allowed.");
		} else {
			elements = new Object[initialCapacity];	//memory allocation
			size = 0;							//contains no elements yet
		}
	}
	
	/**
	 * Constructs a non-empty collection which contains copies of elements
	 * of the given collection.
	 *
	 * @param otherCollection collection which elements are to be copied. Valid if not null and has positive capacity.
	 * e.g. <code>new ArrayIndexedCollection(new ArrayIndexedCollection(0))</code> 
	 * 		is not allowed and will cause an exception.
	 * @throws IllegalArgumentException if given collection has no capacity, which can happen in the example above.
	 * @throws NullPointerException if given collection is <code>null</code>
	 */
	public ArrayIndexedCollection(Collection otherCollection) {
		this(otherCollection.size());		//throws IllegalArgumentException
		addAll(otherCollection);			//copy all elements
		size = otherCollection.size();		//set size appropriately
	}
	
	
	/**
	 * Constructs a non-empty collection which contains copies of all elements 
	 * from other collection. Reference to the other collection must not be 
	 * <code>null</code>. Constructed collection is guaranteed to have the capacity
	 * to store all elements of given collection, even if given initial capacity
	 * is smaller than that.
	 *
	 * @param otherCollection collection to copy elements from
	 * @param initialCapacity initial capacity of this collection
	 * @throws NullPointerException if the given collection is <code>null</code>
	 * @throws IllegalArgumentException if given collection is invalid, e.g.
	 * <code>new ArrayIndexedCollection(new ArrayIndexedCollection(0), 5)</code>
	 */
	public ArrayIndexedCollection(Collection otherCollection, int initialCapacity) {
		//fills current collection with elements of the other collection
		this(otherCollection);	//could throw IllegalArgumentException 
								//or NullPointerException
		
		if (initialCapacity > otherCollection.size()) {	//capacity can be increased
			resize(initialCapacity);					
		}
	}
	
	/**
	 * Adds the given object to the end of the collection. If collection is full,
	 * its inner array will be reallocated and capacity will be doubled.
	 * Works in amortised O(1).
	 *
	 * @param value object which is to be added into the collection
	 * @throws NullPointerException if given value is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		if (value == null) {	//collection cannot contain null elements
			throw new NullPointerException("Array cannot contain null references.");
		} else {
			if (size >= elements.length) {	//resize the array
				resize(2 * elements.length);
			}
			elements[size] = value;		//add new element
			size++;						//increment size
		}
	}
	
	/**
	 * Returns element that is stored in collection at position index.
	 * Works in O(1) time complexity.
	 *
	 * @param index position of element that is to be returned.
	 * 		Valid indexes are from 0(inclusive) to size of the collection(exclusive).
	 * @throws IndexOutOfBoundsException if given index is not valid.
	 * @return reference to the element at given index.
	 */
	public Object get(int index) {
		if (index < 0 || index > size-1) { //invalid index
			throw new IndexOutOfBoundsException("Given index doesn't exist");
		} else {
			return elements[index];
		}
	}
	
	/**
	 * Removes all elements from the collection. Capacity of the collection
	 * does not decrease. Elements become eligible for garbage collection.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;		// so that the memory can be cleaned
		}
		size = 0;	//collection contains no more elements
	}
	
	/**
	 * Inserts given element to the collection at given position.
	 * Does not overwrite existing elements, elements with greater index are
	 * shifted one place towards the end. If collection is at full capacity, it
	 * will be reallocated and its capacity will double.
	 *
	 * @param value object which is to be inserted 
	 * @param position position at which the given object will be inserted.
	 * 		Valid positions are from 0 to <code>size</code> (both inclusive)
	 */
	public void insert(Object value, int position) {
		if (position < 0 || position > size) { //invalid position
			throw new IndexOutOfBoundsException("Invalid insert position");
		} else {
			if (size+1 >= elements.length) {//capacity is not enough for new element
				resize(2 * elements.length);	//doubles the capacity
			}
			if (size - position >= 0) { //elements at greater index are shifted by one towards the end of the array
				System.arraycopy(elements, position, elements, position + 1, size - position);
			}
			elements[position] = value;		//inserting the given value			
			size++;							//increasing size
		}
	}
	
	/**
	 * Searches collection and finds the first occurrence of the given element.
	 * Given element can be <code>null</code>, but it is guaranteed that collection
	 * does not contain such element.
	 * Equality is determined using the <code>equals</code> method.
	 * Average time complexity is O(n), where n is the number of elements stored(size).
	 *
	 * @param value element to be searched for
	 * @return index of first occurrence of the given element, or -1 if collection
	 * 			does not contain the element.
	 */
	int indexOf(Object value) {
		int indexOfFirstOccurrence = -1; //return value, initialized for null
		
		if (value != null) {			//null is guaranteed not to be found
			for (int i = 0; i < size; i++) {	//from beginning to end(for first occurrence)
				if (elements[i].equals(value)) {
					indexOfFirstOccurrence = i;	//remember the index
					break;						//no point in searching further
				}
			}
		}
		return indexOfFirstOccurrence;
	}
	
	/**
	 * Removes element at given index. Elements with indexes greater than given index
	 * are shifted one place towards the beginning of the collection.
	 *
	 * @param index position of element which is to be removed. Valid values are from 0
	 * 			to <code>size-1</code> (inclusive).
	 * @throws IndexOutOfBoundsException if invalid index is given
	 */
	public void remove(int index) {
		if (index < 0 || index > size-1) { 			//invalid index
			throw new IndexOutOfBoundsException("Invalid index.");
		} else {
			if (size - 1 - index >= 0) { //shift elements to the left
				System.arraycopy(elements, index + 1, elements, index, size - 1 - index);
			}
			elements[size-1] = null;	//for garbage collection
			size--;
		}
	}
	
	/**
	 * Checks whether collection contains any elements.
	 *
	 * @return <code>true</code> if collection is empty, <code>false</code> otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size==0;
	}
	
	/**
	 * Determines how many elements are currently stored in the collection.
	 *
	 * @return number of elements in the collection
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Checks whether the collection contains a given object, as determined
	 * by <code>equals</code> method.
	 * It's allowed to check whether the collection contains <code>null</code>, but
	 * it is guaranteed that the result of that operation will be <code>false</code>.
	 *
	 * @param value object which the collection is being searched for.
	 * 		Valid are all objects, including <code>null</code>
	 * @return <code>true</code> if collection contains given object, 
	 * 			<code>false</code> otherwise
	 */
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Removes first occurrence of a given object from the collection.
	 * All elements with index greater than the one of removed element are
	 * shifted one place towards the beginning. Process of finding the object
	 * is based on <code>equals</code> method.
	 *
	 * @param value object to be removed
	 * @return <code>true</code> if collection contains given object
	 * 		(and consequently, its first occurrence is removed),
	 * 		<code>false</code> otherwise
	 */
	public boolean remove(Object value) {
		int index = indexOf(value); //tries to find the given object
		if (index == -1) {	//collection doesn't contain given element
			return false;
		} else {
			remove(index);	//removes the first occurrence of element
			return true;
		}
	}
	
	/**
	 * Allocates new array, fills it with content of the collection,
	 * and returns the new array. The order of the elements in the
	 * resulting array is the same as the order in the collection.
	 *
	 * @throws UnsupportedOperationException if there is not enough memory
	 * 			to allocate a new array
	 * @return array filled with elements of the collection. Guaranteed to
	 * 			never be <code>null</code> (throws exception instead)
	 */
	@Override
	public Object[] toArray() {
		Object[] newArray = new Object[size];	//allocate the new array
		try {									//check if it is null
			Objects.requireNonNull(newArray, "must not return null");
		} catch (NullPointerException ex) {	//array was not allocated successfully
			throw new UnsupportedOperationException(ex.getMessage());
		}
		//copying all elements to the new array
		if (size >= 0) {
			System.arraycopy(elements, 0, newArray, 0, size);
		}
		return newArray;
		
	}
	
	/**
	 * Processes every element of the collection using <code>process</code>
	 * method of the given instance of <code>Processor</code> class. Elements are
	 * processed from the beginning to the end of the collection.
	 *
	 * @param processor Processor which is used to process elements.
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]); //process each element
		}
	}
	
	/**
	 * Resizes the collection to the given capacity.
	 * @param targetCapacity new capacity of the collection
	 */
	private void resize(int targetCapacity) {
		
		Object[] temporaryArray = new Object[targetCapacity]; //new memory
		//copy to temporary array
		if (size >= 0) {
			System.arraycopy(elements, 0, temporaryArray, 0, size);
		}
		
		elements = temporaryArray;	//redefine reference to the inner array
	}
}
