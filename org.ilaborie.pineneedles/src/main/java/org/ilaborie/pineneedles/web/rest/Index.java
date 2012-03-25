package org.ilaborie.pineneedles.web.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

/**
 * The Class ShelfResource.
 */
@Path("index")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Index {

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The logger. */
	@Inject
	private Logger logger;

	/**
	 * Clear.
	 *
	 * @return the response
	 */
	@GET
	@Path("clear")
	public Response clear() {
		logger.info("Index#clear() : {}", this.uriInfo.getAbsolutePath());

		return Response.ok().build();
	}

	/**
	 * Synchronize.
	 *
	 * @return the response
	 */
	@GET
	@Path("synchronize")
	public Response synchronize() {
		logger.info("Index#synchronize() : {}", this.uriInfo.getAbsolutePath());

		return Response.ok().build();
	}
}
