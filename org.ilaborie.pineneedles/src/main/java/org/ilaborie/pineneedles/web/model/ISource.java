package org.ilaborie.pineneedles.web.model;


/**
 * The Interface Source.
 */
public interface ISource {
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	String getId();

	/**
	 * Gets the shelf.
	 *
	 * @return the shelf
	 */
	IShelf getShelf();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	String getDescription();

}
