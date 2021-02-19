package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	@Test
	void testIsEmpty() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		assertEquals(true, testAIC.isEmpty());
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler3");
		
		assertEquals(false, testAIC.isEmpty());
		
	}

	@Test
	void testSize() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		assertEquals(0, testAIC.size());
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler3");
		
		assertEquals(3, testAIC.size());
	}

	@Test
	void testAdd() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(3);
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler3");
		
		assertEquals(true, testAIC.contains("Filler1")
						&& testAIC.contains("Filler2")
						&& testAIC.contains("Filler3"));
		
		testAIC.add("Filler4"); //capacity should be doubled
		assertEquals(true, testAIC.contains("Filler4"));
		
		assertThrows(NullPointerException.class, () -> testAIC.add(null));
	}

	@Test
	void testContains() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(3);
		
		testAIC.add("Filler1");
		
		assertEquals(true, testAIC.contains("Filler1"));
		assertEquals(false, testAIC.contains(null));
	}

	@Test
	void testRemoveObject() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		
		assertEquals(true, testAIC.remove("Filler1"));
		assertEquals(1, testAIC.indexOf("Filler1"));
		
		assertEquals(true, testAIC.remove("Filler1"));
		assertEquals(-1, testAIC.indexOf("Filler1"));
		
		assertEquals(false, testAIC.remove("Filler1"));
		assertEquals(-1, testAIC.indexOf("Filler1"));
		
	}

	@Test
	void testToArray() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler3");
		testAIC.add("Filler4");
		
		Object[] array = testAIC.toArray();
		
		assertEquals(4, array.length);
		assertEquals("Filler1", array[0]);
		assertEquals("Filler2", array[1]);
		assertEquals("Filler3", array[2]);
		assertEquals("Filler4", array[3]);
		
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
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler3");
		
		testAIC.forEach(testProcessor);
		
		String result = sb.toString();
		
		assertEquals(true, result.contains("Filler1"));
		assertEquals(true, result.contains("Filler2"));
		assertEquals(true, result.contains("Filler3"));
		
		
	}

	@Test
	void testClear() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.add("Filler3");
		
		testAIC.clear();
		assertEquals(0, testAIC.size());
	}

	@Test
	void testArrayIndexedCollection() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection();
		
		assertEquals(0, testAIC.size());
	}

	@Test
	void testArrayIndexedCollectionInt() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(10);
		
		assertThrows(IllegalArgumentException.class, () ->
		new ArrayIndexedCollection(0));
		
	}

	@Test
	void testArrayIndexedCollectionCollection() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(2);
		
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		
		ArrayIndexedCollection testAIC2 = new ArrayIndexedCollection(testAIC);
		
		assertEquals(true, testAIC2.contains("Filler1"));
		assertEquals(true, testAIC2.contains("Filler2"));
		
		assertThrows(IllegalArgumentException.class, () -> 
		new ArrayIndexedCollection(new ArrayIndexedCollection(0)));
		
		assertThrows(NullPointerException.class, () -> 
		new ArrayIndexedCollection(null));
		
	}

	@Test
	void testArrayIndexedCollectionCollectionInt() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(2);
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		ArrayIndexedCollection test2AIC = new ArrayIndexedCollection(testAIC, 3);
		assertEquals(true, test2AIC.contains("Filler1")
						&& test2AIC.contains("Filler2"));	
		assertThrows(IllegalArgumentException.class, () -> 
		new ArrayIndexedCollection(new ArrayIndexedCollection(0), 5));
		assertThrows(NullPointerException.class, () -> 
		new ArrayIndexedCollection(null, 5));
		
		
	}

	@Test
	void testGet() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(2);
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		assertEquals("Filler1", testAIC.get(0));
		assertEquals("Filler2", testAIC.get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> 
		testAIC.get(2));
		assertThrows(IndexOutOfBoundsException.class, () -> 
		testAIC.get(-1));
		
	}

	@Test
	void testInsert() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(2);
		testAIC.add("Filler1");
		testAIC.add("Filler3");
		testAIC.insert("Middle", 1);
		assertEquals(3, testAIC.size());
		assertEquals("Filler1", testAIC.get(0));
		assertEquals("Middle", testAIC.get(1), "inserted element");
		assertEquals("Filler3", testAIC.get(2));
		
		testAIC.insert("End", testAIC.size());
		assertEquals(4, testAIC.size());
		assertEquals("Filler1", testAIC.get(0));
		assertEquals("Middle", testAIC.get(1));
		assertEquals("Filler3", testAIC.get(2));
		assertEquals("End", testAIC.get(3));
		
		assertThrows(IndexOutOfBoundsException.class, () ->
		testAIC.insert("InvalidPosition", -1));
		
		assertThrows(IndexOutOfBoundsException.class, () ->
		testAIC.insert("InvalidPosition", 5));
		
	}

	@Test
	void testIndexOf() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(2);
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		assertEquals(0, testAIC.indexOf("Filler1"));
		assertEquals(-1, testAIC.indexOf("NowhereToBeFound"));
	}

	@Test
	void testRemoveInt() {
		ArrayIndexedCollection testAIC = new ArrayIndexedCollection(2);
		testAIC.add("Filler1");
		testAIC.add("Filler2");
		testAIC.remove(0);
		assertEquals(1, testAIC.size());
		assertEquals(false, testAIC.contains("Filler1"));
		assertEquals(true, testAIC.contains("Filler2"));
		
		assertThrows(IndexOutOfBoundsException.class, () -> 
				testAIC.remove(-1));
		
		assertThrows(IndexOutOfBoundsException.class, () -> 
		testAIC.remove(2));

	}

}
