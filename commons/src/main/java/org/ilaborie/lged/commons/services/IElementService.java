package org.ilaborie.lged.commons.services;

import org.ilaborie.lged.commons.model.IIndexableElement;

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
