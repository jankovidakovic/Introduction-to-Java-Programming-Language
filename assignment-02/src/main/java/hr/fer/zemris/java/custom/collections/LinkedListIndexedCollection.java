package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Implementation of doubly linked list-backed collection of objects.
 * It provides all methods that a standard linked list is expected to have.
 * Some methods throw exceptions for unsupported operations, and those 
 * exceptions are not declared in method declarations since none of them
 * are checked exceptions. Furthermore, all such exceptions are thoroughly
 * documented in the corresponding JavaDocs of the methods.
 *
 * @author jankovidakovic
 *
 */
public class LinkedListIndexedCollection extends Collection {
	
	/**
	 * Represents one node of a double linked list.
	 * 
	 */
	private static class ListNode {
		
		//private variables
		private final Object value;		//value of current node
		private ListNode previous;	//previous node in the list
		private ListNode next;		//next node in the list
		
		/**
		 * Constructs one list node and sets its value, previous node
		 * and next node to the given values.
		 *
		 * @param value given value of constructed node
		 * @param previous previous node in the list
		 * @param next next node in the list
		 */
		private ListNode(Object value, ListNode previous, ListNode next) {
			this.value = value;
			this.previous = previous;
			this.next = next;
		}
		
		//No need to write getters and setters since parent class can see
		//private members of the inner class
		
	}
	
	//private variables
	private int size;
	private ListNode first;
	private ListNode last;
	
	/**
	 * Constructs an empty list.
	 */
	public LinkedListIndexedCollection() {
		first = null;
		last = null;
		size = 0;		//empty
	}
	
	/**
	 * Constructs a list filled with content of given collection.
	 * Order of elements in the constructed list is the same as in given collection.
	 *
	 * @param collection collection which elements are to be copied in the list.
	 */
	public LinkedListIndexedCollection(Collection collection) {
		this();
		if (!collection.isEmpty()) { //if there are elements to be added
			addAll(collection);				
		}
		size += collection.size();	//increase size accordingly
	}
	
	
	/**
	 * Determines the size of the list.
	 *
	 * @return size of the list
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Adds given value as a node to the end of the list.
	 * Operation is performed in O(1) time complexity.
	 *
	 * @param value Value to be added in the list. Must not be <code>null</code>.
	 * @throws NullPointerException if given value is <code>null</code>
	 */
	@Override
	public void add(Object value) throws NullPointerException {
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
	}
	
	/**
	 * Determines whether list contains a node with given value.
	 *
	 * @param value Value which is being searched for. Can be <code>null</code>.
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
	 * Removes first occurrence of the node with the given value.
	 * @param value Value of node to be removed
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
				return true;
			}
		}
		return false; //whole list was traversed, no node with given value was found
	}
	
	/**
	 * Returns content of the list in form of an array.
	 * Order of the elements in the array is the same as in the list.
	 *
	 * @throws UnsupportedOperationException if array can not be initialized.
	 * @return array filled with content of the list. Never <code>null</code>
	 */
	@Override
	public Object[] toArray() throws UnsupportedOperationException {
		Object[] newArray = new Object[size];	//allocating memory
		try {
			Objects.requireNonNull(newArray, "Must not be empty array");
		} catch (NullPointerException ex) {	//couldn't allocate memory
			throw new UnsupportedOperationException(ex.getMessage());
		}
		ListNode it;	//iterator node
		int i;			//index
		for (it = first, i = 0; it != null; it = it.next, i++) {
			newArray[i] = it.value; //copy all references to the array
		}
		return newArray;
	}
	
	/**
	 * Processes each node of the list(from first to last) using
	 * the <code>process</code> method of a given instance of 
	 * <code>Processor</code> class.
	 * @param processor Processor which is used to process nodes.
	 */
	@Override
	public void forEach(Processor processor) {
		for (ListNode it = first; it != null; it = it.next) {
			processor.process(it.value);	//process each node
		}
	}
	
	/**
	 * Removes all nodes from the list. Makes all nodes eligible for 
	 * garbage collection.
	 */
	@Override
	public void clear() {
		for (ListNode it = first; it != null; it = it.next) {
			it.previous = null;	//destroy backwards
			if (it == last) {
				it = null;		//destroy last element
				break;	//the end
			}
		}
		size = 0;
	}
	
	/**
	 * Retrieves value of the node with a given index. Index 0 corresponds to
	 * the first node, and index size-1 corresponds to the last node.
	 *
	 * @param index Index of node which value is to be returned. Valid indexes are
	 * 		from 0 to size-1 inclusive.
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
		for (; i != index; it = (delta == 1 ? it.next : it.previous), i+=delta)
			;	//traversing until given index is reached
		
		return it.value;	//it points to node at given index
	}
	
	/**
	 * Creates new node with a given value and inserts it into the list at the 
	 * given position. First node corresponds to the position 0, and 
	 * last node corresponds to the position size. If invalid position
	 * is given, appropriate exception is thrown.
	 * @param value Value of node that is to be inserted.
	 * @param position Position at which the node will be inserted. Valid positions
	 * 		are from 0 to size (both inclusive).
	 * @throws IndexOutOfBoundsException if invalid position is specified.
	 */
	public void insert(Object value, int position) {	
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
	}
	
	
	/**
	 * Determines the index of the first occurrence of node with a given value.
	 * First node of the list has index 0, and last node has index size-1.
	 * @param value Value which is to be found in the list.
	 * @return index of first found node which contains a given value. If no such
	 * 			node is found, returns <code>-1</code>
	 */
	public int indexOf(Object value) {
		ListNode it;	//helper node for list traversal
		int i;			//will become index of first node with a given value

		for (it = first, i = 0; !(it == null || it.value.equals(value)); it = it.next, i++)
			;

		if (it == null) {	//end of the list, no node with given value was found
			return -1;
		} else {			//node was found, index is i
			return i;
		}
	}
}
