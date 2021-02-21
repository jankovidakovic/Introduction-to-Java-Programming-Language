package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of doubly linked list-backed collection of objects.
 * Implements the <code>Collection</code>interface and defines all its
 * abstract methods.
 * It provides all methods that a standard linked list is expected to have.
 * Some methods throw exceptions for unsupported operations, and those 
 * exceptions are not declared in method declarations since none of them
 * are checked exceptions. Furthermore, all such exceptions are thoroughly
 * documented in the corresponding JavaDocs of the methods.
 * Supports access of content through <code>ElementsGetter</code> objects.
 * @author jankovidakovic
 *
 */
public class LinkedListIndexedCollection implements List {
	
	/**
	 * Represents one node of a double linked list.
	 * @author jankovidakovic
	 */
	private static class ListNode {
		
		//private variables
		private Object value;		//value of current node
		private ListNode previous;	//previous node in the list
		private ListNode next;		//next node in the list
		
		/**
		 * Constructs one list node and sets its value, previous node
		 * and next node to the given values.
		 * @param value given value of constructed node
		 * @param previous previous node in the list
		 * @param next next node in the list
		 */
		private ListNode(Object value, ListNode previous, ListNode next) {
			this.value = value;
			this.previous = previous;
			this.next = next;
		}
		
		//No need to write getters and setters since outer class can see
		//private members of the inner non-static class
		
	}
	
	//private variables
	private int size;
	private ListNode first;
	private ListNode last;
	private long modificationCount; //counts the modifications on the collection
	
	/**
	 * Constructs an empty list.
	 */
	public LinkedListIndexedCollection() {
		first = null;
		last = null;
		size = 0;		//empty
		modificationCount = 0; //not modified yet
	}
	
	/**
	 * Constructs a list filled with content of given collection.
	 * Order of elements in the constructed list is the same as in given collection.
	 * @param collection collection which elements are to be copied in the list.
	 */
	public LinkedListIndexedCollection(Collection collection) {
		this();
		if (collection.isEmpty() == false) { //if there are elements to be added
			addAll(collection);				
		}
		size += collection.size();	//increase size accordingly
	}
	
	
	/**
	 * Determines the size of the list.
	 * @return size of the list
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Adds given value as a node to the end of the list.
	 * Operation is performed in O(1) time complexity.
	 * If new node is succesfully added, then this operation counts as a
	 * modification of the collection, which effectively disables
	 * all <code>ElementsGetter</code> objects which were active for this
	 * instance of the list.
	 * @param value Value to be added in the list. Must not be <code>null</code>.
	 * @throws NullPointerException if given value is <code>null</code>.
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);	//throws NullPointerException if it's null
		//previous node of the added node is the last node of the list
		ListNode newNode = new ListNode(value, last, null);
		if (isEmpty()) {				//special case when list is empty
			first = last = newNode;
		} else {
			last.next = newNode;		//now points to new node
			last = newNode;				//new node becomes last in the list
		}
		size++;							//added 1 node
		modificationCount++;	//list was modified
	}
	
	/**
	 * Determines whether list contains a node with given value.
	 * @param value Value which is being searched for. Valid are all
	 * 		<code>Object</code> instances, even <code>null</code>.
	 * @return <code>true</code> it list contains a node with given value, 
	 * 			<code>false</code> otherwise.
	 */
	@Override
	public boolean contains(Object value) {
		for(ListNode it = first; it != null; it = it.next) { //iterate over the list
			if (it.value.equals(value)) {	//found first occurrence
				return true; //no need to go further
			}
		}
		return false;	//given value was not found 
	}
	
