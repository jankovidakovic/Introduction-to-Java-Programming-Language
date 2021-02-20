package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QueryParserTest {

	@Test
	void test() {
		QueryParser qp1 = new QueryParser(" jmbag  	=\"0123456789\"	");
		assertEquals(true, qp1.isDirectQuery());
		assertEquals("0123456789", qp1.getQueriedJMBAG());
		assertEquals(1, qp1.getQuery().size());
		
		QueryParser qp2 = new QueryParser(" jmbag=\"0123456789\" and lastName"
				+ ">\"J\"");
		assertEquals(false, qp2.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp2.getQueriedJMBAG());
		assertEquals(2, qp2.getQuery().size());
		
		assertThrows(QueryParserException.class, () -> new QueryParser("lmao"));
		assertThrows(QueryParserException.class, () -> new QueryParser(""
				+ "jmbag like 2"));
		
		
	}

}
