package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void testHextobyte() {
		byte[] targetBytes = new byte[] {1, -82, 34};
		byte[] actualBytes = Util.hexToByte("01aE22");
		assertEquals(targetBytes[0], actualBytes[0]);
		assertEquals(targetBytes[1], actualBytes[1]);
		assertEquals(targetBytes[2], actualBytes[2]);
		
		assertThrows(IllegalArgumentException.class, () -> 
		Util.hexToByte("abcdefgh"));
		assertThrows(IllegalArgumentException.class, () -> 
		Util.hexToByte("abc"));
		
	}

	@Test
	void testBytetohex() {
		byte[] bytesToHex = new byte[] {1, -82, 34};
		String targetString = "01ae22";
		String actualString = Util.byteToHex(bytesToHex);
		assertEquals(targetString, actualString);
	}

}
