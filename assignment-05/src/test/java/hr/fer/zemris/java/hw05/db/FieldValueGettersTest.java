package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {

	@Test
	void test() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/validInput.txt"));
		StudentDatabase sb = new StudentDatabase(lines);
		
		StudentRecord record = sb.forJMBAG("0000000001");
		
		assertEquals("0000000001", FieldValueGetters.JMBAG.get(record));
		assertEquals("Ivan", FieldValueGetters.FIRST_NAME.get(record));
		assertEquals("Ivić", FieldValueGetters.LAST_NAME.get(record));
		
	}

}
