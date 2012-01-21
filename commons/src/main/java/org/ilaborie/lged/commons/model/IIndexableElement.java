package org.ilaborie.lged.commons.model;

import java.io.File;

import org.ilaborie.lged.commons.utils.Fields;

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
	Multimap<Fields, ?> getIndexableFields();
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	File getFile();
	
}
