package org.ilaborie.pineneedles.web.rest;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.entity.Shelf;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

/**
 * The Class Shelves.
 */
@Path("shelves")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Shelves {

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The entity manager. */
	@Inject
	private EntityManager em;

	@Inject
	private Logger logger;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@GET
	public List<Shelf> findAll() {
		logger.info("Shelves#FindAll() : {}", this.uriInfo.getAbsolutePath());

		TypedQuery<Shelf> query = this.em.createNamedQuery(Shelf.QUERY_FIND_ALL, Shelf.class);
		return Lists.newArrayList(query.getResultList());
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the shelf
	 */
	@GET
	@Path("{id}")
	public Shelf findById(@PathParam("id") String id) {
		logger.info("Shelves#FindById() : {}", this.uriInfo.getAbsolutePath());
		return this.em.find(Shelf.class, id);
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the shelf
	 */
	@DELETE
	@Path("{id}")
	public void deleteById(@PathParam("id") String id) {
		logger.info("Shelves#deleteById() : {}", this.uriInfo.getAbsolutePath());
		Shelf entity = this.em.find(Shelf.class, id);
		this.em.remove(entity);
	}

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 * @return the shelf
	 */
	@PUT
	public Shelf createOrUpdate(Shelf entity) {
		logger.info("Shelves#create() : {}", this.uriInfo.getAbsolutePath());

		if (entity.getId() == null) {
			// Create
			entity.setId(this.createId());
			this.em.persist(entity);
		} else {
			// Update
			this.em.merge(entity);
		}

		return entity;
	}

	/**
	 * Creates the id.
	 *
	 * @return the string
	 */
	private String createId() {
		return UUID.randomUUID().toString();
	}

}
