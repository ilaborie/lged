package org.ilaborie.pineneedles.web.model.elements;


/**
 * The Interface Field.
 */
public interface Field {
	
	String TYPE_URL = "url";

	/**
	 * Gets the field name.
	 *
	 * @return the field name
	 */
	String getFieldName();

	/**
	 * Gets the value.
	 * @param val 
	 *
	 * @return the value
	 */
	Object getValue(String val);

}
