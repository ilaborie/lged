package org.ilaborie.pineneedles.web.service;

import java.util.Iterator;

import org.ilaborie.pineneedles.web.model.elements.IIndexableElement;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;

/**
 * The Interface IIndexElementProvider.
 */
public interface IIndexElementProvider {

	/**
	 * Can process source.
	 *
	 * @param source the source
	 * @return true, if successful
	 */
	boolean canProcessSource(SourceEntity source);

	/**
	 * Process source.
	 *
	 * @param source the source
	 * @return the iterator
	 */
	Iterator<IIndexableElement> processSource(SourceEntity source);

	/**
	 * Update entity.
	 *
	 * @param element the element
	 * @param source the source
	 */
	void updateEntity(IIndexableElement element, SourceEntity source);

}
