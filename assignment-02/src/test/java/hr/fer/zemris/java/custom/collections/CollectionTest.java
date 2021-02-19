package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CollectionTest {

	@Test
	void testCollection() {
		Collection collection = new Collection();
		//if it does nothing, it passes by default
	}

	@Test
	void testIsEmpty() {
		Collection collection = new Collection();
		assertEquals(true, collection.size() == 0);
		assertEquals(false, collection.size() != 0);
		
	}

	@Test
	void testSize() {
		Collection collection = new Collection();
		assertEquals(0, collection.size());
	}

	@Test
	void testAdd() {
		Collection collection = new Collection();
		collection.add("Filler1");
		//passes
	}

	@Test
	void testContains() {
		Collection collection = new Collection();
		collection.add("Filler1");
		assertEquals(false, collection.contains("Filler1"));
	}

	@Test
	void testRemove() {
		Collection collection = new Collection();
		collection.remove("DoesNothing");
		//passes
	}

	@Test
	void testToArray() {
		Collection collection = new Collection();
		assertThrows(UnsupportedOperationException.class, () -> 
		collection.toArray());
	}

	@Test
	void testForEach() {
		
		Processor processor = new Processor();
		Collection collection = new Collection();
		collection.forEach(processor);
		//passes
	}

	@Test
	void testAddAll() {
		Collection collection1 = new Collection();
		Collection collection2 = new Collection();
		collection1.addAll(collection2);
		//passes
	}

	@Test
	void testClear() {
		Collection collection = new Collection();
		collection.clear();
		//passes
	}

}
