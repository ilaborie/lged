package org.ilaborie.pineneedles.web.rest;

import java.io.File;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.Folder;
import org.ilaborie.pineneedles.web.model.Message;
import org.ilaborie.pineneedles.web.model.entity.Shelf;
import org.ilaborie.pineneedles.web.model.entity.SourceFolder;
import org.slf4j.Logger;

import com.google.common.base.Strings;

/**
 * The Class ShelfResource.
 */
@Path("folders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Folders {

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The entity manager. */
	@Inject
	private EntityManager em;

	/** The logger. */
	@Inject
	private Logger logger;

	/**
	 * Creates the.
	 *
	 * @param entity the entity
	 * @return the shelf
	 */
	@PUT
	public Response createOrUpdate(Folder folder) {
		logger.info("Folders#createOrUpdate() : {}", this.uriInfo.getAbsolutePath());

		String id = folder.getId();
		String shelfId = folder.getShelfId();
		String name = folder.getName();
		String description = folder.getDescription();
		String path = folder.getPath();
		boolean recursive = folder.isRecursive();

		if (Strings.isNullOrEmpty(name)) {
			return Response
			        .status(Response.Status.BAD_REQUEST)
			        .entity(new Message("'name' parameter must not be null"))
			        .build();
		}
		if (Strings.isNullOrEmpty(path)) {
			return Response
			        .status(Response.Status.BAD_REQUEST)
			        .entity(new Message("'path' parameter must not be null"))
			        .build();
		}
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return Response
			        .status(Response.Status.BAD_REQUEST)
			        .entity(new Message("Folder does not exists: " + file))
			        .build();
		}

		// Clean fields
		SourceFolder entity = new SourceFolder();
		entity.setName(name.trim());
		if (description != null) {
			entity.setDescription(Strings.nullToEmpty(description.trim()));
		}
		entity.setRecursive(recursive);
		entity.setFolder(file.getAbsolutePath());

		try {
			Shelf shelf = this.em.find(Shelf.class, shelfId);
			if (shelf == null) {
				return Response.status(Status.BAD_REQUEST)
				        .entity(new Message("Could not find the shelf: " + shelfId))
				        .build();
			}

			entity.setShelf(shelf);

			Response response;
			if (id == null) {
				// Create
				entity.setId(this.createId());
				this.em.persist(entity);
				response = Response.status(Status.CREATED).entity(entity).build();
			} else {
				// Update
				entity.setId(id);
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
