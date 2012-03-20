package org.ilaborie.pineneedles.web.model;

import java.io.File;

import com.google.common.collect.Multimap;

/**
 * The Interface IIndexableElement.
 * 
 */
public interface IIndexableElement {

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	String getId();

	/**
	 * Gets the indexable fields.
	 *
	 * @return the indexable fields
	 */
	Multimap<Field, ?> getIndexableFields();

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	File getFile();

}
