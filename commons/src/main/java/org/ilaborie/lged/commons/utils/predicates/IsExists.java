package org.ilaborie.lged.commons.utils.predicates;

import java.io.File;

import com.google.common.base.Predicate;

public class IsExists implements Predicate<File> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(File file) {
		return file != null && file.exists();
	}

}
