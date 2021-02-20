package hr.fer.zemris.java.custom.collections;

/**
 * Implementation of standard stack container, which can contain any Object.
 * Implementation is done using an Adapter Pattern. Adapter class is 
 * ArrayIndexedCollection, which is implemented as a dynamic random access
 * container.
 * Provides the full functionality of standard stack container.
 * Methods deal with unsupported operations (such as popping from an empty stack)
 * by throwing an instance of custom made EmptyStackException, which implementation
 * and documentation can be found in the same package. Methods do not declare the
 * exception with <code>throws</code> keyword, because it is not a checked exception.
 * Stack can contain duplicate elements, and cannot contain null elements.
 *
 * @author jankovidakovic
 * @param <E> type of elements stored in the stack.
 */
public class ObjectStack<E> {
	
	//private variables
	private final ArrayIndexedCollection<E> elements; //inner container
	
	/**
	 * Constructs an empty stack
	 */
	public ObjectStack() {
		elements = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Constructs a stack with given capacity
	 *
	 * @param initialCapacity Wanted capacity of the stack. Valid if natural number.
	 * @throws IllegalArgumentException if given capacity is invalid
	 */
	public ObjectStack(int initialCapacity) {
		elements = new ArrayIndexedCollection<>(initialCapacity); //throws exception if
																//capacity is invalid
	}
	
	//it makes no sense to adapt the constructors of ArrayIndexedCollection
	//which take a collection as an argument, since we would almost never
	//want to create a stack from some random access collection in that way.
	
	/**
	 * Determines whether or not does the stack contain any elements.
	 *
	 * @return <code>true</code>, if stack contains no elements, 
	 * 			<code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	/**
	 * Determines the number of elements currently stored in the stack.
	 *
	 * @return Number of elements in the stack.
	 */
	public int size() {
		return elements.size();
	}
	
	/**
	 * Pushes the given value to the top of the stack.
	 *
	 * @param value Value to be pushed. Valid if not <code>null</code>.
	 * @throws NullPointerException if given value is invalid.
	 */
	public void push(E value) {
		elements.add(value);	//throws NullPointerException for null value
	}
	
	/**
	 * Removes the top element from the stack and retrieves its value.
	 * Valid to call if stack is not empty.
	 *
	 * @return Value of the top element (the one being removed).
	 * @throws EmptyStackException If the stack is empty.
	 */
	public E pop() {
		E topElement = peek();	//throws NullPointerException for empty stack
		elements.remove(elements.size() - 1); //remove top of the stack
		return topElement;
	}
	
	/**
	 * Retrieves value of the top element of the stack. Top element is NOT removed.
	 * Valid to call if stack is not empty.
	 *
	 * @return Value of top element of the stack.
	 * @throws EmptyStackException If the stack is empty
	 */
	public E peek() {
		if (elements.isEmpty()) {
			throw new EmptyStackException("Error: stack is empty");
		}
		
		//top element of the stack corresponds to last element of inner array
		return elements.get(elements.size() - 1);
		
	}
	
	/**
	 * Removes all elements from the stack.
	 */
	void clear() {
		elements.clear();
	}
}
