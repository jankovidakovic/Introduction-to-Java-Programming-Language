package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ObjectStackTest {

	@Test
	void testObjectStack() {
		ObjectStack stack = new ObjectStack();
		//passes
	}

	@Test
	void testObjectStackInt() {
		ObjectStack stack = new ObjectStack(3);
		assertThrows(IllegalArgumentException.class, () -> 
		new ObjectStack(0));
	}

	@Test
	void testIsEmpty() {
		ObjectStack stack = new ObjectStack();
		assertEquals(true, stack.isEmpty());
		stack.push("NonEmpty");
		assertEquals(false, stack.isEmpty());
	}

	@Test
	void testSize() {
		ObjectStack stack = new ObjectStack();
		assertEquals(0, stack.size());
		stack.push("Filler1");
		assertEquals(1, stack.size());
		stack.push("Filler2");
		assertEquals(2, stack.size());
		stack.pop();
		assertEquals(1, stack.size());
	}

	@Test
	void testPush() {
		ObjectStack stack = new ObjectStack();
		stack.push("Filler1");
		assertEquals(1, stack.size());
		assertThrows(NullPointerException.class, () -> stack.push(null));
		
	}

	@Test
	void testPop() {
		ObjectStack stack = new ObjectStack();
		stack.push("Filler1");
		stack.push("Filler2");
		assertEquals("Filler2", stack.pop());
		assertEquals("Filler1", stack.pop());
		assertThrows(EmptyStackException.class, () -> stack.pop());
	}

	@Test
	void testPeek() {
		ObjectStack stack = new ObjectStack();
		stack.push("Filler1");
		stack.push("Filler2");
		
		assertEquals("Filler2", stack.peek());
		stack.pop();
		assertEquals("Filler1", stack.peek());
		stack.pop();
		assertThrows(EmptyStackException.class, () -> stack.peek());
	}

	@Test
	void testClear() {
		ObjectStack stack = new ObjectStack();
		stack.push("Filler1");
		stack.push("Filler2");
		
		assertEquals(2,  stack.size());
		stack.clear();
		assertEquals(0, stack.size());
	}

}
