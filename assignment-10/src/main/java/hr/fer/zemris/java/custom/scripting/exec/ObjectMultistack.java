package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Map-like collection of object, capable of storing multiple values for each
 * key and providing stack-like abstraction for each key. Keys are instances of
 * <code>String</code>, and values are instances of <code>ValueWrapper</code>.
 * 
 * @author jankovidakovic
 *
 */
public class ObjectMultistack {

	/**
	 * Helper class that acts as a node of a single-linked list.
	 * 
	 * @author jankovidakovic
	 *
	 */
	private static class MultistackEntry {
		private ValueWrapper value; // value stored at the node
		private MultistackEntry next; // next node of the list
		
		/**
		 * Constructs a new multistack entry with given value, setting the next
		 * node to the given one.
		 * 
		 * @param value value of the multistack entry
		 * @param next  next node in the linked list
		 */
		private MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
	}
	
	// first node is top of the stack, for O(1) operations
	private Map<String, MultistackEntry> map;

	/**
	 * Constructs an empty object multistack.
	 */
	public ObjectMultistack() {
		map = new HashMap<String, MultistackEntry>();
	}

	/**
	 * Pushes the given value to the stack stored at the given key.
	 * 
	 * @param keyName      key at which the value will be pushed
	 * @param valueWrapper value to be pushed
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		MultistackEntry newEntry =
				new MultistackEntry(valueWrapper, map.get(keyName));
		map.put(keyName, newEntry);

	}

	/**
	 * Removes the top of the stack stored at the given key, and returns the
	 * removed value.
	 * 
	 * @param  keyName             key at which the value will be popped
	 * @return                     popped value
	 * @throws EmptyStackException if stack at given key contains no values.
	 */
	public ValueWrapper pop(String keyName) {
		MultistackEntry entry = map.get(keyName);
		if (entry == null) { // empty stack
			throw new EmptyStackException();
		} else {
			map.put(keyName, map.get(keyName).next);
			return entry.value;
		}
	}

	/**
	 * Returns the value at the top of the stack stored at the given key, but
	 * does not remove the value.
	 * 
	 * @param  keyName             key at which the top of the stack will be
	 *                             peeked
	 * @return                     top of the stack at given key
	 * @throws EmptyStackException if stack at given key is empty
	 */
	public ValueWrapper peek(String keyName) {
		MultistackEntry entry = map.get(keyName);
		if (entry == null) { // empty stack
			throw new EmptyStackException();
		} else {
			return entry.value;
		}
	}

	/**
	 * Checks whether stack at the given key stores any values.
	 * 
	 * @param  keyName key at which the checking is done
	 * @return         <code>true</code> if stack at the given key contains no
	 *                 values, <code>false</code> otherwise.
	 */
	public boolean isEmpty(String keyName) {
		return map.get(keyName) != null;
	}

}
