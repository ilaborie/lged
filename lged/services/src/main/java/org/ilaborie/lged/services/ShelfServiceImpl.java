package org.ilaborie.lged.services;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.ilaborie.lged.commons.model.Shelf;
import org.ilaborie.lged.commons.service.IShelfService;
import org.ilaborie.lged.services.entity.ShelfEntity;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * The Class ShelfServiceImpl.
 */
@Stateless
public class ShelfServiceImpl implements IShelfService {

	/** The log. */
	@Inject
	private Logger log;

	/** The entity manager. */
	@PersistenceContext
	private EntityManager em;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.service.IShelfService#createShelf(org.ilaborie .lged.commons.model.Shelf)
	 */
	@Override
	public Shelf createShelf(Shelf shelf) {
		Preconditions.checkNotNull(shelf);

		// Mapping to entity
		ShelfEntity entity = new ShelfEntity();
		entity.setName(shelf.getName());

		// Create the id
		String uuid = UUID.randomUUID().toString();
		entity.setUuid(uuid);

		// Persist
		this.em.persist(entity);
		this.log.debug("Shelf created: {}", shelf);
		assert shelf.getId() != null;
		return shelf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.service.IShelfService#updateShelf()
	 */
	@Override
	public Shelf updateShelf(Shelf shelf) {
		Preconditions.checkNotNull(shelf);
		this.em.merge(shelf);
		this.log.debug("Shelf updated: {}", shelf);
		assert shelf.getId() != null;
		return shelf;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.lged.commons.service.IShelfService#getShelfById(java.lang.String)
	 */
	@Override
	public Shelf getShelfById(String id) {
		Preconditions.checkNotNull(id);
		ShelfEntity shelf = this.em.find(ShelfEntity.class, id);
		this.log.trace("Get shelf: {}", shelf);
		return shelf;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.lged.commons.service.IShelfService#getAllShelves()
	 */
	@Override
	public List<? extends Shelf> getAllShelves() {
		TypedQuery<ShelfEntity> query = this.em.createNamedQuery(ShelfEntity.QUERY_FIND_ALL, ShelfEntity.class);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.service.IShelfService#deleteShelf(org.ilaborie .lged.commons.model.Shelf)
	 */
	@Override
	public void deleteShelf(Shelf shelf) {
		Preconditions.checkNotNull(shelf);
		this.em.remove(shelf);
		this.log.debug("Shelf deleted: {}", shelf);
	}

}
