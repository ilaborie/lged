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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.entity.Shelf;
import org.ilaborie.pineneedles.web.model.entity.Source;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

/**
 * The Class ShelfResource.
 */
@Path("sources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Sources {

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
	public List<Source> findByShelf(@QueryParam("shelf") String shelfId) {
		logger.info("Sources#FindAll() : {}", this.uriInfo.getAbsolutePath());

		TypedQuery<Source> query = this.em.createNamedQuery(Source.QUERY_FIND_BY_SHELF, Source.class);
		Shelf shelf = this.em.find(Shelf.class, shelfId);
		query.setParameter("shelf", shelf);

		return Lists.newArrayList(query.getResultList());

	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the source
	 */
	@GET
	@Path("{id}")
	public Source findById(@PathParam("id") String id) {
		logger.info("Sources#FindById() : {}", this.uriInfo.getAbsolutePath());
		return this.em.find(Source.class, id);
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
		logger.info("Sources#deleteById() : {}", this.uriInfo.getAbsolutePath());
		Source entity = this.em.find(Source.class, id);
		this.em.remove(entity);
	}

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 * @return the shelf
	 */
	@PUT
	@Path("{shelfId}")
	public Source createOrUpdate(@PathParam("shelfId") String shelfId, Source entity) {
		logger.info("Sources#createOrUpdate() : {}", this.uriInfo.getAbsolutePath());

		Shelf shelf = this.em.find(Shelf.class, shelfId);
		if (entity.getId() == null) {
			// Create
			entity.setId(this.createId());
			entity.setShelf(shelf);
			this.em.persist(entity);
		} else {
			// Update
			entity.setShelf(shelf);
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
