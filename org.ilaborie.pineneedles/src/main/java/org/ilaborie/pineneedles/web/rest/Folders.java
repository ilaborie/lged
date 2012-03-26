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

import org.ilaborie.pineneedles.web.model.FolderSource;
import org.ilaborie.pineneedles.web.model.entity.ShelfEntity;
import org.ilaborie.pineneedles.web.model.entity.FolderSourceEntity;
import org.ilaborie.pineneedles.web.util.ResponseBuilder;
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
	public Response createOrUpdate(FolderSource folder) {
		logger.info("Folders#createOrUpdate() : {}", this.uriInfo.getAbsolutePath());

		String id = folder.getId();
		String shelfId = folder.getShelfId();
		String name = folder.getName();
		String description = folder.getDescription();
		String path = folder.getPath();
		boolean recursive = folder.isRecursive();

		if (Strings.isNullOrEmpty(name)) {
			return ResponseBuilder.nullArgument("name");
		}
		if (Strings.isNullOrEmpty(path)) {
			return ResponseBuilder.nullArgument("path");
		}
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return ResponseBuilder.notFound("folder", path);
		}

		// Clean fields
		FolderSourceEntity entity = new FolderSourceEntity();
		entity.setName(name.trim());
		if (description != null) {
			entity.setDescription(Strings.nullToEmpty(description.trim()));
		}
		entity.setRecursive(recursive);
		entity.setFolder(file.getAbsolutePath());

		try {
			ShelfEntity shelf = this.em.find(ShelfEntity.class, shelfId);
			if (shelf == null) {
				return ResponseBuilder.notFound("shelf", shelfId);
			}

			entity.setShelf(shelf);

			Response response;
			if (id == null) {
				// Create
				entity.setId(this.createId());
				this.em.persist(entity);
				response = ResponseBuilder.created(entity);
			} else {
				// Update
				entity.setId(id);
				this.em.merge(entity);
				response = ResponseBuilder.ok(entity);
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
