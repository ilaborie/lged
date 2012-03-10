package org.ilaborie.lged.commons.service;

import java.util.List;

import org.ilaborie.lged.commons.model.Shelf;
import org.ilaborie.lged.commons.model.Source;
import org.ilaborie.search.commons.model.IIndexableElement;

/**
 * The Interface ISourceService.
 */
public interface ISourceService {

	/**
	 * Find sources by shelf.
	 *
	 * @param shelf the shelf
	 * @return the list<? extends source>
	 */
	List<? extends Source> findSourcesByShelf(Shelf shelf);

	/**
	 * Fetch.
	 *
	 * @param source the source
	 * @return the list
	 */
	List<IIndexableElement> fetch(Source source);

}
