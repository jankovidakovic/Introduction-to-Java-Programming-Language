package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

class QueryFilterTest {

	@Test
	void test() throws IOException {
		QueryParser qp = new QueryParser("jmbag=\"0000000001\" and lastName>\"A\"");
		
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/validInput.txt"));
		StudentDatabase sb = new StudentDatabase(lines);
		
		for (StudentRecord r : sb.filter(new QueryFilter(qp.getQuery()))) {
			assertEquals("0000000001", r.getJmbag());
			assertEquals(true, ComparisonOperators.GREATER.satisfied(
					r.getLastName(), "A"));
		}
	}

}
