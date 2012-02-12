package org.ilaborie.search.commons.utils.predicates;

import java.io.File;

import com.google.common.base.Predicate;

public class IsDotFile implements Predicate<File> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(File file) {
		return file != null && file.getName().startsWith(".");
	}

}
