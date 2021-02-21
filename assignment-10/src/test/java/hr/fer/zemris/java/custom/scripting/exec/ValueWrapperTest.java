package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {

	@Test
	void testNullValue() {
		ValueWrapper v = new ValueWrapper(null);
		v.add(Integer.valueOf(0));
		assertEquals(0, v.getValue());
		assertTrue(v.getValue() instanceof Integer);
	}

	@Test
	void testThrowsForUnsupportedTypes() {
		ValueWrapper v = new ValueWrapper(Boolean.valueOf(true));
		assertThrows(RuntimeException.class, () -> v.add(Integer.valueOf(189)));
		assertTrue(v.getValue() instanceof Boolean); // type must not change if
														// operation fails
	}

	@Test
	void testWorksWithStrings() {
		ValueWrapper v1 = new ValueWrapper("1.2");
		ValueWrapper v2 = new ValueWrapper("3.4");
		v1.add(v2.getValue());
		assertEquals(4.6, v1.getValue());
		assertTrue(v1.getValue() instanceof Double);
		assertTrue(v2.getValue() instanceof String); // type of second operand
														// should not change
	}

	@Test
	void testUneligibleString() {
		ValueWrapper v1 = new ValueWrapper("Ankica");
		assertThrows(RuntimeException.class,
				() -> v1.add(Integer.valueOf(189)));
		assertTrue(v1.getValue() instanceof String);
	}

	@Test
	void testAddStringWithNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper("12");
		v1.add(v2.getValue());
		assertEquals(12, v1.getValue());
	}

	@Test
	void testCompareDoesntChangeData() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper("-1");
		assertEquals(1, v1.numCompare(v2.getValue()));
		assertTrue(v1.getValue() == null);
		assertEquals(v2.getValue(), "-1");
	}

	@Test
	void testConvertsToDoubleAfterArithmetic() {
		ValueWrapper v1 = new ValueWrapper(Integer.valueOf(1));
		assertTrue(v1.getValue() instanceof Integer);
		v1.add("5.0");
		assertEquals(6.0, v1.getValue());
		assertTrue(v1.getValue() instanceof Double);
	}

	@Test
	void testConvertsFromStringToInt() {
		ValueWrapper v1 = new ValueWrapper("1");
		assertTrue(v1.getValue() instanceof String);
		v1.add("2");
		assertEquals(3, v1.getValue());
		assertTrue(v1.getValue() instanceof Integer);
	}

}
