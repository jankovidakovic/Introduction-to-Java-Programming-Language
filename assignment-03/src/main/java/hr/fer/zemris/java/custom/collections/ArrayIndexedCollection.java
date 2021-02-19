package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Implementation of resizeable array-backed collection of objects.
 * Implements the <code>Collection</code> interface, and defines
 * all its abstract methods.
 * Some methods are implemented to throw exceptions on unsupported operations,
 * but this exceptions are not declared in method declaration, since none
 * of them are checked exceptions. Furthermore, all exceptions are thoroughly
 * documented in corresponding JavaDocs of the methods.
 * Duplicate elements are allowed, storage of <code>null</code> references
 * is not allowed.
 * Supports access of content using <code>ElementsGetter</code> objects.
 *
 * @author jankovidakovic
 */
public class ArrayIndexedCollection implements List {

	//private variables
	private int size; 		//number of elements stored in the collection
	Object[] elements;		//array of object references
	private long modificationCount; //tracks the modifications of the collection
	
	/**
	 * Constructs an empty collection with capacity to store 16 elements.
	 */
	public ArrayIndexedCollection() {
		super();							//constructor of parent class
		final int capacity = 16;			//initial capacity
		elements = new Object[capacity];	//allocation of memory
		size = 0;	//contains no actual elements yet
		modificationCount = 0;	//freshly initialized
	}
	
	/**
	 * Constructs empty collection with the given initial capacity.
	 *
	 * @param initialCapacity initial capacity of the collection.
	 * 			Valid capacity is a positive whole number.
	 * @throws IllegalArgumentException if given capacity is invalid.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) { 		//capacity can't be smaller than 1
			throw new IllegalArgumentException("Invalid capacity.");
		} else {
			elements = new Object[initialCapacity];	//memory allocation
			size = 0;							//contains no elements yet
			modificationCount = 0;	//hasn't been modified yet
		}
	}
	
	/**
	 * Constructs a non-empty collection which contains copies of elements
	 * of the given collection.
	 *
	 * @param otherCollection collection which elements are to be copied.
	 * 			Valid if not <code>null</code> and has positive capacity.
	 * e.g. <code>new ArrayIndexedCollection(new ArrayIndexedCollection(0))</code> 
	 * 		is not allowed and will cause an exception.
	 * @throws IllegalArgumentException if given collection has no capacity, which
	 * 			can happen in the example above.
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
	 * If new value is added successfully, then the calling of this
	 * method counts as a modification of the instance collection on which it
	 * has been called. That means that all <code>ElementsGetter</code> objects
	 * which are active for this instance of the collection, become useless after
	 * this method has been successfully executed.
	 * Works in amortised O(1).
	 *
	 * @param value object which is to be added into the collection. Must not be
	 * 			<code>null</code>.
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
			modificationCount++;		//collection has been modified
		}
	}
	
	/**
	 * Returns element that is stored in collection at position index.
	 * Works in O(1) time complexity.
	 *
	 * @param index position of element that is to be returned.
	 * 		Valid indexes are all non-negative integers smaller than the size
	 * 		of the collection.
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
	 * This method counts as a modification of the collection, which means 
	 * that all <code>ElementsGetter</code> objects which work with the
	 * instance of the collection that this method has been called on, will
	 * become useless.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;		// so that the memory can be cleaned
		}
		size = 0;	//collection contains no more elements
		modificationCount++;
	}
	
	/**
	 * Inserts given element to the collection at given position.
	 * Does not overwrite existing elements, elements with greater index are
	 * shifted one place towards the end. If collection is at full capacity, it
	 * will be reallocated and its capacity will double.
	 * Successful insertion of new value counts as a modification of the instance
	 * of the collection, and renders all active <code>ElementsGetter</code> objects
	 * (which work with that instance) useless.
	 *
	 * @param value object which is to be inserted 
	 * @param position position at which the given object will be inserted.
	 * 		Valid positions are non-negative integers which are not greater than
	 * 		the size of the collection(maximum valid position is equal to the
	 * 		size of the collection, in which case element will be inserted as
	 * 		last element of the collection).
	 * @throws IndexOutOfBoundsException for invalid index
	 */
	public void insert(Object value, int position) {
		if (position < 0 || position > size) { //invalid position
			throw new IndexOutOfBoundsException("Invalid insert position");
		} else {
			if (size+1 >= elements.length) {//capacity is not enough for new element
				resize(2 * elements.length);	//doubles the capacity
			}
			if (size - position >= 0) { //shift elements at the greater index one place to the right
				System.arraycopy(elements, position, elements, position + 1, size - position);
			}
			elements[position] = value;		//inserting the given value			
			size++;							//increasing size
			modificationCount++;			//collection has been modified
		}
	}
	
