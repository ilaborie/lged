package org.ilaborie.pineneedles.web.rest;

import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.Shelf;
import org.ilaborie.pineneedles.web.model.entity.ShelfEntity;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.util.ResponseBuilder;
import org.slf4j.Logger;

import com.google.common.base.Strings;
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
	public Response findAll() {
		logger.info("Shelves#FindAll() : {}", this.uriInfo.getAbsolutePath());

		TypedQuery<ShelfEntity> query = this.em.createNamedQuery(ShelfEntity.QUERY_FIND_ALL, ShelfEntity.class);
		return ResponseBuilder.ok(Lists.newArrayList(query.getResultList()));
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the shelf
	 */
	@GET
	@Path("{id}")
	public Response findById(@PathParam("id") String id) {
		logger.info("Shelves#FindById() : {}", this.uriInfo.getAbsolutePath());
		ShelfEntity shelf = this.em.find(ShelfEntity.class, id);

		Response response;
		if (shelf == null) {
			response = ResponseBuilder.emptyResult();
		} else {
			response = ResponseBuilder.ok(shelf);
		}
		return response;
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the shelf
	 */
	@DELETE
	@Path("{id}")
	public Response deleteById(@PathParam("id") String id) {
		logger.info("Shelves#deleteById() : {}", this.uriInfo.getAbsolutePath());

		try {
			ShelfEntity entity = this.em.find(ShelfEntity.class, id);
			if (entity == null) {
				return ResponseBuilder.notFound("shelf", id);
			}

			// Delete all sources
			Query query = this.em.createNamedQuery(SourceEntity.QUERY_DELETE_BY_SHELF);
			query.setParameter("shelf", entity);
			query.executeUpdate();

			// Delete shelf
			this.em.remove(entity);
			return ResponseBuilder.deleted("Shelf ", id);
		} catch (Exception e) {
			return ResponseBuilder.fail(e);
		}
	}

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 * @return the shelf
	 */
	@PUT
	public Response createOrUpdate(Shelf shelf) {
		logger.info("Shelves#create() : {}", this.uriInfo.getAbsolutePath());
		String name = shelf.getName();
		String description = shelf.getDescription();

		if (Strings.isNullOrEmpty(name)) {
			return ResponseBuilder.nullArgument("name");
		}

		// Clean fields
		ShelfEntity entity = new ShelfEntity();
		entity.setName(name.trim());
		if (description != null) {
			entity.setDescription(Strings.nullToEmpty(description.trim()));
		}

		try {
			Response response;
			if (entity.getId() == null) {
				// Create
				entity.setId(this.createId());
				this.em.persist(entity);
				response = ResponseBuilder.created(entity);
			} else {
				// Update
				this.em.merge(entity);
				response = Response.ok(entity).build();
			}
			return response;
		} catch (Exception e) {
			return ResponseBuilder.fail(e);
		}
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
