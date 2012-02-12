package org.ilaborie.search.commons.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * The Class ShaUtils. Utility to compute SHA-1
 */
public final class ShaUtils {

	/**
	 * Instantiates a new sha utils.
	 */
	private ShaUtils() {
		super();
	}

	/**
	 * SHA.
	 * 
	 * @param s
	 *            the s
	 * @return the string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String sha(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			byte[] bytes = s.getBytes(Charsets.UTF_8);
			return byteArray2Hex(md.digest(bytes));
		} catch (Exception e) {
			throw new RuntimeException("Fail to SHA-1 with String: " + s, e);
		}
	}

	/**
	 * SHA-1 on file.
	 * 
	 * @param file
	 *            the file
	 * @return the string
	 */
	public static String sha(File file) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return byteArray2Hex(Files.getDigest(file, md));
		} catch (Exception e) {
			throw new RuntimeException("Fail to SHA-1 with file: " + file, e);
		}
	}

	/**
	 * Byte array2 hex.
	 * 
	 * @param hash
	 *            the hash
	 * @return the string
	 */
	private static String byteArray2Hex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

}
