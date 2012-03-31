package org.ilaborie.pineneedles.web.rest;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.ilaborie.pineneedles.web.model.elements.BasicField;
import org.ilaborie.pineneedles.web.model.search.DocFacet;
import org.ilaborie.pineneedles.web.model.search.Document;
import org.ilaborie.pineneedles.web.model.search.Documents;
import org.ilaborie.pineneedles.web.model.search.FacetEntry;
import org.ilaborie.pineneedles.web.util.ResponseBuilder;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * The Class ShelfResource.
 */
@Path("search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Search {

	/** The Constant SIZE. */
	public static final int SIZE = 10;

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The logger. */
	@Inject
	private Logger logger;

	/** The client. */
	@Inject
	private Client client;

	/**
	 * Search.
	 *
	 * @param query the query
	 * @return the response
	 */
	@GET
	public Response search(@QueryParam("q") String query, @QueryParam("from") Integer from) {
		logger.info("Search#searh() : {}", this.uriInfo.getAbsolutePath());
		int start = 0;
		if (from != null) {
			start = from;
		}
		Documents docs = this.retrieveDocuments(query, SIZE, start);

		return ResponseBuilder.ok(docs);
	}

	/**
	 * Retrieve documents.
	 *
	 * @param query the query
	 * @return the documents
	 */
	private Documents retrieveDocuments(String query, int size, int from) {
		Documents docs = new Documents();

		// Create Query
		QueryBuilder q = QueryBuilders.fieldQuery(BasicField.FULL_TEXT.getFieldName(), query);

		// Facets
		TermsFacetBuilder tagFacet = FacetBuilders.termsFacet("Tags")
		        .fields(BasicField.TAG.getFieldName());
		TermsFacetBuilder termFacet = FacetBuilders.termsFacet("Term")
		        .fields(BasicField.FULL_TEXT.getFieldName());

		// Search
		SearchRequestBuilder request = this.client.prepareSearch()
		        .setSearchType(SearchType.QUERY_AND_FETCH)
		        .setQuery(q)
		        .addFields(BasicField.TITLE.getFieldName(),
		                BasicField.TEASER.getFieldName(),
		                BasicField.LOCATION.getFieldName(),
		                BasicField.TAG.getFieldName())
		        .setSize(size)
		        .addFacet(tagFacet)
		        .addFacet(termFacet)
		        .setFrom(from);

		// Get response
		SearchResponse response = request.execute().actionGet();

		// Handle result
		SearchHits hits = response.getHits();

		// Search metadata
		docs.setTime(response.took().nanos());
		// Totals
		docs.setResults(hits.getTotalHits());

		// Paging
		docs.setFrom(from);
		docs.setSize(size);

		// Facets
		docs.setFacets(
		        this.handleFacets(response.getFacets()));

		// Documents
		docs.setDocs(this.extractDocuments(hits));

		return docs;
	}

	/**
	 * Handle facets.
	 *
	 * @param facets the facets
	 * @return the list
	 */
	private List<DocFacet> handleFacets(Facets facets) {
		List<DocFacet> docFacets = Lists.newArrayList();
		TermsFacet termsFacet;

		DocFacet facet;
		for (Entry<String, Facet> entry : facets.getFacets().entrySet()) {
			termsFacet = (TermsFacet) entry.getValue();

			facet = new DocFacet(entry.getKey());
			docFacets.add(facet);

			for (TermsFacet.Entry fe : termsFacet.getEntries()) {
				facet.getEntries().add(new FacetEntry(fe.getTerm(), fe.getCount()));
			}
		}
		return docFacets;
	}

	/**
	 * Extract documents.
	 *
	 * @param hits the hits
	 * @return the list
	 */
	private List<Document> extractDocuments(SearchHits hits) {
		List<Document> documents = Lists.newArrayList();
		Document doc;
		for (SearchHit hit : hits.hits()) {
			doc = new Document();
			documents.add(doc);
			doc.setId(hit.getId());

			// Title
			doc.setTitle(this.getValue(hit, BasicField.TITLE, String.class));

			// Location
			doc.setLocation(this.getValue(hit, BasicField.LOCATION, String.class));

			// Teaser
			doc.setTeaser(this.getValue(hit, BasicField.TEASER, String.class));

			// Tag
			doc.setTags(this.getValues(hit, BasicField.TAG, String.class));
		}
		return documents;
	}

	/**
	 * Gets the value.
	 *
	 * @param <T> the generic type
	 * @param hit the hit
	 * @param field the field
	 * @param clazz the class
	 * @return the value
	 */
	private <T> T getValue(SearchHit hit, BasicField field, Class<T> clazz) {
		SearchHitField sField = hit.getFields().get(field.getFieldName());
		return clazz.cast(sField.getValue());
	}

	/**
	 * Gets the values.
	 *
	 * @param hit the hit
	 * @param field the field
	 * @param clazz the class
	 * @return the values
	 */
	@SuppressWarnings("unchecked")
	private <T extends Comparable<T>> Set<T> getValues(SearchHit hit, BasicField field, Class<T> clazz) {
		SearchHitField sField = hit.getFields().get(field.getFieldName());
		Set<T> result;

		Object val = sField.getValue();
		if (val instanceof List) {
			List<T> lst = sField.getValue();
			result = Sets.newTreeSet(lst);
		} else if (clazz.isInstance(val)) {
			result = (Set<T>) Sets.newHashSet(val);
		} else {
			result = Collections.emptySet();
		}
		return result;
	}

}
