package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Implementation of a dictionary data structure, which stores values that
 * can be retrieved using their respective keys. Elements of the dictionary
 * are entries which consist of entry's key and the value that corresponds
 * to the key. The implementation is not very efficient but it is very
 * simple. It uses <code>ArrayIndexedCollection</code> as inner container.
 *
 * @author jankovidakovic
 * @param <K> key type
 * @param <V>  value type
 */
public class Dictionary<K, V> {
	
	//private variables
	private final ArrayIndexedCollection<Entry> values; //inner container
	
	/**
	 * Entry of the map, represented as a pair of key and value.
	 *
	 * @author jankovidakovic
	 */
	private class Entry {
		
		private final K key;	//key by which the entry can be retrieved
		private final V value;//value of the entry

		/**
		 * Constructs an entry with given key and value
		 *
		 * @param key key of the entry. Must not be <code>null</code>.
		 * @param value value of the entry. Can be <code>null</code>.
		 * @throws NullPointerException if given key is <code>null</code>.
		 */
		private Entry(K key, V value) {
			Objects.requireNonNull(key); //key must not be null
			this.key = key;
			this.value = value;
		}
	}
	
	/**
	 * Constructs an empty dictionary.
	 */
	public Dictionary() {
		values = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Determines the number of currently stored entries in the dictionary
	 *
	 * @return size of the dictionary.
	 */
	public int size() {
		return values.size();
	}
	
	/**
	 * Deletes all entries from the dictionary, leaving it with size equal to 0.
	 */
	public void clear() {
		values.clear();
	}
	
	/**
	 * Inserts a new entry in the dictionary. If entry with given key already
	 * exists, its value will be updated and overwritten.
	 *
	 * @param key key of the new entry. If it exists in the dictionary,
	 * 		existing one is overwritten.
	 * @param value value of the new entry.
	 */
	public void put(K key, V value) {
		int entryIndex = findKey(key);
		if (entryIndex != -1) {	//given key exists
			values.remove(entryIndex);	//delete the entry
		}
		values.add(new Entry(key, value));	//insert new entry
	}
	
	/**
	 * Determines whether the map contains an entry with given key. If so,
	 * returns the index at which the entry is stored in the inner array.
	 *
	 * @param key given key to be found
	 * @return index of given key if it is found within the inner array,
	 * 		-1 otherwise.
	 */
	private int findKey(Object key) {
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).key.equals(key)) { 
				return i; //key is found
			}
		}
		return -1;	//key is not found
 	}
	
	/**
	 * Finds the value that corresponds to the given key. If dictionary 
	 * contains no entry with given key, appropriate value is returned.
	 *
	 * @param key Key of the value that is being requested
	 * @return value that is stored at the given key in the dictionary.
	 * 		If entry with given key doesn't exist, returns <code>null</code>
	 */
	public V get(Object key) {
		int entryIndex = findKey(key);
		if (entryIndex == -1) { //there is no such entry
			return null;
		} else { //en entry with given key was found - return its value
			return values.get(entryIndex).value;
		}
	}
}
