package hr.fer.zemris.java.custom.collections;

import java.util.*;

/**
 * Implementation of simple hash table. Stores instances of nested class
 * <code>TableEntry<K,V></code>. Each instance is a table "entry" that
 * consists of its value and key under which it is stored. Entries are
 * retrieved using their respective keys. Overflowing of the hash table
 * is solved using chaining. Each slot in table is a linked list, and if
 * two keys have the same hash they will be placed in the same list.
 * For efficiency purposes, it is guaranteed that the ratio of stored
 * entries to table slots will never exceed 0.75. Inner table is therefore
 * resized dynamically when needed. Hash table is iterable, and provides
 * method factory for creating iterators.
 *
 * @author jankovidakovic
 * @param <K> type of key
 * @param <V> type of value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>>{

	//private variables
	private int size;	//number of entries stored
	private TableEntry<K,V>[] table;	//table of first nodes
	private long modificationCount; //for iterators
	
	/**
	 * Represents an entry in the hash table. Entries are stored as nodes
	 * of linked lists, because the hash table takes care of overflowing
	 * by chaining. Each entry contains key, value and reference to the
	 * next entry in the same table slot.
	 * @author jankovidakovic
	 *
	 * @param <K> Type of key
	 * @param <V> Type of value
	 */
	public static class TableEntry<K,V> {
		
		//private variables
		private final K key;	//key of the entry
		private V value;//value of the entry
		private TableEntry<K,V> next;	//next entry in the list
		
		/**
		 * Constructs a new entry with given key, value, and next entry
		 * in its corresponding list.
		 *
		 * @param key key of the table entry
		 * @param value value that the table entry holds
		 * @param next next table entry, as a next node in the linked list
		 */
		public TableEntry(K key, V value, TableEntry<K,V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Getter for key of the entry.
		 *
		 * @return Key of the entry.
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Getter for value of the entry.
		 *
		 * @return Value of the entry.
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Sets the value of the entry to the given one.
		 *
		 * @param value New value of the entry.
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Creates a string representation of the entry.
		 *
		 * @return string representation of the entry, in form of "key=value".
		 *
		 */
		@Override
		public String toString() {
			return key +
					"=" +
					value;
		}
		
		
	}
	
	/**
	 * Constructs an empty hash map with size of the inner table set to 16.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable() {
		size = 0;	//empty
		table = (TableEntry<K,V>[]) new TableEntry[16]; //inner hash table
		modificationCount = 0;	//no modifications yet
	}
	
	/**
	 * Constructs an empty hash map with capacity that is the smallest
	 * power of two which is greater than or equal to the given capacity.
	 * For example, if capacity 30 is given, map will be initialized with
	 * capacity of 32.
	 *
	 * @param capacity Wanted capacity. Must be a natural number.
	 * @throws IllegalArgumentException if given capacity is not a natural
	 * 		number (smaller than 1).
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) { //not valid
			throw new IllegalArgumentException("Invalid capacity.");
		}
		//find smallest power of two that is greater than or equal
		int targetCapacity = 1;
		while (capacity > 1) {
			capacity /= 2;
			targetCapacity *= 2;
		}
		table = (TableEntry<K,V>[]) new TableEntry[targetCapacity];
		size = 0;	//empty
		modificationCount = 0; //no modifications yet
	}
	
	/**
	 * Calculates the index at which the given key should be stored.
	 *
	 * @param key Key
	 * @param tableCapacity capacity of the table, used for calculating
	 * 		the index.
	 * @return index in the entry table
	 */
	private int calculateIndex(Object key, int tableCapacity) {
		return Math.abs(key.hashCode()) % tableCapacity;
	}
	
	/**
	 * Creates an entry with a given key and value and inserts it into the map.
	 * If entry with given key already exists, its value is overwritten with
	 * the new value. If a new entry is inserted, inner structure of the 
	 * hash map is modified in such a way that all active iterators become
	 * obsolete and throw <code>ConcurrentModificationException</code> 
	 * upon further use.
	 *
	 * @param key Key which will be associated with the value. Must not be null.
	 * @param value Value to be inserted into the map. Can be null.
	 * @throws NullPointerException if given key is null.
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key); //key must not be null
		int entryIndex = calculateIndex(key, table.length);	//inner table index
		if (table[entryIndex] == null) {	//slot is empty
			table[entryIndex] = new TableEntry<>(key, value, null);
		} else if (table[entryIndex].key.equals(key)) { //first node is equal
			table[entryIndex].value = value;
			return;	//don't modify the size or modificationCount
		} else {	//slot contains entries, need to use chaining
			TableEntry<K,V> it = table[entryIndex];
			while (it.next != null) {
				if (it.key.equals(key)) { //found entry with given key
					it.value = value; //modify value only
					return;	//size and modificationCount are not increased
				}
				it = it.next;
			}
			//at this point it is the last entry at given index
			it.next = new TableEntry<>(key, value, null);
			
		}
		size++;		//entry was added
		modificationCount++;	//inner structure was modified
		if (tooMuchOverflow()) //dynamically increase capacity
			doubleCapacity();
	}
	
	/**
	 * Retrieves the value that is referenced by the given key.
	 *
	 * @param key Key which value is wanted. <code>null</code> can be passed
	 * 		and the method will return <code>null</code> in that case.
	 * @return value stored at the given key. If map contains no entry
	 * 		with such key, returns <code>null</code>.
	 */
	public V get(Object key) {
		if (key == null) { //map cannot store null keys
			return null;
		}
		TableEntry<K,V> it = table[calculateIndex(key, table.length)];
		while (it != null) {	//search for given key
			if (it.key.equals(key)) {	//found
				return it.value;
			}
			it = it.next;
		}
		return null;	//no entry with given key
	}
	
	/**
	 * Returns the number of currently stored entries in the map.
	 *
	 * @return Number of entries in the map.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks whether map stores some value at the given key.
	 *
	 * @param key Key which is to be found. Can be <code>null</code>.
	 * @return <code>true</code> if map contains entry with given key,
	 * 		<code>false</code> otherwise.
	 */
	public boolean containsKey(Object key) {
		if (key == null) { //map doesn't contain entries with null key
			return false;
		}
		TableEntry<K,V> it = table[calculateIndex(key, table.length)];
		while (it != null)  { //search slot for entry with given key
			if (it.key.equals(key)) {	//found
				return true;	
			}
			it = it.next;
		}
		return false;	//not found
	}
	
	/**
	 * Check whether map stores given value at any key.
	 *
	 * @param value Value to be found in the map.
	 * @return <code>true</code> if map stores given value,
	 * 		<code>false</code> otherwise.
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> kvTableEntry : table) {
			TableEntry<K, V> it = kvTableEntry;
			while (it != null) {
				if (it.value.equals(value)) { //found
					return true;
				}
				it = it.next;
			}
		}
		return false; //not found
	}
	
	/**
	 * Removes entry which is stored at given key. If there is no such entry,
	 * does nothing. Successful removal modifies the inner structure of the map,
	 * and renders all active iterators obsolete, throwing
	 * <code>ConcurrentModificationException</code> upon further use.
	 *
	 * @param key Key which entry is to be removed.
	 */
	public void remove(Object key) {
		if (!containsKey(key)) { //nothing to be done
			return;
		}
		
		int newIndex = calculateIndex(key, table.length); //inner table index
		TableEntry<K,V> it = table[newIndex]; //used for traversing the list
		
		if (it.key.equals(key)) { //first node of linked list
			table[calculateIndex(key, table.length)] = it.next;
		} else { //not first node
			while (!it.next.key.equals(key)) {
				it = it.next;
			}
			it.next = it.next.next; //unlinks it.next 
		}
		size--; //entry was removed
		modificationCount++; //insert structure was modified
		
	}
	
	/**
	 * Checks whether map contains any entries.
	 *
	 * @return <code>true</code> if the map is empty, <code>false</code>
	 * 		if it's not.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns a string representation of the map, in the form of
	 * 	"[key1=value1, key2=value2, ... ]"
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("["); //beginning of the map

		for (TableEntry<K, V> kvTableEntry : table) {
			TableEntry<K, V> it = kvTableEntry; //for traversing the list

			while (it != null) {
				if (!sb.toString().endsWith("[")) {
					sb.append(", "); //only if not the first entry
				}
				sb.append(it.toString());
				it = it.next;
			}
		}
		sb.append("]"); //end of the map
		return sb.toString();
	}
	
	/**
	 * Checks whether map stores too many elements relative to its
	 * capacity.
	 *
	 * @return <code>true</code> if the ratio of stored elements to the
	 * capacity is greater than or equal to 0.75, <code>false</code> otherwise.
	 */
	private boolean tooMuchOverflow() {
		return (double) size / table.length >= 0.75;
	}
	
	/**
	 * Doubles the capacity of the map. All previous entries are rehashed
	 * and stored at the nex positions.
	 */
	@SuppressWarnings("unchecked")
	private void doubleCapacity() {
		TableEntry<K,V>[] newTable = (TableEntry<K,V>[]) 
				new TableEntry[table.length * 2]; //new inner table
		for (TableEntry<K, V> kvTableEntry : table) { //all slots
			TableEntry<K, V> it = kvTableEntry;    //current slot
			while (it != null) { //for every node in that slot
				int newIndex = calculateIndex(kvTableEntry.key, 2 * table.length);

				if (newTable[newIndex] == null) { //new slot is empty
					newTable[newIndex] = new TableEntry<>(it.key, it.value, null);
				} else {    //inert at the end of new slot's linked list
					TableEntry<K, V> newIt = newTable[newIndex];
					while (newIt.next != null) {
						newIt = newIt.next;
					}
					newIt.next = new TableEntry<>(it.key, it.value, null);
				}
				it = it.next;
			}
		}
		//delete old table
		Arrays.fill(table, null);
		table = newTable;
		modificationCount++; //inner structure was modified
	}
	
	/**
	 * Removes all entries from the map.
	 */
	public void clear() {
		Arrays.fill(table, null);
		size = 0;
		modificationCount++;
	}
	
	/**
	 * Iterator of the hash map. Provides standard iterator methods,
	 * including <code>remove</code>, which can be called only once
	 * after each <code>next</code> call. Iterators are valid only when
	 * the map is not modified while they are used. This excludes the
	 * modification made by the <code>remove()</code> method of the iterator.
	 * Any usage of iterator not in terms with what is defined above will result
	 * in an exception.
	 * 
	 * @author jankovidakovic
	 *
	 */
	private class IteratorImpl implements 
	Iterator<SimpleHashtable.TableEntry<K, V>> {

		private TableEntry<K, V> nextEntry;	//next entry
		private TableEntry<K, V> lastReturned;	//last returned entry
		private boolean canRemove;	//eligibility for removal
		private final long savedModificationCount; //to know if map was modified
		
		/**
		 * Constructs an iterator that is ready to retrieve the first entry
		 * of the map.
		 */
		private IteratorImpl() {
			//private variables
			//index of next entry at the inner table
			int index = 0; //attempted index of first entry
			nextEntry = table[index];	//attempted first entry
			savedModificationCount = modificationCount;	//remember the value
			while (nextEntry == null) { //find first occupied slot
				index++;
				if (index >= table.length) { //the end
					break;
				}
				nextEntry = table[index]; 
			}
			//if nextEntry is still null, it means map contains no entries.
		}
		
		/**
		 * Checks whether there are any more entries eligible for retrieval
		 * by using this instance of iterator.
		 *
		 * @throws ConcurrentModificationException if the inner structure
		 * 	of map has been modified since this instance of iterator existed.
		 * @return <code>true</code> if there are more non-retrieved elements,
		 * 		<code>false</code> otherwise.
		 */
		@Override
		public boolean hasNext() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Map was modified.");
			}
			return nextEntry != null;
		}
		
		/**
		 * Retrieves the next non-retrieved entry of the map.
		 *
		 * @throws ConcurrentModificationException if the inner structure
		 * 	of map has been modified since this instance of iterator existed.
		 * @throws NoSuchElementException if there are no more non-retrieved
		 * 		entries.
		 * @return next non-retrieved entry of the map.
		 */
		@Override
		public TableEntry<K, V> next() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Map was modified.");
			}
			if (!hasNext()) { //no more entries
				throw new NoSuchElementException("No more entries.");
			}
			lastReturned = nextEntry; //save the entry which needs to be returned
			//find the entry that comes after
			int index = calculateIndex(nextEntry.key, table.length);
			nextEntry = nextEntry.next;
			while (nextEntry == null) {//search next slots for entries
				index++; //next slot at the inner table
				if (index >= table.length) { //the end of the table
					break;
				}
				nextEntry = table[index];
			}
			canRemove = true; //eligible for removal
			return lastReturned;
		}
		
		/**
		 * Removes the last entry returned by the <code>next()</code> method.
		 * Same entry cannot be removed twice, in which case an exception
		 * will occur. Also, map must have not been structurally modified 
		 * since the last call of <code>next()</code> method.
		 *
		 * @throws ConcurrentModificationException if the map has been
		 * 	structurally modified outside of this iterator.
		 * @throws  IllegalStateException if there are no entries eligible for
		 * 	removal (entry has already been removed, or the method next() hasn't
		 * 	been called on this iterator yet).
		 */
		public void remove() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Map was modified.");
			}
			if (!canRemove) { //no entries eligible for removal
				throw new IllegalStateException("Invalid remove.");
			}
			
			int index = calculateIndex(lastReturned.key, table.length);
			TableEntry<K, V> it = table[index];	//for traversing the slot's list
			if (it.key.equals(lastReturned.key)) { //first node
				table[index] = it.next;
			} else { //somewhere in the middle
				while (!it.next.key.equals(lastReturned.key)) {
					it = it.next;
				}
				it.next = it.next.next; //unlink the removed node
			}
			canRemove = false; //cannot remove anymore
		}
		
	}
	
	/**
	 * Method factory that provides the instance of map iterator.
	 *
	 * @return new instance of iterator of the map.
	 */
	public Iterator<SimpleHashtable.TableEntry<K,V>> iterator() {
		return new IteratorImpl();
	}
}





