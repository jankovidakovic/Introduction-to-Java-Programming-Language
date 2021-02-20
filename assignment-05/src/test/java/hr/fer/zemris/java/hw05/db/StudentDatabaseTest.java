package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

class StudentDatabaseTest {
	@Test
	@SuppressWarnings("unused")
	void testStudentDatabaseValidInput() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/validInput.txt"), 
				StandardCharsets.UTF_8);
		StudentDatabase sb = new StudentDatabase(lines);
		//pass
	}
	
	@Test
	void testStudentDatabaseInvalidInputMultipleJmbags() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/invalidInputMultipleJmbags.txt"), 
				StandardCharsets.UTF_8);
		assertThrows(IllegalArgumentException.class, () -> 
		new StudentDatabase(lines));
	}
	
	@Test
	void testStudentDatabaseInvalidInputGrade() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/invalidInputGrade.txt"), 
				StandardCharsets.UTF_8);
		assertThrows(IllegalArgumentException.class, () -> 
		new StudentDatabase(lines));
	}

	@Test
	void testForJMBAG() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/validInput.txt"));
		StudentDatabase sb = new StudentDatabase(lines);
		assertEquals("0000000001", sb.forJMBAG("0000000001").getJmbag());
		assertEquals("0000000002", sb.forJMBAG("0000000002").getJmbag());
		assertEquals("0000000003", sb.forJMBAG("0000000003").getJmbag());
		
		assertEquals(null, sb.forJMBAG("1234567890"));
	}

	@Test
	void testFilter() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/java/"
				+ "hr/fer/zemris/java/hw05/db/validInput.txt"));
		StudentDatabase sb = new StudentDatabase(lines);
		
		IFilter alwaysTrue = (value) -> true;
		IFilter alwaysFalse = (value) -> false;
		
		List<StudentRecord> filteredLines = sb.filter(alwaysTrue);
		assertEquals("0000000001", filteredLines.get(0).getJmbag());
		assertEquals("0000000002", filteredLines.get(1).getJmbag());
		assertEquals("0000000003", filteredLines.get(2).getJmbag());
		
		assertEquals(true, sb.filter(alwaysFalse).isEmpty());
	}

}