	/**
	 * Searches collection and finds the first occursence of the given element.
	 * Given element can be <code>null</code>, but it is guaranteed that collection
	 * does not contain such element.
	 * Equality is determined using the <code>equals</code> method.
	 * Average time complexity is O(n), where n is the number of elements stored(size).
	 *
	 * @param value element to be searched for
	 * @return index of first occurrence of the given element, or -1 if collection
	 * 			does not contain the element.
	 */
	public int indexOf(Object value) {
		int indexOfFirstOccurrence = -1; //return value, initialized for null
		
		if (value != null) {			//null is guaranteed not to be found
			for (int i = 0; i < size; i++) {//finding first occurrence
				if (elements[i].equals(value)) {
					indexOfFirstOccurrence = i;	//remember the index
					break;						//found it
				}
			}
		}
		return indexOfFirstOccurrence;
	}
	
	/**
	 * Removes element at given index. Elements with indexes greater than given 
	 * index are shifted one place towards the beginning of the collection.
	 * Successful removal of element counts as a modification, and disables
	 * all active <code>ElementsGetter</code> objects that work with that 
	 * instance of collection.
	 * @param index position of element which is to be removed. Valid indices 
	 * 		are non-negative integers smaller than the size of the collection.
	 * @throws IndexOutOfBoundsException if invalid index is given
	 */
	public void remove(int index) {
		if (index < 0 || index > size-1) { 			//invalid index
			throw new IndexOutOfBoundsException("Invalid index.");
		} else {
			//shift elements to the left
			if (size - 1 - index >= 0) {
				System.arraycopy(elements, index + 1, elements, index, size - 1 - index);
			}
			elements[size-1] = null;				//for garbage collection
			size--;
			modificationCount++;	//collection has been modified
		}
	}
	
