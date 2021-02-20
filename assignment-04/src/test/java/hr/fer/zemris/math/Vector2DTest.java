package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector2DTest {

	@Test
	void testVector2D() {
		Vector2D vector = new Vector2D(1, 0);
		//pass
	}

	@Test
	void testGetX() {
		Vector2D vector = new Vector2D(1, 0);
		assertEquals(1, vector.getX(), 1E-12);
	}

	@Test
	void testGetY() {
		Vector2D vector = new Vector2D(1, 0);
		assertEquals(0, vector.getY(), 1E-12);
	}

	@Test
	void testTranslate() {
		Vector2D vector1 = new Vector2D(1, 0);
		Vector2D vector2 = new Vector2D(0, 1);
		vector1.translate(vector2);
		assertEquals(1, vector1.getX(), 1E-12);
		assertEquals(1, vector1.getY(), 1E-12);
		
	}

	@Test
	void testTranslated() {
		Vector2D vector1 = new Vector2D(1, 0);
		Vector2D vector2 = vector1.translated(new Vector2D(0, 1));
		assertEquals(1, vector2.getX(), 1E-12);
		assertEquals(1, vector2.getY(), 1E-12);
	}

	@Test
	void testRotate() {
		Vector2D vector1 = new Vector2D(1, 0);
		vector1.rotate(Math.PI / 2);
		assertEquals(0, vector1.getX(), 1E-12);
		assertEquals(1, vector1.getY(), 1E-12);
		
	}

	@Test
	void testRotated() {
		Vector2D vector1 = new Vector2D(1, 0);
		Vector2D vector2 = vector1.rotated(Math.PI / 2);
		assertEquals(0, vector2.getX(), 1E-12);
		assertEquals(1, vector2.getY(), 1E-12);
	}

	@Test
	void testScale() {
		Vector2D vector1 = new Vector2D(1, 1);
		vector1.scale(2);
		assertEquals(2, vector1.getX(), 1E-12);
		assertEquals(2, vector1.getY(), 1E-12);
		
	}

	@Test
	void testScaled() {
		Vector2D vector1 = new Vector2D(1, 1);
		Vector2D vector2 = vector1.scaled(-2);
		assertEquals(-2, vector2.getX(), 1E-12);
		assertEquals(-2, vector2.getY(), 1E-12);
	}

	@Test
	void testCopy() {
		Vector2D vector1 = new Vector2D(1, 1);
		Vector2D vector2 = vector1.copy();
		assertEquals(1, vector2.getX(), 1E-12);
		assertEquals(1, vector2.getY(), 1E-12);
	}

}
