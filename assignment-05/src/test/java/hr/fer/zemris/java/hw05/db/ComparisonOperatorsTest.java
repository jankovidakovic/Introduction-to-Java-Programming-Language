package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {

	@Test
	void testLike() {
		IComparisonOperator like = ComparisonOperators.LIKE;
		assertEquals(true, like.satisfied("AAA", "AAA"));
		assertEquals(true, like.satisfied("AAAA", "AA*A"));
		assertEquals(true, like.satisfied("AAAA", "*"));
		assertEquals(true, like.satisfied("AAAA", "A*"));
		assertEquals(true, like.satisfied("AAAA", "A*A"));
		assertEquals(false, like.satisfied("AAA", "AA*AA"));
		
	}

	@Test
	void testEquals() {
		IComparisonOperator equal = ComparisonOperators.EQUALS;
		assertEquals(true, equal.satisfied("AAA", "AAA"));
		assertEquals(false, equal.satisfied("AAA", "AA"));
	}
	
	@Test
	void testNotEquals() {
		IComparisonOperator notEqual = ComparisonOperators.NOT_EQUALS;
		assertEquals(false, notEqual.satisfied("AAA", "AAA"));
		assertEquals(true, notEqual.satisfied("AAA", "AA"));
	}
	
	@Test
	void testLess() {
		IComparisonOperator less = ComparisonOperators.LESS;
		assertEquals(true, less.satisfied("AA", "AAA"));
		assertEquals(true, less.satisfied("AAA", "AAB"));
		
		assertEquals(false, less.satisfied("BBB", "BB"));
		assertEquals(false, less.satisfied("BBB", "BBA"));
		
	}
	
	@Test
	void testLessOrEquals() {
		IComparisonOperator lessOrEquals = ComparisonOperators.LESS_OR_EQUALS;
		assertEquals(true, lessOrEquals.satisfied("AAA", "AAA"));
		assertEquals(true, lessOrEquals.satisfied("AA", "AAA"));
		
		assertEquals(false, lessOrEquals.satisfied("AAB", "AAA"));
		assertEquals(false, lessOrEquals.satisfied("AAA", "AA"));
		
	}
	
	@Test
	void testGreater() {
		IComparisonOperator less = ComparisonOperators.GREATER;
		assertEquals(false, less.satisfied("AA", "AAA"));
		assertEquals(false, less.satisfied("AAA", "AAB"));
		
		assertEquals(true, less.satisfied("BBB", "BB"));
		assertEquals(true, less.satisfied("BBB", "BBA"));
		
	}
	
	@Test
	void testGreaterOrEquals() {
		IComparisonOperator lessOrEquals = ComparisonOperators.GREATER_OR_EQUALS;
		assertEquals(true, lessOrEquals.satisfied("AAA", "AAA"));
		assertEquals(false, lessOrEquals.satisfied("AA", "AAA"));
		
		assertEquals(true, lessOrEquals.satisfied("AAB", "AAA"));
		assertEquals(true, lessOrEquals.satisfied("AAA", "AA"));
		
		
	}
}
