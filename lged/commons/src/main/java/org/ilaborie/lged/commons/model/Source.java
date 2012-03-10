package org.ilaborie.lged.commons.model;

import java.util.List;

import org.ilaborie.search.commons.model.IIndexableElement;

/**
 * The Interface Source.
 */
public interface Source {
	
	/**
	 * Gets the shelf.
	 *
	 * @return the shelf
	 */
	Shelf getShelf();
	
	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	String getComment();

	/**
	 * Gets the by id.
	 *
	 * @param id the id
	 * @return the by id
	 */
	IIndexableElement getById(String id);

	/**
	 * Gets the all elements.
	 *
	 * @return the all elements
	 */
	List<IIndexableElement> getAllElements();

}
