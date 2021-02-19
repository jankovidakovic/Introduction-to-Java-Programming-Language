package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ProcessorTest {

	@Test
	void testProcess() {
		Processor processor = new Processor(); //constructing
		processor.process("DoNothinWithThisString"); //processing
		return;	//pass
	}

	@Test
	void testProcessor() {
		Processor processor = new Processor(); //constructing
		return;	//pass
	}

}
