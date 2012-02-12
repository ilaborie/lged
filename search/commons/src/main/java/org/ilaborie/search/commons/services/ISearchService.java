package org.ilaborie.search.commons.services;

import java.util.List;

import org.ilaborie.search.commons.model.IIndexableElement;

/**
 * The Interface ISolrSearch.
 */
public interface ISearchService {
	
	/**
	 * Gets all documents.
	 *
	 * @return all documents
	 */
	List<IIndexableElement> getAll();
	
	/**
	 * Gets first documents.
	 *
	 * @param number the number of wished documents
	 * @return first documents
	 */
	List<IIndexableElement> getFirst(int number);
	
	/**
	 * Search full text.
	 *
	 * @param text the text
	 * @param number the number
	 * @return the list
	 */
	List<IIndexableElement> searchFullText(String text, int number);
	
	/**
	 * Search term.
	 *
	 * @param startsWith the starts with
	 * @param max the max
	 * @return the list of term
	 */
	List<String> searchTerms(String startsWith, int max);
	
	/**
	 * Count indexed documents.
	 *
	 * @return the number of indexed documents
	 */
	long countDocuments();

}
