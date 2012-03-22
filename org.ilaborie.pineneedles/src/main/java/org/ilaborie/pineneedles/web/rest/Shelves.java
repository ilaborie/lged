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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.Message;
import org.ilaborie.pineneedles.web.model.entity.Shelf;
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
	public Response findById(@PathParam("id") String id) {
		logger.info("Shelves#FindById() : {}", this.uriInfo.getAbsolutePath());
		Shelf shelf = this.em.find(Shelf.class, id);

		Response response;
		if (shelf == null) {
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = Response.ok(shelf).build();
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
			Shelf entity = this.em.find(Shelf.class, id);
			if (entity == null) {
				return Response.status(Status.BAD_REQUEST)
				        .entity( new Message("Could not find the shelf: " + id)).build();
			}

			this.em.remove(entity);
			return Response.ok( new Message("Shelf deleted: " + id)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}


	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 * @return the shelf
	 */
	@PUT
	public Response createOrUpdate(Shelf entity) {
		logger.info("Shelves#create() : {}", this.uriInfo.getAbsolutePath());
		String name = entity.getName();
		String description = entity.getDescription();

		if (Strings.isNullOrEmpty(name)) {
			return Response
			        .status(Response.Status.BAD_REQUEST)
			        .entity( new Message("'name' parameter must not be null"))
			        .build();
		}

		// Clean fields
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
				response = Response.status(Status.CREATED).entity(entity).build();
			} else {
				// Update
				this.em.merge(entity);
				response = Response.ok(entity).build();
			}
			return response;
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
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
