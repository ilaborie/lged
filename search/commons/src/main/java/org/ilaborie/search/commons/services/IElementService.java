package org.ilaborie.search.commons.services;

import org.ilaborie.search.commons.model.IIndexableElement;

/**
 * The Interface IElementService.
 */
public interface IElementService {
	
	/**
	 * Gets the by id.
	 *
	 * @param id the id
	 * @return the by id
	 */
	IIndexableElement getById(String id);

}
