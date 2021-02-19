package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EmptyStackExceptionTest {

	@Test
	void testEmptyStackException() {
		EmptyStackException exception = new EmptyStackException();
		return; 	//pass
	}

	@Test
	void testEmptyStackExceptionString() {
		EmptyStackException exception = new EmptyStackException("Test");
		return; 	//pass
	}

}
