package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ComplexNumberTest {

	@Test
	void testComplexNumber() {
		ComplexNumber cn = new ComplexNumber(1.0, 2.0);
		assertEquals(1.0, cn.getReal(), 1E-12);
	}

	@Test
	void testFromReal() {
		ComplexNumber cn = ComplexNumber.fromReal(1.0);
		assertEquals(1.0, cn.getReal(), 1E-12);
		assertEquals(0.0, cn.getImaginary(), 1E-12);
	}

	@Test
	void testFromImaginary() {
		ComplexNumber cn = ComplexNumber.fromImaginary(1.0);
		assertEquals(0.0, cn.getReal(), 1E-12);
		assertEquals(1.0, cn.getImaginary(), 1E-12);
	}

	@Test
	void testFromMagnitudeAndAngleOneReal() {
		
		ComplexNumber targetNumber = new ComplexNumber(1, 0);
		ComplexNumber returnedNumber = ComplexNumber.fromMagnitudeAndAngle(
					1, 0);
		assertEquals(targetNumber.getReal(), returnedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), returnedNumber.getImaginary()
					, 1E-12);
				
	}
	
	void testFromMagnitudeAndAngleOneImaginary() {
		ComplexNumber targetNumber = new ComplexNumber(0, 1);
		ComplexNumber returnedNumber = ComplexNumber.fromMagnitudeAndAngle(
				1, Math.PI / 2);
		assertEquals(targetNumber.getReal(), returnedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), returnedNumber.getImaginary()
					, 1E-12);
		
	}
	
	void testFromMagnitudeAndAngleMinusOneReal() {
		ComplexNumber targetNumber = new ComplexNumber(-1, 0);
		ComplexNumber returnedNumber = ComplexNumber.fromMagnitudeAndAngle(
				1, Math.PI);
		assertEquals(targetNumber.getReal(), returnedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), returnedNumber.getImaginary()
					, 1E-12);;
	}
	
	void testFromMagnitudeAndAngleMinusOneImaginary() {
		ComplexNumber targetNumber = new ComplexNumber(0, -1);
		ComplexNumber returnedNumber = ComplexNumber.fromMagnitudeAndAngle(
				1, Math.PI * 1.5);
		assertEquals(targetNumber.getReal(), returnedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), returnedNumber.getImaginary()
					, 1E-12);
	}

	@Test
	void testParseFullReal() {
		
		ComplexNumber targetNumber = new ComplexNumber(351, 0);
		ComplexNumber parsedNumber = ComplexNumber.parse("351");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(-317, 0);
		parsedNumber = ComplexNumber.parse("-317");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(3.51, 0);
		parsedNumber = ComplexNumber.parse("3.51");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(-3.17, 0);
		parsedNumber = ComplexNumber.parse("-3.17");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
	}
	
	@Test
	void testParseFullImaginary() {
		
		ComplexNumber targetNumber = new ComplexNumber(0, 351);
		ComplexNumber parsedNumber = ComplexNumber.parse("351i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(0, -317);
		parsedNumber = ComplexNumber.parse("-317i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(0, 3.51);
		parsedNumber = ComplexNumber.parse("3.51i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(0, -3.17);
		parsedNumber = ComplexNumber.parse("-3.17i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
			
	}
	
	@Test
	void testParseOnlyImaginaryUnit() {
		ComplexNumber targetNumber = new ComplexNumber(0, 1);
		ComplexNumber parsedNumber = ComplexNumber.parse("i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
		targetNumber = new ComplexNumber(0, -1);
		parsedNumber = ComplexNumber.parse("-i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary()
					, parsedNumber.getImaginary(), 1E-12 );
		
	}
	
	
	@Test
	void testParseImaginaryUnitBeforeMagnitude() {
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("i351"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("i-317"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("i3.51"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("i-3.17"));
		
	}
	
	@Test
	void testParseRealAndImaginaryParts() {
		ComplexNumber targetNumber = new ComplexNumber(31, 24);
		ComplexNumber parsedNumber = ComplexNumber.parse("31+24i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), parsedNumber.getImaginary()
					, 1E-12);
		
		targetNumber = new ComplexNumber(-2.71, -3.15);
		parsedNumber = ComplexNumber.parse("-2.71-3.15i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), parsedNumber.getImaginary()
					, 1E-12);
		
		targetNumber = new ComplexNumber(-1, -1);
		parsedNumber = ComplexNumber.parse("-1-i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), parsedNumber.getImaginary()
					, 1E-12);

	}
	

	@Test
	void testParseLeadingPlusSignOK() {
		ComplexNumber targetNumber = new ComplexNumber(2.71, 0);
		ComplexNumber parsedNumber = ComplexNumber.parse("+2.71");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), parsedNumber.getImaginary()
					, 1E-12);
		
		targetNumber = new ComplexNumber(2.71, 3.15);
		parsedNumber = ComplexNumber.parse("+2.71+3.15i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), parsedNumber.getImaginary()
					, 1E-12);
		
		targetNumber = new ComplexNumber(0, 1);
		parsedNumber = ComplexNumber.parse("+i");
		assertEquals(targetNumber.getReal(), parsedNumber.getReal(), 1E-12);
		assertEquals(targetNumber.getImaginary(), parsedNumber.getImaginary()
					, 1E-12);
	}
	
	@Test
	void testParseLeadingPlusSignNotOK() {
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("+2.71-+3.15i"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("-+2.71"));
	}
	
	@Test
	void testParseMultipleSignsNotSupported() {
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("-+2.71"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("--2.71"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("-2.71+-3.15i"));
		assertThrows(IllegalArgumentException.class, () -> 
		ComplexNumber.parse("-+2.71"));
	}
	
	
	
	@Test
	void testGetReal() {
		ComplexNumber cn = new ComplexNumber(2, 3);
		assertEquals(2, cn.getReal(), 1E-12);
		cn = new ComplexNumber(0, 3);
		assertEquals(0, cn.getReal(), 1E-12);
		cn = new ComplexNumber(-2, 3);
		assertEquals(-2, cn.getReal(), 1E-12);
		
	}

	@Test
	void testGetImaginary() {
		ComplexNumber cn = new ComplexNumber(2, 3);
		assertEquals(3, cn.getImaginary(), 1E-12);
		cn = new ComplexNumber(2, 0);
		assertEquals(0, cn.getImaginary(), 1E-12);
		cn = new ComplexNumber(2, -3);
		assertEquals(-3, cn.getImaginary(), 1E-12);
	}

	@Test
	void testGetMagnitude() {
		ComplexNumber cn = new ComplexNumber(1, 1);
		assertEquals(Math.sqrt(2), cn.getMagnitude(), 1E-12);
		
		cn = new ComplexNumber(0, -1);
		assertEquals(1, cn.getMagnitude(), 1E-12);
		
		cn = new ComplexNumber(1, -1);
		assertEquals(Math.sqrt(2), cn.getMagnitude(), 1E-12);
		
		cn = new ComplexNumber(0, 0);
		assertEquals(0, cn.getMagnitude(), 1E-12);
		
		
	}

	@Test
	void testGetAngleFirstQuadrant() {
		ComplexNumber cn = new ComplexNumber(1, 1);
		assertEquals(Math.PI / 4, cn.getAngle(), 1E-12);
	}
	
	@Test
	void testGetAngleSecondQuadrant() {
		ComplexNumber cn = new ComplexNumber(-1, 1);
		assertEquals(Math.PI * 0.75, cn.getAngle(), 1E-12);
	}
	
	@Test
	void testGetAngleThirdQuadrant() {
		ComplexNumber cn = new ComplexNumber(-1, -1);
		assertEquals(Math.PI * 1.25, cn.getAngle(), 1E-12);
	}
	
	@Test
	void testGetAngleFourthQuadrant() {
		ComplexNumber cn = new ComplexNumber(1, -1);
		assertEquals(Math.PI * 1.75, cn.getAngle(), 1E-12);
	}
	
	@Test
	void testGetAngleZero() {
		ComplexNumber cn = new ComplexNumber(0, 0);
		assertEquals(Double.NaN, cn.getAngle(), 1E-12);
	}
	
	
	@Test
	void testAdd() {
		ComplexNumber firstOperand = new ComplexNumber(1, 1);
		ComplexNumber secondOperand = new ComplexNumber(1, 1);
		ComplexNumber targetResult = new ComplexNumber(2, 2);
		ComplexNumber actualResult = firstOperand.add(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
	}

	@Test
	void testSub() {
		ComplexNumber firstOperand = new ComplexNumber(1, 1);
		ComplexNumber secondOperand = new ComplexNumber(1, 1);
		ComplexNumber targetResult = new ComplexNumber(0, 0);
		ComplexNumber actualResult = firstOperand.sub(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
	}

	@Test
	void testMul() {
		ComplexNumber firstOperand = new ComplexNumber(1, 1);
		ComplexNumber secondOperand = new ComplexNumber(1, 0);
		ComplexNumber targetResult = new ComplexNumber(1, 1);
		ComplexNumber actualResult = firstOperand.mul(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		firstOperand = new ComplexNumber(5, 2);
		secondOperand = new ComplexNumber(1, 0);
		targetResult = new ComplexNumber(5, 2);
		actualResult = firstOperand.mul(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		firstOperand = new ComplexNumber(5, 2);
		secondOperand = new ComplexNumber(0, 1);
		targetResult = new ComplexNumber(-2, 5);
		actualResult = firstOperand.mul(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		firstOperand = new ComplexNumber(0, 1);
		secondOperand = new ComplexNumber(0, 1);
		targetResult = new ComplexNumber(-1, 0);
		actualResult = firstOperand.mul(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		
	}

	@Test
	void testDivNotZero() {
		ComplexNumber firstOperand = new ComplexNumber(1, 1);
		ComplexNumber secondOperand = new ComplexNumber(1, 0);
		ComplexNumber targetResult = new ComplexNumber(1, 1);
		ComplexNumber actualResult = firstOperand.div(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		firstOperand = new ComplexNumber(5, 2);
		secondOperand = new ComplexNumber(1, 0);
		targetResult = new ComplexNumber(5, 2);
		actualResult = firstOperand.div(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		firstOperand = new ComplexNumber(5, 2);
		secondOperand = new ComplexNumber(0, 1);
		targetResult = new ComplexNumber(2, -5);
		actualResult = firstOperand.div(secondOperand);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
	}
	
	@Test
	void testDivZero() {
		ComplexNumber firstOperand = new ComplexNumber(1, 1);
		assertThrows(IllegalArgumentException.class, () -> 
		firstOperand.div(new ComplexNumber(0, 0)));
	}

	@Test
	void testPowerValidExponent() {
		ComplexNumber base = new ComplexNumber(1, 1);
		int exponent = 5;
		ComplexNumber targetResult = new ComplexNumber(-4, -4);
		ComplexNumber actualResult = base.power(exponent);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		base = new ComplexNumber(0, 1);
		exponent = 4;
		targetResult = new ComplexNumber(1, 0);
		actualResult = base.power(exponent);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
		base = new ComplexNumber(2, 4);
		exponent = 0;
		targetResult = new ComplexNumber(1, 0);
		actualResult = base.power(exponent);
		assertEquals(targetResult.getReal(), actualResult.getReal(), 1E-12);
		assertEquals(targetResult.getImaginary(), actualResult.getImaginary()
					, 1E-12);
		
	}
	
	@Test
	void testPowerInvalidExponent() {
		assertThrows(IllegalArgumentException.class, () -> 
		new ComplexNumber(1, 2).power(-1));
	}

	@Test
	void testRootValidRoot() {
		ComplexNumber base = new ComplexNumber(-4, 0);
		int rootDegree = 4;
		ComplexNumber[] targetRoots = new ComplexNumber[] {
				new ComplexNumber(1, 1),
				new ComplexNumber(-1, 1),
				new ComplexNumber(-1, -1),
				new ComplexNumber(1, -1)
		};
		
		ComplexNumber[] actualRoots = base.root(rootDegree);
		assertEquals(targetRoots.length, actualRoots.length);
		for (int i = 0; i < rootDegree; i++) {
			assertEquals(targetRoots[i].getReal(), actualRoots[i].getReal()
					, 1E-12);
			assertEquals(targetRoots[i].getImaginary(), 
						actualRoots[i].getImaginary(), 1E-12);
		}
	}
	
	@Test
	void testRootInvalidRoot() {
		assertThrows(IllegalArgumentException.class, () -> 
		new ComplexNumber(1, 1).root(0));
	}

	@Test
	void testToStringPureImaginary() {
		assertEquals("1.0i", new ComplexNumber(0, 1).toString());
	}
	
	@Test
	void testToStringPureReal() {
		assertEquals("1.0", new ComplexNumber(1, 0).toString());
	}
	
	@Test
	void testToStringZero() {
		assertEquals("0.0", new ComplexNumber(0, 0).toString());
	}
	
	void testToStringRealAndImaginary() {
		assertEquals("1.0-2.0i", new ComplexNumber(1, -2).toString());
	}

}
