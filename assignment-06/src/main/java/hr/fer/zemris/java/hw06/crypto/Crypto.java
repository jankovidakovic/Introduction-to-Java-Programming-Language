package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program that allows the user to encrypt/decrypt given file using the AES
 * cryptoalgorithm and the 128-bit encryption key or calculate and check 
 * the SHA-256 file digest. Example usage:
 * 		java hr.fer.zemris.java.hw06.crypto.Crypto checksha hw06test.bin
 * 				-> program checks whether the checksum of a given file
 * 					matches the given checksum
 * 		java hr.fer.zemris.java.hw06.crypto.Crypto encrypt file encrypted_file
 * 				-> program encrypts the file using the above specified encryption
 * 					and saves the result to the encrypted_file
 * 		java hr.fer.zemris.java.hw06.crypto.Crypto decrypt file decrypted_file
 * 				-> program decrypts the file and saves the decrypted content
 * 					to the decrypted_file
 *
 * @author jankovidakovic
 *
 */
public class Crypto {

	public static void main(String[] args) throws IOException, 
	NoSuchAlgorithmException, InvalidAlgorithmParameterException,
	InvalidKeyException, NoSuchPaddingException, BadPaddingException,
	IllegalBlockSizeException {
		
		if (args.length < 1) { //wrong input
			System.out.println("Not enough arguments.");
			return;
		}
		
		if (args[0].equals("checksha")) {
			if (args.length != 2) { //wrong input
				System.out.println("Wrong number of arguments. A single "
						+ "argument, a filename, is expected after the checksha "
						+ "argument.");
				return;
			}
			//valid input
			
			//acquire target digest
			System.out.printf("Please provide expected sha-256 digest for %s:%n>", 
					args[1]);
			
			Scanner sc = new Scanner(System.in);
			String targetDigest = sc.next();
			
			//digest the input file
			
			MessageDigest digestEngine = MessageDigest.getInstance("SHA-256");
			InputStream input = new BufferedInputStream(
					Files.newInputStream(Paths.get(args[1])), 4096);
			byte[] inputBuffer = new byte[4096];
			
			//process all bytes
			while (input.available() > 0) { //read next 4 bytes
				int bytesRead = input.read(inputBuffer);
				digestEngine.update(Arrays.copyOf(inputBuffer, bytesRead)); //update the engine
			}
			
			byte[] actualDigest = digestEngine.digest(); //resulting digest
			System.out.print("Digesting completed. ");
			
			if (Util.byteToHex(actualDigest).equals(targetDigest)) { //equal
				System.out.printf("Digest of %s matches expected digest.%n", 
						args[1]);
			} else { //not equal
				System.out.printf("Digest of %s does not match the expected "
				+ "digest.%nDigest was: %s", args[1], Util.byteToHex(actualDigest));
			}
			
			sc.close();
			
		} else { //encrypt or decrypt
			boolean encrypt;
			if (args[0].equals("encrypt")) {
				encrypt = true;
			} else if (args[0].equals("decrypt")) {
				encrypt = false;
			} else { //wrong input
				System.out.println("Invalid first argument. Options are: checksha, "
						+ "encrypt or decrypt." );
				return;
			}
			if (args.length != 3) { //wrong input
				System.out.println("Wrong number of arguments.");
				return;
			}
			
			File file = new File(args[2]);
			file.createNewFile(); //create destination file
			
			Scanner sc = new Scanner(System.in);
			
			System.out.printf("Please provide password as hex-encoded text "
					+ "(16 bytes, i.e. 32 hex-digits):%n> ");
			String keyText = sc.nextLine();
			
			System.out.printf("Please provide initialization vector as "
					+ "hex-encoded text (32 hex-digits):%n> ");
			String ivText = sc.nextLine();
			
			SecretKeySpec keySpec = new SecretKeySpec(
					Util.hexToByte(keyText), "AES"
			);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(
					Util.hexToByte(ivText)
			);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : 
				Cipher.DECRYPT_MODE, keySpec, paramSpec);
			
			InputStream input = new BufferedInputStream(
					Files.newInputStream(Paths.get(args[1]))); //input file
			OutputStream output = new BufferedOutputStream(
					Files.newOutputStream(Paths.get(args[2]))); //output file
	
			byte[] inputBuffer = new byte[4096]; //buffer
			
			while (input.available() > 0) {
				int bytesRead = input.read(inputBuffer);
				output.write(cipher.update(Arrays.copyOf(inputBuffer, bytesRead)));
			}
			output.write(cipher.doFinal());
			output.flush(); //ensure that all bytes are written
			output.close(); //close the stream
			
			System.out.println((encrypt ? "Encryption" : "Decryption") + 
					" completed. Generated file " + args[2] + " based on file "
							+ args[1] + ".");
			
			sc.close();
		}
	}

}
