package org.ilaborie.lged.commons.service;

import java.util.List;

import org.ilaborie.lged.commons.model.Shelf;

/**
 * The Interface IShelfService.
 */
public interface IShelfService {

	/**
	 * Creates the shelf.
	 *
	 * @param shelf the shelf
	 * @return the shelf
	 */
	Shelf createShelf(Shelf shelf);

	/**
	 * Update shelf.
	 *
	 * @param shelf the shelf
	 * @return the shelf
	 */
	Shelf updateShelf(Shelf shelf);

	/**
	 * Gets the shelf by id.
	 *
	 * @param id the id
	 * @return the shelf by id
	 */
	Shelf getShelfById(String id);

	/**
	 * Gets the all shelves.
	 *
	 * @return the all shelves
	 */
	List<? extends Shelf> getAllShelves();

	/**
	 * Delete shelf.
	 *
	 * @param shelf the shelf
	 */
	void deleteShelf(Shelf shelf);

}
