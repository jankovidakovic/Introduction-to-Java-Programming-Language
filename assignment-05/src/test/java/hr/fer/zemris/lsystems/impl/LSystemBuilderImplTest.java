package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;

class LSystemBuilderImplTest {

	@Test
	void test() {
		LSystemBuilder lsysimpl = new LSystemBuilderImpl()
				.setAxiom("F")
				.registerProduction('F', "F+F--F+F");
		
		LSystem lsys = lsysimpl.build();
		assertEquals("F", lsys.generate(0));
		assertEquals("F+F--F+F", lsys.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lsys.generate(2));
		
	}

}
