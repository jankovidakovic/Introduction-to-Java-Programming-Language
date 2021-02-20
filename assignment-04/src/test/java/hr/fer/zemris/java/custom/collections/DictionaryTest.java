package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DictionaryTest {

	@Test
	void testDictionary() {
		Dictionary<String, Integer> dict = new Dictionary<>();
		//pass;
	}

	@Test
	void testSize() {
		Dictionary<String, Integer> dict = new Dictionary<>();
		assertEquals(0, dict.size());
		dict.put("Marko", 1);
		assertEquals(1, dict.size());
	}

	@Test
	void testClear() {
		Dictionary<String, Integer> dict = new Dictionary<>();
		dict.put("Marko", 1);
		dict.put("Luka", 2);
		
		dict.clear();
		assertEquals(0, dict.size());
	}

	@Test
	void testPutAndGet() {
		Dictionary<String, Integer> dict = new Dictionary<>();
		dict.put("Marko", 1);
		dict.put("Luka", 2);
		
		assertEquals(1, dict.get("Marko"));
		assertEquals(2, dict.get("Luka"));
		
	}


}
