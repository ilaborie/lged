package org.ilaborie.pineneedles.web.rest;

import java.util.Set;
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

import org.ilaborie.pineneedles.web.model.LinksSource;
import org.ilaborie.pineneedles.web.model.Message;
import org.ilaborie.pineneedles.web.model.entity.LinkEntry;
import org.ilaborie.pineneedles.web.model.entity.Shelf;
import org.ilaborie.pineneedles.web.model.entity.SourceLinks;
import org.slf4j.Logger;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * The Class ShelfResource.
 */
@Path("links")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Links {

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
	public Response createOrUpdate(LinksSource source) {
		logger.info("Links#createOrUpdate() : {}", this.uriInfo.getAbsolutePath());

		String id = source.getId();
		String shelfId = source.getShelfId();
		String name = source.getName();
		String description = source.getDescription();
		String links = source.getLinks();

		if (Strings.isNullOrEmpty(name)) {
			return Response
			        .status(Response.Status.BAD_REQUEST)
			        .entity(new Message("'name' parameter must not be null"))
			        .build();
		}
		if (Strings.isNullOrEmpty(links)) {
			return Response
			        .status(Response.Status.BAD_REQUEST)
			        .entity(new Message("'links' parameter must not be null"))
			        .build();
		}

		// Clean fields
		SourceLinks entity = new SourceLinks();
		entity.setName(name.trim());
		if (description != null) {
			entity.setDescription(Strings.nullToEmpty(description.trim()));
		}
		// handle links
		entity.setLinks(this.createLinks(links));

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
	 * Creates the links.
	 *
	 * @param links the links
	 * @return the list
	 */
	private Set<LinkEntry> createLinks(String links) {
		Set<LinkEntry> result = Sets.newHashSet();

		Splitter tagsSplitter = Splitter.on(',').omitEmptyStrings().trimResults();

		Set<String> tags = Sets.newHashSet();
		LinkEntry entry;

		Iterable<String> lines = Splitter.on(CharMatcher.anyOf("\n\r\f")).split(links);
		for (String line : lines) {
			if (Strings.isNullOrEmpty(line.trim())) {
				// Separator line
				tags = Sets.newHashSet();
			} else if (line.startsWith("http:") || line.startsWith("https:")) {
				// an entry
				entry = new LinkEntry();
				entry.setLink(line.trim());
				entry.setTags(tags);
				entry.setId(this.createId());
				result.add(entry);
			} else {
				// Handle tag
				tags = Sets.newHashSet(tagsSplitter.split(line));
			}
		}

		return result;
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
