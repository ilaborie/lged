package org.ilaborie.pineneedles.web.model;

import java.util.List;

/**
 * The Interface Field.
 */
public interface Field {
	
	/**
	 * Gets the field name.
	 *
	 * @return the field name
	 */
	String getFieldName();
	
	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	List<Object> getValues();
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	Object getValue();
	
	/**
	 * Gets the value as string.
	 *
	 * @return the value as string
	 */
	String getValueAsString();
	
	/**
	 * Gets the values as string.
	 *
	 * @return the values as string
	 */
	List<String> getValuesAsString();
	
	/**
	 * Gets the value as number.
	 *
	 * @return the value as number
	 */
	Number getValueAsNumber();
	
	/**
	 * Gets the values as number.
	 *
	 * @return the values as number
	 */
	List<Number> getValuesAsNumber();
	

}
