package hr.fer.zemris.java.hw06.crypto;

/**
 * Class which provides static methods for conversion between array of bytes
 * and its hexadecimal representation in the form of a string.
 *
 * @author jankovidakovic
 *
 */
public class Util {

	/**
	 * Converts a given hexadecimal string to the array of bytes.
	 *
	 * @param keyText Hexadecimal string. Such consists of even number of 
	 * characters, where two characters represent one hexadecimal number
	 * which will be converted to a byte. Both lowercase and uppercase
	 * letters are supported. As one byte is created from two characters,
	 * string must be of even length. String must consist only of valid 
	 * hexadecimal digits.
	 * @return array of bytes which represents the given string.
	 * @throws IllegalArgumentException if given string is of odd length,
	 * or contains illegal hexadecimal digits.
	 */
	public static byte[] hexToByte(String keyText) {
		if (keyText.length() % 2 == 1) {
			throw new IllegalArgumentException("String must contain even number"
					+ "of hex digits!");
		}
		byte[] byteArray = new byte[keyText.length() / 2];
		for (int i = 0; i < keyText.length(); i+=2) {
			byteArray[i/2] = parseByte(keyText.substring(i, i+2));
		}
		return byteArray;
	}
	
	/**
	 * Converts a given hex string to a single byte.
	 *
	 * @param hex Hex representation of a single byte, in the form of a string
	 * @return a single byte, equivalent to the given string
	 */
	private static byte parseByte(String hex) {
		if (!isHexDigit(hex.charAt(0)) || !isHexDigit(hex.charAt(1))) {
			throw new IllegalArgumentException("Illegal hex digit!");
		}
		byte resultByte = 0;
		resultByte += Character.digit(hex.charAt(0), 16) << 4; //leading digit
		resultByte += Character.digit(hex.charAt(1), 16); //trailing digit
		return resultByte;
	}
	
	/**
	 * Converts a given array of bytes to a hexadecimal representation, stored
	 * in a string. Resulting string will have lowercase hexadecimal digits.
	 *
	 * @param keyText array of bytes to convert 
	 * @return hexadecimal representation of bytes, stored in a string
	 */
	public static String byteToHex(byte[] keyText) {
		StringBuilder sb = new StringBuilder();
		for (byte b : keyText) {
			int currentByte = (int) b & 0xff;
			String currentByteString = Integer.toHexString(currentByte);
			if (currentByteString.length() == 1) {
				currentByteString = "0" + currentByteString;
			}
			sb.append(currentByteString);
		}
		return sb.toString();
	}
	
	private static boolean isHexDigit(char ch) {
		return "0123456789abcdef".indexOf(Character.toLowerCase(ch)) != -1;
	}
}
