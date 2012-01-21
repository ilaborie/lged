package org.ilaborie.lged.services.ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.ilaborie.lged.commons.model.IIndexableElement;
import org.ilaborie.lged.commons.services.IElementService;
import org.ilaborie.lged.commons.services.ISearchService;
import org.ilaborie.lged.commons.utils.Fields;
import org.ilaborie.lged.commons.utils.functions.StringToElement;
import org.ilaborie.lged.services.utils.functions.SolrDocumentToId;
import org.ilaborie.lged.services.utils.functions.TermToString;
import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * The Class SearchSolr.
 */
@Stateless
@Local(ISearchService.class)
public class SearchSolrService implements ISearchService {

	/** The Constant OK_STATUS. */
	private static final int OK_STATUS = 0;

	/** The log. */
	@Inject
	private Logger log;

	/** The server. */
	@Inject
	private SolrServer server;

	/** The elements service. */
	@Inject
	private IElementService eltService;

	/**
	 * Initialize.
	 */
	@PostConstruct
	protected void initialize() {
		log.info("Initialize");
		// Check Server
		try {
			SolrPingResponse response = this.server.ping();
			log.debug("Ping response: {}", response);
		} catch (Exception e) {
			log.error("Could not ping the solr server");
			throw new EJBException(e);
		}

		assert this.server != null;
	}

	/**
	 * Gets the doc to element function.
	 * 
	 * @return the doc to element function
	 */
	private Function<SolrDocument, IIndexableElement> getDocToElementFunction() {
		StringToElement g = new StringToElement(this.eltService);
		SolrDocumentToId f = new SolrDocumentToId();
		return Functions.compose(g, f);
	}

	/**
	 * Search.
	 * 
	 * @param query
	 *            the query
	 * @return the list
	 */
	private List<IIndexableElement> search(SolrQuery query) {
		List<IIndexableElement> result = null;
		try {
			QueryResponse response = this.server.query(query);
			if (response.getStatus() == OK_STATUS) {
				Function<SolrDocument, IIndexableElement> compose = this
						.getDocToElementFunction();

				List<SolrDocument> docs = response.getResults();
				result = Lists.transform(docs, compose);
			}
		} catch (SolrServerException e) {
			this.log.error("Could not retrieve documents", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.services.ISearchSolrService#getAll()
	 */
	@Override
	public List<IIndexableElement> getAll() {
		assert this.server != null;
		log.warn("Retrieve all elements !");

		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");

		return this.search(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.services.ISearchSolrService#getFirst(int)
	 */
	@Override
	public List<IIndexableElement> getFirst(int number) {
		assert this.server != null;
		log.info("Retrieve {} first elements", number);

		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRows(number);

		return this.search(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISearchSolrService#searchFullText(
	 * java.lang.String, int)
	 */
	@Override
	public List<IIndexableElement> searchFullText(String text, int number) {
		assert this.server != null;
		log.info("Full text search: {}", text);

		SolrQuery query = new SolrQuery();
		query.setQuery(text);
		query.setRows(number);

		return this.search(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISearchSolrService#searchTerm(java
	 * .lang.String, int)
	 */
	@Override
	public List<String> searchTerms(String startsWith, int max) {
		assert this.server != null;
		log.info("Terms search: {}", startsWith);

		SolrQuery query = new SolrQuery();
		query.setQueryType("/terms");
		query.setTerms(true);
		query.setTermsLimit(max);
		query.addTermsField(Fields.FULL_TEXT.getField());
		if (!Strings.isNullOrEmpty(startsWith)) {
			query.setTermsPrefix(startsWith);
			query.setTermsLower(startsWith);
		}

		List<String> result = null;
		try {
			QueryResponse response = this.server.query(query);
			if (response.getStatus() == OK_STATUS) {
				TermsResponse termsResponse = response.getTermsResponse();
				List<Term> terms = termsResponse.getTerms(Fields.FULL_TEXT
						.getField());
				result = Lists.transform(terms, new TermToString());
			}
		} catch (SolrServerException e) {
			this.log.error("Could not retrieve documents", e);
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISearchSolrService#countDocuments()
	 */
	@Override
	public long countDocuments() {
		assert this.server != null;
		log.info("Count documents");
		long count = 0;

		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setRows(1);

		try {
			QueryResponse response = this.server.query(query);
			if (response.getStatus() == OK_STATUS) {
				SolrDocumentList list = response.getResults();
				count = list.getNumFound();
			}
		} catch (SolrServerException e) {
			this.log.error("Could not retrieve documents", e);
		}

		return count;
	}

}
