package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {

	@Test
	void testSize() {
		
	}

	@Test
	void testAdd() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		assertEquals(1, list.size());
		list.add("Filler2");
		assertEquals(2, list.size());
		list.add("Filler3");
		assertEquals(3, list.size());
		
		assertEquals("Filler1", list.get(0));
		assertEquals("Filler2", list.get(1));
		assertEquals("Filler3", list.get(2));
	}

	@Test
	void testContains() {
		
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		assertEquals(true, list.contains("Filler1"));
		assertEquals(true, list.contains("Filler2"));
		assertEquals(true, list.contains("Filler3"));
		
	}

	@Test
	void testRemove() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		assertEquals(true, list.remove("Filler2"));
		assertEquals(2, list.size());
		assertEquals(false, list.contains("Filler2"));
		assertEquals(true, list.contains("Filler1"));
		assertEquals(true, list.contains("Filler3"));
		
		assertEquals(true, list.remove("Filler1"));
		assertEquals(1, list.size());
		assertEquals(false, list.contains("Filler1"));
		assertEquals(true, list.contains("Filler3"));
		
		assertEquals(true, list.remove("Filler3"));
		assertEquals(0, list.size());
		assertEquals(false, list.contains("Filler3"));
		
		assertEquals(false, list.remove("Nothing"));
		
	}

	@Test
	void testToArray() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		Object[] array = list.toArray();
		assertEquals(3, array.length);
		assertEquals("Filler1", array[0]);
		assertEquals("Filler2", array[1]);
		assertEquals("Filler3", array[2]);
	}

	@Test
	void testForEach() {
		
		StringBuilder sb = new StringBuilder();
		
		class TestProcessor extends Processor {
			@Override
			public void process(Object value) {
				sb.append(value.toString());
			}
		}
		
		Processor testProcessor = new TestProcessor();
		
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		list.forEach(testProcessor);
		String result = sb.toString();
		assertEquals(true, result.contains("Filler1"));
		assertEquals(true, result.contains("Filler2"));
		assertEquals(true, result.contains("Filler3"));
		
		
	}

	@Test
	void testClear() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		list.clear();
		assertEquals(true, list.isEmpty());
	}

	@Test
	void testLinkedListIndexedCollection() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		assertEquals(0, list.size());
		
	}

	@Test
	void testLinkedListIndexedCollectionCollection() {
		Collection collection = new ArrayIndexedCollection(3);
		collection.add("Filler1");
		collection.add("Filler2");
		collection.add("Filler3");
		LinkedListIndexedCollection list = new LinkedListIndexedCollection(collection);
		
	}

	@Test
	void testGet() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		assertEquals("Filler1", list.get(0));
		assertEquals("Filler2", list.get(1));
		assertEquals("Filler3", list.get(2));
		
		assertThrows(IndexOutOfBoundsException.class, () -> 
				list.get(-1));
		
		assertThrows(IndexOutOfBoundsException.class, () -> 
		list.get(3));

		
	}

	@Test
	void testInsert() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		list.insert("Beginning", 0);
		assertEquals(4, list.size());
		assertEquals(0, list.indexOf("Beginning"));
		
		list.insert("End", 4);
		assertEquals(5, list.size());
		assertEquals(4, list.indexOf("End"));
		
		list.insert("Middle", 2);
		assertEquals(6, list.size());
		assertEquals(2, list.indexOf("Middle"));
		
		
	}

	@Test
	void testIndexOf() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Filler1");
		list.add("Filler2");
		list.add("Filler3");
		
		assertEquals(0, list.indexOf("Filler1"));
		assertEquals(1, list.indexOf("Filler2"));
		assertEquals(2, list.indexOf("Filler3"));
		
		assertEquals(-1, list.indexOf("ImNotInside"));
		
	}

}
