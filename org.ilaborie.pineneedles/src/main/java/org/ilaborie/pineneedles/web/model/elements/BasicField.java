package org.ilaborie.pineneedles.web.model.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.ilaborie.pineneedles.web.util.DateUtil;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

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

	/** The TEASER. */
	TEASER(String.class),

	/** The LOCATION. */
	LOCATION(String.class),

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

	/** The singleton. */
	private static Function<Object, List<Object>> singleton = new Function<Object, List<Object>>() {
		@Override
		public List<Object> apply(Object input) {
			return input != null ? Collections.singletonList(input) : Collections.emptyList();
		}
	};

	/** The splitter. */
	private static Function<Object, List<Object>> splitter = new Function<Object, List<Object>>() {
		@Override
		public List<Object> apply(Object input) {
			List<Object> result = new ArrayList<Object>();
			if (input != null) {
				Iterable<String> split = Splitter.on(",").trimResults().omitEmptyStrings().split((String) input);
				for (String s : split) {
					result.add(s);
				}
			}
			return result;
		}
	};

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
				try {
					result = DateUtil.parse(val);
				} catch (IllegalArgumentException e) {
					// Do nothing
				}
			} else {
				throw new RuntimeException("Unsupported type :" + this.clazz.getName());
			}
		}
		return result;
	}

	/**
	 * Gets the values function.
	 *
	 * @return the values function
	 */
	private Function<Object, List<Object>> getValuesFunction() {
		switch (this) {
			case TAG:
				return splitter;
			default:
				return singleton;
		}
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.model.elements.Field#getValues(java.lang.String)
	 */
	@Override
	public Collection<Object> getValues(String value) {
		Object val = this.getValue(value);
		return this.getValuesFunction().apply(val);
	}

}
