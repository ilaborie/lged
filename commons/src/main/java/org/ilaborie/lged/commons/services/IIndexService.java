package org.ilaborie.lged.commons.services;

import java.util.Collection;

import org.ilaborie.lged.commons.model.IIndexableElement;

/**
 * The Interface ISolrIndexService.
 */
public interface IIndexService {

	/**
	 * Index.
	 * Commit if successful.
	 * @param elements the elements
	 * @return true, if successful
	 */
	boolean index(IIndexableElement... elements);

	/**
	 * Index all.
	 * Commit if successful.
	 * @param elements the elements
	 * @return true, if successful
	 */
	boolean indexAll(Collection<? extends IIndexableElement> elements);

	/**
	 * Delete documents.
	 * Commit if successful.
	 * @param ids documents identifier
	 * @return true, if successful
	 */
	boolean delete(String... ids);

	/**
	 * Delete documents.
	 * Commit if successful.
	 * @param ids the documents identifier
	 * @return true, if successful
	 */
	boolean delete(Collection<String> ids);
	
	/**
	 * Clear the index.
	 *
	 * @return true, if successful
	 */
	boolean clear();
	
	/**
	 * Optimize the index.
	 *
	 * @return true, if successful
	 */
	boolean optimize();

}