	/**
	 * Removes first occurence of the node with the given value.
	 * Occurence is determined by the <code>equals</code> method.
	 * If a node is removed, it counts as a modification of the collection,
	 * which means that all <code>ElementsGetter</code> objects which were
	 * active on this collection become effectively useless.
	 * @param value Value of node to be removed. Valid are all 
	 * 		<code>Object</code> instances.
	 * @return <code>true</code> if list contains node with given value
	 * (and consequently, it is removed), <code>false</code> otherwise.
	 */
	@Override
	public boolean remove(Object value) {
		for(ListNode it = first; it != null; it = it.next) { //iterate over the list
			if (it.value.equals(value)) {	//this node should be removed
				
				if (size == 1) {//special case, list contains only 1 node
					first = last = null;	//list becomes empty
					
				} else if (it == first) {	//special case of first node
					first = it.next;		//second element becomes the first one
					first.previous = null;	//so it doesn't point to removed node
					
				} else if (it == last) {	//special case of last node
					last = last.previous;   //second-last becomes last
					last.next = null;		//so it doesn't point to removed node
					
				} else {//general case
					it.previous.next = it.next; //bypassing node in one direction
					it.next.previous = it.previous;	//other direction
				}
				size--;		//one element was removed
				modificationCount++; //collection was modified
				return true;
			}
		}
		return false; //no node with given value was found.
	}
	
	
	/**
	 * Removes the node at the given index. If node is removed, it counts
	 * as a modification of the list, which means that all active instances
	 * of <code>ElementsGetter</code> will throw an exception when used
	 * again.
	 * @param index Index of node which is to be removed. Valid indices are
	 * 		all non-negative integers smaller than the number of elements
	 * 		stored in the list.
	 * @throws IndexOutOfBoundsException If invalid index was given.
	 */
	@Override
	public void remove(int index) {
		if (index < 0 || index > size() - 1) { //index our of range
			throw new IndexOutOfBoundsException("No element with such index.");
		}
		ListNode it = first;
		for (int i = 0; i < index; i++, it = it.next); //find node at given index
		ListNode nextNode = it.next; //next node
		ListNode previousNode = it.previous; //previous node
		if (it == last) { //no nextNode - last node is being removed
			if (previousNode == null) { //there is only one node in the list
				first = last = null;	//list becomes empty
			} else { //there is more than one node in the list
				previousNode.next = null; 
				last = previousNode; //previous node becomes the last one
			}
		} else if (previousNode == null) { //first node is being removed
			nextNode.previous = null;
			first = nextNode;	//next node becomes the first one
		} else { //node is removed from somewhere in the middle of the list
			previousNode.next = nextNode;	
			nextNode.previous = previousNode;	//bypass the removed node
		}
		it = null; //defererence the node so it is eligible for garbage
	}
	
	/**
	 * Returns content of the list in form of an array.
	 * Order of the elements in the array is the same as in the list.
	 * This operation is supported by this collection, so this method
	 * will not throw <code>UnsupportedOperationException</code>.
	 * @return array filled with content of the list. 
	 */
	@Override
	public Object[] toArray() {
		Object[] newArray = new Object[size];	//allocating memory
		ListNode it;	//iterator node
		int i;			//index
		for (it = first, i = 0; it != null; it = it.next, i++) {
			newArray[i] = it.value; //copy all references to the array
		}
		return newArray;
	}
	
	/**
	 * Removes all nodes from the list. Makes all nodes eligible for 
	 * garbage collection. This method counts as a modification of the 
	 * collection, which means that all <code>ElementsGetter</code> objects
	 * which were operating on that instance of the collection, become
	 * effectively useless.
	 */
	@Override
	public void clear() {
		for (ListNode it = first; it != null; it = it.next) {
			it.previous = null;	//destroy the previous element
			if (it == last) {
				it = null;		//destroy last element
				break;	//the end
			}
		}
		size = 0;	//no more elements
		modificationCount++; //collection was modified
	}
	
	/**
	 * Retrieves value of the node with a given index. Index 0 corresponds to
	 * the first node, and index size-1 corresponds to the last node. 
	 * Method is optimized to perform at most O(n/2) operations, where
	 * n is number of elements in the list.
	 * @param index Index of node which value is to be returned. Valid 
		indices are all non-negative integers smaller than the size of the list.
	 * @return Value of node with a given index.
	 * @throws IndexOutOfBoundsException If given index is invalid.
	 */
	public Object get(int index) {
		if (index < 0 || index > size-1) {	//invalid index
			throw new IndexOutOfBoundsException("invalid index");
		}
		
		int delta;	//helper variable for direction of traversing
		ListNode it;	//starting point of list traversal
		if (index < size/2) {	//more efficient to start at first
			it = first;
			delta = 1;	//forward traversal
		} else {	//more efficient to start at last
			it = last;
			delta = -1;	//backward traversal
		}
		
		int i  = (delta == 1 ? 0 : size-1); //first or last, efficiently chosen
		for(; i != index; it = (delta == 1 ? it.next : it.previous), i+=delta)
			;	//traversing until given index is reached
		
		return it.value;	//it points to node at given index
	}
	
