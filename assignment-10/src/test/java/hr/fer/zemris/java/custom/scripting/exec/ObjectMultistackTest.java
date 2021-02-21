package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ObjectMultistackTest {

	@Test
	void test() {

		ObjectMultistack multistack = new ObjectMultistack();

		ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
		multistack.push("year", year);

		ValueWrapper price = new ValueWrapper("200.51");
		multistack.push("price", price);

		assertEquals(2000, multistack.peek("year").getValue());
		assertEquals("200.51", multistack.peek("price").getValue());

		multistack.peek("year").add("5");
		assertEquals(2005, multistack.peek("year").getValue());

		multistack.peek("year").add(5.0);
		assertEquals(2010.0, multistack.peek("year").getValue());
	}

}
