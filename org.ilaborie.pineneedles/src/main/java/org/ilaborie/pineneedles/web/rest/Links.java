package org.ilaborie.pineneedles.web.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.Link;
import org.ilaborie.pineneedles.web.model.LinksSource;
import org.ilaborie.pineneedles.web.model.entity.LinkEntry;
import org.ilaborie.pineneedles.web.model.entity.ShelfEntity;
import org.ilaborie.pineneedles.web.model.entity.LinksSourceEntity;
import org.ilaborie.pineneedles.web.util.ResponseBuilder;
import org.ilaborie.pineneedles.web.util.func.Capitalize;
import org.slf4j.Logger;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * The Class ShelfResource.
 */
@Path("links")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Links {

	/** The Constant URL_SPLITTER. */
	private static final Splitter URL_SPLITTER = Splitter.on("/").omitEmptyStrings().trimResults();

	/** The Constant TAGS_SPLITTER. */
	private static final Splitter TAGS_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

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
			return ResponseBuilder.nullArgument("name");
		}
		if (Strings.isNullOrEmpty(links)) {
			return ResponseBuilder.nullArgument("links");
		}

		// Clean fields
		LinksSourceEntity entity = new LinksSourceEntity();
		entity.setName(name.trim());
		if (description != null) {
			entity.setDescription(Strings.nullToEmpty(description.trim()));
		}
		// handle links
		entity.setLinks(this.createLinks(links));

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
			return ResponseBuilder.fail(e);
		}
	}

	/**
	 * Adds the link.
	 *
	 * @param link the link
	 * @return the response
	 */
	@PUT
	@Path("link")
	public Response addLink(Link link) {
		logger.info("Links#addLink() : {}", this.uriInfo.getAbsolutePath());

		String id = link.getId();
		String sourceId = link.getSourceId();
		String lnk = link.getLink();
		String tags = link.getTags();

		if (Strings.isNullOrEmpty(lnk)) {
			return ResponseBuilder.nullArgument("name");
		}
		try {
			new URL(lnk);
		} catch (MalformedURLException e1) {
			return ResponseBuilder.nullArgument("name");
		}

		// Clean fields
		LinkEntry entity = new LinkEntry();
		entity.setActive(true);
		entity.setId(id);
		entity.setLink(lnk);
		entity.setHost(this.extractHost(lnk));
		entity.setDate(this.extractDate(lnk));
		entity.setTitle(this.extractTitle(lnk));

		entity.setTags(Sets.newHashSet(TAGS_SPLITTER.split(tags)));

		try {
			LinksSourceEntity source = this.em.find(LinksSourceEntity.class, sourceId);
			if (source == null) {
				return ResponseBuilder.notFound("source", sourceId);
			}

			source.getLinks().add(entity);

			Response response;
			if (id == null) {
				// Create
				entity.setId(this.createId());
				this.em.merge(source);
				response = ResponseBuilder.created(source);
			} else {
				// Update
				this.em.merge(entity);
				response = ResponseBuilder.ok(source);
			}
			return response;
		} catch (Exception e) {
			return ResponseBuilder.fail(e);
		}
	}

	/**
	 * Delete link.
	 *
	 * @param id the id
	 * @return the response
	 */
	@DELETE
	@Path("{id}")
	public Response deleteLink(@PathParam("id") String id) {
		try {
			LinkEntry entity = this.em.find(LinkEntry.class, id);

			// TODO use a NamedQuery
			TypedQuery<LinksSourceEntity> query = this.em.createQuery("From SourceLinks s Where :link Member Of s.links ", LinksSourceEntity.class);
			query.setParameter("link", entity);
			LinksSourceEntity source = query.getSingleResult();

			source.getLinks().remove(entity);
			this.em.merge(source);

			this.em.remove(entity);

			return ResponseBuilder.ok(source);
		} catch (Exception e) {
			return ResponseBuilder.fail(e);
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

		Set<String> tags = Sets.newHashSet();
		LinkEntry entry;

		Iterable<String> lines = Splitter.on(CharMatcher.anyOf("\n\r\f")).split(links);
		String url;
		for (String line : lines) {
			if (Strings.isNullOrEmpty(line.trim())) {
				// Separator line
				tags = Sets.newHashSet();
			} else if (line.startsWith("http:") || line.startsWith("https:")) {
				url = line.trim();
				// an entry
				entry = new LinkEntry();
				entry.setActive(true);
				entry.setLink(url);
				entry.setTags(tags);
				entry.setId(this.createId());

				entry.setTitle(this.extractTitle(url));
				entry.setHost(this.extractHost(url));
				entry.setDate(this.extractDate(url));

				result.add(entry);
			} else {
				// Handle tag
				tags = Sets.newHashSet(TAGS_SPLITTER.split(line));
			}
		}

		return result;
	}

	/**
	 * Extract host.
	 *
	 * @param link the link
	 * @return the string
	 */
	public String extractHost(String link) {
		Iterable<String> split = URL_SPLITTER.split(link);
		return Iterables.get(split, 1);
	}

	/**
	 * Extract title.
	 *
	 * @param link the link
	 * @return the string
	 */
	public String extractTitle(String link) {
		Iterable<String> split = URL_SPLITTER.split(link);
		String page = Iterables.getLast(split);

		// Handle last segment
		page = Joiner.on(' ').join(Splitter.on('-').split(page));

		// Remove .html
		int idx = page.lastIndexOf(".html");
		if (idx > 0) {
			page = page.substring(0, idx);
		}

		return Capitalize.FUNCTION.apply(page);
	}

	/**
	 * Extract date.
	 *
	 * @param link the link
	 * @return the calendar
	 */
	public Calendar extractDate(String link) {
		Calendar result = null;
		Pattern pattern = Pattern.compile("/(\\d\\d\\d\\d)/(\\d\\d)/(\\d\\d)/");

		Matcher matcher = pattern.matcher(link);
		if (matcher.matches()) {
			int year = Integer.valueOf(matcher.group(1));
			int month = Integer.valueOf(matcher.group(2));
			int day = Integer.valueOf(matcher.group(3));

			// Build Calendar
			result = Calendar.getInstance();
			result.clear();
			result.set(Calendar.YEAR, year);
			result.set(Calendar.MONTH, month - 1); // January is 0
			result.set(Calendar.DAY_OF_MONTH, day);
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
