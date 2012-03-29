package org.ilaborie.pineneedles.web.model.elements;


import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.service.IFieldProvider;

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
	 * @param source 
	 *
	 * @return the indexable fields
	 */
	Multimap<Field, ?> getIndexableFields(IFieldProvider fieldProvider, SourceEntity source);

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	boolean isActive();

}
