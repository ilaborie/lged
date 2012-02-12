package org.ilaborie.search.commons.utils.predicates;

import java.io.File;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * The Class FilesPredicates.
 */
@SuppressWarnings("unchecked")
public final class FilesPredicates {

	/** The Constant CLASSICS_FILES. */
	private final static Predicate<File> CLASSICS_FILES = Predicates.and(
			new CanRead(), new IsFile(), new IsExists(),
			Predicates.not(new IsDotFile()));
	

	/** The Constant CLASSICS_DIRECTORIES. */
	private final static Predicate<File> CLASSICS_DIRECTORIES = Predicates.and(
			new CanRead(), new IsDirectory(), new IsExists(),
			Predicates.not(new IsDotFile()));

	/**
	 * Classics files.
	 *
	 * @return the predicate
	 */
	public static Predicate<File> classicsFiles() {
		return CLASSICS_FILES;
	}

	/**
	 * Classics directories.
	 *
	 * @return the predicate
	 */
	public static Predicate<File> classicsDirectories() {
		return CLASSICS_DIRECTORIES;
	}

	/**
	 * Instantiates a new files predicates.
	 */
	private FilesPredicates() {
		super();
	}

}
