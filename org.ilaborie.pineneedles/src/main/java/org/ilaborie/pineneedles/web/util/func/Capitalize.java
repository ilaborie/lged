package org.ilaborie.pineneedles.web.util.func;

import com.google.common.base.Function;
import com.google.common.base.Strings;

/**
 * The Class CapitalizeFunction.
 */
public class Capitalize implements Function<String, String> {

	/** The Constant FUNCTION. */
	public static final Capitalize FUNCTION = new Capitalize();

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(String input) {
		String result;
		if (!Strings.isNullOrEmpty(input) && input.length() > 2) {
			result = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
		} else {
			result = input;
		}
		return result;
	}

}
