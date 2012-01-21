package org.ilaborie.lged.commons.utils.predicates;

import java.io.File;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * The Class IsExtension.
 */
public class IsExtension implements Predicate<File> {

	/** The extension. */
	private final String extension;
	
	/**
	 * Instantiates a new checks if is extension.
	 *
	 * @param extension the extension
	 */
	public IsExtension(String extension) {
		super();
		Preconditions.checkNotNull(extension);
		if (!extension.startsWith(".")) {
			this.extension = "." + extension;
		} else {
			this.extension = extension;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(File file) {
		return file != null && file.getName().endsWith(extension);
	}

}
