package org.ilaborie.pineneedles.web.rest;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.Message;
import org.ilaborie.pineneedles.web.model.Tag;
import org.slf4j.Logger;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * The Class ShelfResource.
 */
@Path("tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Tags {

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The entity manager. */
	@Inject
	private EntityManager em;

	/** The logger. */
	@Inject
	private Logger logger;

	@GET
	@SuppressWarnings("unchecked")
	public Response findAll() {
		logger.info("Tags#findAll() : {}", this.uriInfo.getAbsolutePath());

		try {
			Query query = this.em.createNativeQuery("SELECT tags, count(tags) FROM tag GROUP BY tags");
			List<Object[]> lst = query.getResultList();
			List<Tag> result = this.createTags(lst);

			return Response.ok(result).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	@GET
	@Path("{startWith}")
	@SuppressWarnings("unchecked")
	public Response find(@PathParam("startWith") String startWith) {
		logger.info("Tags#findAll() : {}", this.uriInfo.getAbsolutePath());

		if (Strings.isNullOrEmpty(startWith)) {
			return Response.status(Status.BAD_REQUEST)
			        .entity(new Message("'startWith' should not been empty !")).build();
		}

		try {
			Query query = this.em.createNativeQuery("SELECT tags, count(tags) FROM tag WHERE LOWER(tags) LIKE :start GROUP BY tags");
			query.setParameter("start", startWith.toLowerCase() + '%');
			List<Object[]> lst = query.getResultList();
			List<Tag> result = this.createTags(lst);

			return Response.ok(result).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	/**
	 * Replace.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the response
	 */
	@PUT
	@Path("replace")
	public Response replace(@QueryParam("from") String from, @QueryParam("to") String to) {
		logger.info("Tags#findAll() : {}", this.uriInfo.getAbsolutePath());

		if (Strings.isNullOrEmpty(from)) {
			return Response.status(Status.BAD_REQUEST)
			        .entity(new Message("'to' should not been empty !")).build();
		}
		try {
			Query query = this.em.createNativeQuery("UPDATE tag SET tags = :to WHERE tags = :from");
			query.setParameter("from", from);
			query.setParameter("to", to);
			int update = query.executeUpdate();

			return Response.ok(new Message("Updated: " + update)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	/**
	 * Creates the tags.
	 *
	 * @param lst the lst
	 * @return the list
	 */
	private List<Tag> createTags(List<Object[]> lst) {
		List<Tag> result = Lists.newArrayList();
		Tag tag;
		for (Object[] a : lst) {
			tag = new Tag();
			tag.setTag((String) a[0]);
			tag.setCount(((BigInteger) a[1]).intValue());
			result.add(tag);
		}
		Collections.sort(result);
		return result;
	}

}
