package org.ilaborie.pineneedles.web.model.elements;

import java.util.Date;

import org.ilaborie.pineneedles.web.util.DateUtil;

/**
 * The Enum BasicField.
 */
public enum BasicField implements Field {

	/** The TITLE. */
	TITLE(String.class),

	/** The AUTHOR. */
	AUTHOR(String.class),

	/** The FUL l_ text. */
	FULL_TEXT(String.class),

	/** The CREAT e_ date. */
	CREATE_DATE(Date.class),

	/** The TAG. */
	TAG(String.class),

	/** The SHELF. */
	SHELF(String.class),

	/** The SOURCE. */
	SOURCE(String.class),

	/** The TYPE. */
	TYPE(String.class),

	/** The Checksum. */
	CHECKSUM(String.class);

	/** The class. */
	private Class<?> clazz;

	/**
	 * Instantiates a new basic field.
	 *
	 * @param clazz the class
	 */
	private BasicField(Class<?> clazz) {
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.Field#getFieldName()
	 */
	@Override
	public String getFieldName() {
		return this.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.Field#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String val) {
		Object result = null;
		if (val != null) {
			if (String.class.equals(this.clazz)) {
				result = val;
			} else if (Date.class.equals(this.clazz)) {
				result = DateUtil.parse(val);
			} else {
				throw new RuntimeException("Unsupported type :" + this.clazz.getName());
			}
		}
		return result;
	}
}
