package org.ilaborie.lged.services.functions;

import java.util.regex.Pattern;

import com.google.common.base.Function;

/**
 * The Class StringToPattern.
 */
public class StringToPattern implements Function<String, Pattern> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public Pattern apply(String input) {
		return input != null ? Pattern.compile(input) : null;
	}

}