	/**
	 * Checks whether collection contains any elements.
	 *
	 * @return <code>true</code> if collection is empty, 
	 * 			<code>false</code> otherwise
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
	 * It's allowed to theck whether the collection contains <code>null</code>, 
	 * but it is guaranteed that the result of that operation will be 
	 * <code>false</code>.
	 *
	 * @param value object which the colleciton is being searched for. Valid 
	 * 		are all <code>Object</code> instances, including <code>null</code>.
	 * @return <code>true</code> if collection contains given object, 
	 * 			<code>false</code> otherwise.
	 */
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Removes first occurrence of a given object from the collection.
	 * All elements with index greater than the one of removed element are
	 * shifted one place towards the beginning. Occurrence of given object
	 * is determined by <code>equals</code> method.
	 * Successful removal of object counts as a modification, and consequently
	 * disables all active <code>ElementsGetter</code> objects which worked
	 * with that instance of the collection.
	 *
	 * @param value object to be removed. Valid are all <code>Object</code> 
	 * 				instances.
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
							//also modifies modificationCount
			return true;
		}
	}
	
	/**
	 * Allocates new array, fills it with content of the collection,
	 * and returns the new array. The order of the elements in the
	 * resulting array is the same as the order in the collection.
	 * This collection supports this operation, so it will not throw
	 * <code>UnsupportedOperationException</code>
	 *
	 * @return array filled with elements of the collection. Guaranteed to
	 * 			never be <code>null</code> (throws exception instead)
	 */
	@Override
	public Object[] toArray() {
		Object[] newArray = new Object[size];	//allocate the new array
		//copying all elements to the new array
		if (size >= 0) {
			System.arraycopy(elements, 0, newArray, 0, size);
		}
		return newArray;
		
	}
	
	/**
	 * Resizes the collection to the given capacity.
	 *
	 * @param targetCapacity new capacity of the collection
	 */
	private void resize(int targetCapacity) {
		
		Object[] temporaryArray = new Object[targetCapacity]; //new memory
		//copy to temporary array
		if (size >= 0) {
			System.arraycopy(elements, 0, temporaryArray, 0, size);
		}
		
		elements = temporaryArray;	//redefine reference to the inner array
		modificationCount++; //because of the reallocation
	}
	
	
	/**
	 * Implementation of collection's <code>ElementsGetter</code>, which can
	 * be used for accessing elements of the collection, using its
	 * <code>hasNextElement</code> and <code>getNextElement</code> methods.
	 * Instances of <code>ElementsGetter</code> retrieve elements from first
	 * to last, and cannot retrieve the same element twice (similar to an
	 * iterator). Multiple <code>ElementsGetter</code> instances can be created
	 * for the same instance of the collection, and their behavior will be 
	 * independent of each other. Active instances of <code>ElementsGetter</code>
	 * don't support concurrent modification of the collection which they refer 
	 * to, and will throw exceptions if used after such modification was made.
	 *
	 * @author jankovidakovic
	 */
	private class ArrayElementsGetter implements ElementsGetter {
		

		//private variables
		private int nextElementIndex; //element at this index is retrieved
		private final long savedModificationCount; //so it doesn't allow concurrent
													//modification
		
		/**
		 * Constructor which points the <code>ElementsGetter</code> to the 
		 * first element of the collection. Also it remembers the modification
		 * count of the collection, and if it increases while this instance of
		 * elements getter is active, then any further usage of that instance will
		 * throw an exception.
		 */
		private ArrayElementsGetter() {
			nextElementIndex = 0;	//first element of the collection
			savedModificationCount = modificationCount; //remember current count
		}
		
		/**
		 * Checks whether collection contains any elements that have not
		 * been retrieved yet. Guaranteed to be idempotent, in other words,
		 * every concurrent call of this method should return the same value,
		 * if the <code>getNextElement</code> has not been called in between.
		 *
		 * @throws ConcurrentModificationException if collection which this
		 * instance of <code>ElementsGetter</code> refers to has been modified
		 * in a structural way (inner array was reallocated, or elements were
		 * inserted or deleted).
		 * @return <code>true</code> if there are more elements to be retrieved,
		 * <code>false</code> otherwise.
		 */
		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != modificationCount) { //was modified
				throw new ConcurrentModificationException(
						"Collection was modified."); //unsupported
			}
			return nextElementIndex < size;
		}
		
		/**
		 * Retrieves the next element of the collection. If there are no
		 * more elements that this instance of <code>ElementsGetter</code> can
		 * retrieve(as determined by <code>hasNextElement</code> method), 
		 * appropriate exception is thrown.
		 *
		 * @throws NoSuchElementException if there are no more elements that 
		 * can be retrieved(<code>hasNextElement</code> returned <code>false</code>).
		 * @throws ConcurrentModificationException if collection which this instance
		 * of <code>ElementsGetter</code> refers to has been modified in a 
		 * structural way(inner array was reallocated, or elements were inserted or
		 * deleted).
		 * @return first element which has not been retrieved yet.
		 * 
		 */
		@Override
		public Object getNextElement() {
			if (savedModificationCount != modificationCount) { //modified
				throw new ConcurrentModificationException(
						"Collection was modified."); //unsupported
			}
			if (hasNextElement()) {
				return elements[nextElementIndex++]; 
			} else { //no more elements
				throw new NoSuchElementException("No more elements.");
			}
		}
	}
	
	/**
	 * Creates a new instance of <code>ElementsGetter</code>.
	 *
	 * @return new instance of <code>ElementsGetter</code>.
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new ArrayElementsGetter();
	}
	
}