	/**
	 * Creates new node with a given value and inserts it into the list at the 
	 * given position. First node corresponds to the position 0, and 
	 * last node corresponds to the position size. If invalid position
	 * is given, appropriate exception is thrown. If insertion was performed
	 * succesfully, it counts as a modification of the collection, which means
	 * that all <code>ElementsGetter</code> objects which were active on this
	 * instance of the collection become effectively useless.
	 * @param value Value of node that is to be inserted. Valid if not
	 * 		<code>null</code>.
	 * @param position Position at which the node will be inserted. Valid positions
	 * 		are from 0 to size (both inclusive).
	 * @throws IndexOutOfBoundsException if invalid position is specified.
	 * @throws IllegalArgumentException if given value is <code>null</code>.
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException("Null elements are not allowed.");
		}
		if (position < 0 || position > size) { //invalid position
			throw new IndexOutOfBoundsException("Invalid insert position");
		}
		
		//previous and next element are not known yet
		ListNode newNode = new ListNode(value, null, null); //new node to insert
		
		if (isEmpty()) {	//special case, list is empty
			first = last = newNode;	//previous and next can stay null
		} else {
			ListNode it;	//for traversing, will point to node at given position
			int i;			//position finder
			for (i = 0, it = first; i < position; it = it.next, i++)
				;	//find node at given position
			
			if (i == 0) { 			//needs to be inserted at the beginning
				//next node is the old first node
				newNode.next = first;
				//previous node of old first node is the newly inserted node
				first.previous = newNode;
				first = newNode;	//make new node the new first node
			} else if (i == size) {	//needs to be inserted at the end
				//previous node of new node is the old last node
				newNode.previous = last;
				//next node of old last node is the newly inserted node
				last.next = newNode;
				last = newNode;	//make new node the last node
			} else {				//general case, somewhere in the middle
				//previous node of new node should be it.previous
				it.previous.next = newNode;
				newNode.previous = it.previous;
				//next node of new node should be it
				it.previous = newNode;	
				newNode.next = it;
				
			}
		}
		size++;
		modificationCount++; //collection was modified
	}
	
	
	/**
	 * Determines the index of the first occurence of node with a given value.
	 * First node of the list has index 0, and last node has index size-1.
	 * @param value Value which is to be fouund in the list.
	 * @return index of first found node which contains a given value. If no such
	 * 			node is found, returns <code>-1</code>
	 */
	public int indexOf(Object value) {
		ListNode it;	//helper node for list traverstal
		int i;			//will become index of first node with a given value
		for(it = first, i = 0; !(it == null || it.value.equals(value)); 
			it = it.next, i++)
				;
		if (it == null) {	//end of the list, no node with given value was found
			return -1;
		} else {			//node was found, index is i
			return i;
		}
	}
	
	/**
	 * /**
	 * Implementation of collection's <code>ElementsGetter</code>, which can
	 * be used for accessing elements of the collection, using its
	 * <code>hasNextElement</code> and <code>getNextElement</code> methods.
	 * Instances of <code>ElementsGetter</code> retrieve elements from first
	 * to last, and cannot retrieve the same element twice (similar to an
	 * iterator). Multiple <code>ElementsGetter</code> instances can be created
	 * for the same instance of the list, and their behavior will be 
	 * indepentend of each other. Active instances of <code>ElementsGetter</code> 
	 * don't support structural modification of the collection which they refer 
	 * to(such as inserting or deleting nodes from the list) and will throw an
	 * expression if attempted to be used after such modification has been made.
	 * @author jankovidakovic
	 *
	 */
	private class LinkedListElementsGetter implements ElementsGetter {
		
		//private variables
		private final long savedModificationCount; //tracking the modifications
		private ListNode nextElement;	//next retrievable element
		
		/**
		 * Default constructor. Sets up the <code>ElementsGetter</code>
		 * for retrieval of the first node of the list, and remembers
		 * the current modication count of the list, so it can track
		 * any future unsupported modifications.
		 */
		private LinkedListElementsGetter() {
			nextElement = first;	//first retrievable element
			savedModificationCount = modificationCount;	//to track changes
		}
		
		/**
		 * Checks wherher list contains any elements that have not
		 * been retrieved yet. Guaranteed to be idempotent, in other words,
		 * every concurrent call of this method shold return the same value,
		 * if <code>getNextElement</code> has not been called in the meantime.
		 * @throws ConcurrentModificationException if list which this
		 * instance of <code>ElementsGetter</code> refers to has been modified
		 * in a structural way (nodes were inserted or removed).
		 * @return <code>true</code> if there are more elements to be retrieved,
		 * <code>false</code> otherwise.
		 */
		@Override
		public boolean hasNextElement() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException(
						"Collection was modified.");
			}
			return nextElement != null;
		}
		
		/**
		 * Retrieves the value of the next node in the list. If there are no
		 * more elements that this instance of <code>ElementsGetter</code> can
		 * retrieve(as determined by <code>hasNextElement</code> method), 
		 * appropriate exception is thrown.
		 * @throws NoSuchElementException if there are no more elements that 
		 * can be retrieved(<code>hasNextElement</code> returned <code>false</code>).
		 * @throws ConcurrentModificationException if list which this instance
		 * of <code>ElementsGetter</code> refers to has been modified in a 
		 * structural way(nodes were inserted or removed).
		 * @return value of next node of the collection.
		 * 
		 */
		@Override
		public Object getNextElement() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException(
						"Collection was modified."); //unsupported
			}
			
			if (!hasNextElement()) { //all values have been retrieved
				throw new NoSuchElementException("No more elements.");
			}
			
			Object value = nextElement.value;
			nextElement = nextElement.next;
			return value;
		}
	}
	
	/**
	 * Creates a new instance of <code>ElementsGetter</code> for this list.
	 * @return new instance of <code>ElementsGetter</code>.
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new LinkedListElementsGetter();
	}
	
}
