package org.ilaborie.pineneedles.web.model.elements;

import java.util.Collection;


/**
 * The Interface Field.
 */
public interface Field {
	
	/** The TYP e_ url. */
	String TYPE_URL = "url";
	
	/** The TEASE r_ length. */
	int TEASER_LENGTH= 256;

	/**
	 * Gets the field name.
	 *
	 * @return the field name
	 */
	String getFieldName();

	/**
	 * Gets the value.
	 *
	 * @param val the val
	 * @return the value
	 */
	Object getValue(String val);

	/**
	 * Gets the values.
	 *
	 * @param value the value
	 * @return the values
	 */
	Collection<Object> getValues(String value);

}
