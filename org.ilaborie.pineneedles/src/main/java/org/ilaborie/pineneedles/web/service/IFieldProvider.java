/*
 * 
 */
package org.ilaborie.pineneedles.web.service;

import org.ilaborie.pineneedles.web.model.elements.Field;

/**
 * The Interface IFieldProvider.
 */
public interface IFieldProvider {

	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	Iterable<Field> getFields();

	/**
	 * Gets the field by metadata.
	 *
	 * @param name the name
	 * @return the field by metadata
	 */
	Field getFieldByMetadata(String name);

}
