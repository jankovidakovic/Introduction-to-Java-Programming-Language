package hr.fer.zemris.java.blogapp.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Manager for the application security, offering methods to work with
 * passwords.
 * 
 * @author jankovidakovic
 *
 */
public class SecurityManager {

	/**
	 * Hashes a given password string using a given hashing algorithm. For the
	 * purposes of this application, SHA-1 algorithm is used, but the
	 * functionality can be easily extended to any other, more secure hashing
	 * algorithms.
	 * 
	 * @param  password                 password to hash
	 * @param  hashAlgorithm            hashing algorithm to use
	 * @return                          hashed password, in hex representation
	 * @throws NoSuchAlgorithmException if given hashing algorithm does not
	 *                                  exist in the JVM
	 */
	public static String getPasswordHash(String password,
			String hashAlgorithm) {
		MessageDigest digestEngine = null;
		try {
			digestEngine = MessageDigest.getInstance(hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error - cannot find given algorithm.");
		}

		byte[] hashBytes = digestEngine
				.digest(password.getBytes(Charset.forName("UTF-8")));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hashBytes.length; i++) {
			// &0xff - deletes all but the lower two hex digits
			// +0x100 - creates leading zeroes if neccessary
			// .substring(1) - takes only the last two digits in the
			// consideration, since the created string has length 3
			sb.append(Integer
					.toString((hashBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}
}
