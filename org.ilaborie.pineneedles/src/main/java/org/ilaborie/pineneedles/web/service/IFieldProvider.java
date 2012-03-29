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

	// PineNeedles fields
	
	/**
	 * Gets the full text field.
	 *
	 * @return the full text field
	 */
	Field getFullTextField();

	/**
	 * Gets the tag field.
	 *
	 * @return the tag field
	 */
	Field getTagField();

	/**
	 * Gets the source field.
	 *
	 * @return the source field
	 */
	Field getSourceField();

	/**
	 * Gets the shelf field.
	 *
	 * @return the shelf field
	 */
	Field getShelfField();
	
	/**
	 * Gets the checksum field.
	 *
	 * @return the checksum field
	 */
	Field getChecksumField();
	
	/**
	 * Gets the type field.
	 *
	 * @return the type field
	 */
	Field getTypeField();

}
