package org.ilaborie.lged.commons.model;

import java.util.List;

/**
 * The Interface Shelf.
 */
public interface Shelf {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	String getId();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the sources.
	 *
	 * @return the sources
	 */
	List<Source> getSources();

}
