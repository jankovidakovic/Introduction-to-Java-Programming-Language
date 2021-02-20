package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class SimpleHashtableTest {

	@Test
	void testSimpleHashtable() {
		SimpleHashtable<Object, Object> table = new SimpleHashtable<>();
		//pass
	}

	@Test
	void testSimpleHashtableInt() {
		SimpleHashtable<Object, Object> table = new SimpleHashtable<>(5);
		assertThrows(IllegalArgumentException.class, () -> 
		new SimpleHashtable<Object, Object>(0));
	}

	@Test
	void testPut() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		assertEquals(2, test.size());
		
		test.put("Marko", 3);
		assertEquals(2, test.size());
		assertEquals(3, test.get("Marko"));
		
		assertThrows(NullPointerException.class, () -> test.put(null, 4));
		
	}

	@Test
	void testGet() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		assertEquals(1, test.get("Marko"));
		assertEquals(2, test.get("Ivan"));
		
		assertEquals(null, test.get("Luka"));
		assertEquals(null, test.get(null));
	}

	@Test
	void testSize() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		assertEquals(0, test.size());
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		assertEquals(2, test.size());
	}

	@Test
	void testContainsKey() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		assertEquals(true, test.containsKey("Marko"));
		assertEquals(false, test.containsKey("Luka"));
		assertEquals(false, test.containsKey(null));
	}

	@Test
	void testContainsValue() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		assertEquals(true, test.containsValue(1));
		assertEquals(false, test.containsValue(3));
		assertEquals(false, test.containsValue(null));
		
	}

	@Test
	void testRemove() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		test.put("Luka", 3);
		
		test.remove("Marko");
		assertEquals(2, test.size());
		assertEquals(true, test.containsKey("Ivan"));
		assertEquals(true, test.containsKey("Luka"));
		
	}

	@Test
	void testIsEmpty() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		
		assertEquals(true, test.isEmpty());
		
		test.put("Marko", 1);
		assertEquals(false, test.isEmpty());
		
		test.remove("Marko");
		assertEquals(true, test.isEmpty());
	}

	@Test
	void testToString() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		assertEquals("[Marko=1, Ivan=2]", test.toString());
		
		test.clear();
		assertEquals("[]", test.toString());
	}

	@Test
	void testClear() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		
		test.clear();
		assertEquals(0, test.size());
	}

	@Test
	void testIterator() {
		SimpleHashtable<String, Integer> test = new SimpleHashtable<>();
		test.put("Marko", 1);
		test.put("Ivan", 2);
		test.put("Luka", 3);
		
		var it = test.iterator();
		assertEquals(true, it.hasNext());
		it.next();
		assertEquals(true, it.hasNext());
		it.next();
		assertEquals(true, it.hasNext());
		it.next();
		assertEquals(false, it.hasNext());
		assertThrows(NoSuchElementException.class, () -> it.next());
		
		
	}
	
	@Test
	void testTableEntry() {
		SimpleHashtable.TableEntry<String, Integer> entry = 
				new SimpleHashtable.TableEntry<>("Marko", 1, null);
		assertEquals("Marko", entry.getKey());
		assertEquals(1, entry.getValue());
		
		entry.setValue(2);
		assertEquals(2, entry.getValue());
		
		assertEquals("Marko=2", entry.toString());
	}

}
