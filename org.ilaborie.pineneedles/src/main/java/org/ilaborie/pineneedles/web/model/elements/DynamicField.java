package org.ilaborie.pineneedles.web.model.elements;

import java.util.Collection;
import java.util.Collections;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * The Class DynamicField.
 */
public class DynamicField implements Field {

	/** The Constant PREFIX. */
	public static final String PREFIX = "dyn-";

	/** The name. */
	private final String name;

	/**
	 * Instantiates a new dynamic field.
	 *
	 * @param name the name
	 */
	public DynamicField(String name) {
		super();
		this.name = PREFIX + name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		Hasher hasher = Hashing.sha1().newHasher();
		return hasher.putString(this.name).hash().asInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass().equals(this.getClass())) {
			return this.name.equals(((DynamicField) obj).name);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.Field#getFieldName()
	 */
	@Override
	public String getFieldName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.Field#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String val) {
		return val;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.model.elements.Field#getValues(java.lang.String)
	 */
	@Override
	public Collection<Object> getValues(String value) {
		return Collections.singletonList(this.getValue(value));
	}

}
