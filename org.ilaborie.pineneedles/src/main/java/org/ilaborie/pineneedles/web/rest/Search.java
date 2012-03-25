package org.ilaborie.pineneedles.web.rest;

import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.Document;
import org.ilaborie.pineneedles.web.model.Documents;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

/**
 * The Class ShelfResource.
 */
@Path("search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Search {

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The logger. */
	@Inject
	private Logger logger;

	/**
	 * Search.
	 *
	 * @param query the query
	 * @return the response
	 */
	@GET
	public Response search(@QueryParam("q") String query) {
		logger.info("Search#searh() : {}", this.uriInfo.getAbsolutePath());

		Documents docs = this.retrieveDocuments(query);

		return Response.ok(docs).build();
	}

	/**
	 * Retrieve documents.
	 *
	 * @param query the query
	 * @return the documents
	 */
	private Documents retrieveDocuments(String query) {
		Documents docs = new Documents();
		// XXX Dummy data
		docs.setPage(1);
		docs.setResults(42);
		docs.setTime(243L);

		List<Document> documents = Lists.newArrayList();
		docs.setDocs(documents);

		Document doc;
		for (int i = 0; i < 5; i++) {
			doc = new Document();
			doc.setId(UUID.randomUUID().toString());
			doc.setTitle("Doc " + i);
			doc.setLocation("/Documents/plop");
			doc.setTeaser("Lorem ipsum dolor set ament");
			documents.add(doc);
		}

		return docs;
	}}
