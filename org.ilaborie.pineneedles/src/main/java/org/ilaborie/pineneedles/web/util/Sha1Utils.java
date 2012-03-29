package org.ilaborie.pineneedles.web.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * The Class Sha1Utils.
 */
public final class Sha1Utils {
	
	/** The Constant HASHING. */
	private static final HashFunction HASHING = Hashing.sha1();
	
	/**
	 * Instantiates a new sha1 utils.
	 */
	private Sha1Utils() {
		super();
    }
	
	/**
	 * Sha1.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String sha1(String string) {
		  // Create a SHA-1 Hasher
        Hasher hasher = HASHING.newHasher();
        hasher.putString(string);
        
        return hasher.hash().toString();
	}

}
