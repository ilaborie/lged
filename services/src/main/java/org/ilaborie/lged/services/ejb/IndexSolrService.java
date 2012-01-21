package org.ilaborie.lged.services.ejb;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.ilaborie.lged.commons.model.IIndexableElement;
import org.ilaborie.lged.commons.services.IIndexService;
import org.ilaborie.lged.commons.utils.Fields;
import org.ilaborie.lged.commons.utils.predicates.IndexableElementWithFile;
import org.ilaborie.lged.services.utils.functions.ElementToSolrDocumentFunction;
import org.slf4j.Logger;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * The Class SolrIndexService.
 */
@Stateless
@Local(IIndexService.class)
public class IndexSolrService implements IIndexService {

	/** The Constant OK_STATUS. */
	private static final int OK_STATUS = 0;

	/** The log. */
	@Inject
	private Logger log;

	/** The server. */
	@Inject
	private SolrServer server;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISolrIndexService#index(org.ilaborie
	 * .lged.commons.model.ISolrIndexableElement[])
	 */
	@Override
	public boolean index(IIndexableElement... elements) {
		assert this.server != null;
		log.info("Index elements: {}", elements);

		List<IIndexableElement> list = Lists.newArrayList(elements);
		return this.indexList(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISolrIndexService#indexAll(java.util
	 * .Collection)
	 */
	@Override
	public boolean indexAll(Collection<? extends IIndexableElement> elements) {
		assert this.server != null;
		log.info("Index elements: {}", elements);

		List<? extends IIndexableElement> list = Lists.newArrayList(elements);
		return this.indexList(list);
	}

	/**
	 * Index list.
	 * 
	 * @param list
	 *            the list
	 * @return true, if successful
	 */
	private boolean indexList(List<? extends IIndexableElement> list) {
		boolean success;
		IndexableElementWithFile eltWithFilePredicate = new IndexableElementWithFile();

		// Handle classic documents
		Collection<? extends IIndexableElement> eltsWithoutFiles = Collections2
				.filter(list, Predicates.not(eltWithFilePredicate));
		Collection<SolrInputDocument> classicsDocs = Collections2.transform(
				eltsWithoutFiles, new ElementToSolrDocumentFunction());
		success = this.indexClassic(classicsDocs);

		if (success) {
			// Handle documents with file
			Collection<? extends IIndexableElement> eltsWithFiles = Collections2
					.filter(list, eltWithFilePredicate);
			success = this.indexWithFile(eltsWithFiles);
		}
		return success;
	}

	/**
	 * Index.
	 * 
	 * @param docs
	 *            the documents
	 * @return true, if successful
	 */
	private boolean indexClassic(Collection<SolrInputDocument> docs) {
		assert this.server != null;

		boolean success = docs.isEmpty();
		if (!success) {
			try {
				UpdateRequest req = new UpdateRequest();
				req.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
				req.add(docs);

				UpdateResponse response = req.process(this.server);
				log.trace("Add response: {}", response);
				success = response.getStatus() == OK_STATUS;
			} catch (SolrServerException e) {
				this.log.error("Cannot add documents", e);
			} catch (IOException e) {
				this.log.error("Cannot connect to server", e);
			}
		}
		return success;
	}

	/**
	 * Index.
	 * 
	 * @param docs
	 *            the documents
	 * @return true, if successful
	 */
	private boolean indexWithFile(Collection<? extends IIndexableElement> elts) {
		assert this.server != null;

		boolean success = elts.isEmpty();
		if (!success) {
			try {
				UpdateResponse response;
				ContentStreamUpdateRequest req;
				success = true;
				for (IIndexableElement elt : elts) {
					req = new ContentStreamUpdateRequest("/update/extract");
					req.addFile(elt.getFile());
					req.setParam("defaultField", "text");
					// Add extra fields
					for (Entry<Fields, ?> entry : elt.getIndexableFields()
							.entries()) {
						req.setParam("literal." + entry.getKey().getField(),
								String.valueOf(entry.getValue()));
					}
					// Send
					response = req.process(this.server);
					log.trace("Add response: {}", response);
					success &= response.getStatus() == OK_STATUS;
				}

				// Commit
				if (success) {
					response = this.server.commit();
					success = response.getStatus() == OK_STATUS;
				}
			} catch (SolrServerException e) {
				this.log.error("Cannot add documents", e);
			} catch (IOException e) {
				this.log.error("Cannot connect to server", e);
			}
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISolrIndexService#delete(java.lang
	 * .String[])
	 */
	@Override
	public boolean delete(String... ids) {
		assert this.server != null;
		log.info("Delete elements: {}", ids);

		List<String> list = Lists.newArrayList(ids);
		return this.delete(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.ISolrIndexService#delete(java.util
	 * .Collection)
	 */
	@Override
	public boolean delete(Collection<String> ids) {
		assert this.server != null;
		log.info("Delete elements: {}", ids);

		boolean success = false;
		try {
			List<String> list = Lists.newArrayList(ids);
			UpdateRequest req = new UpdateRequest();
			req.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
			req.deleteById(list);
			UpdateResponse response = req.process(this.server);
			log.trace("Delete response: {}", response);
			success = response.getStatus() == OK_STATUS;
		} catch (SolrServerException e) {
			this.log.error("Cannot delete documents", e);
		} catch (IOException e) {
			this.log.error("Cannot connect to server", e);
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.services.ISolrIndexService#clear()
	 */
	@Override
	public boolean clear() {
		assert this.server != null;
		log.info("Clear index");

		boolean success = false;
		try {
			UpdateRequest req = new UpdateRequest();
			req.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
			req.deleteByQuery("*:*");
			UpdateResponse response = req.process(this.server);
			log.trace("Clear response: {}", response);
			success = response.getStatus() == OK_STATUS;
		} catch (SolrServerException e) {
			this.log.error("Cannot clear index", e);
		} catch (IOException e) {
			this.log.error("Cannot connect to server", e);
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.services.ISolrIndexService#optimize()
	 */
	@Override
	public boolean optimize() {
		assert this.server != null;
		log.info("Optimize index");

		boolean success = false;
		try {
			UpdateResponse response = this.server.optimize();
			this.log.trace("Optimize response: {}", response);
			success = response.getStatus() == OK_STATUS;
		} catch (SolrServerException e) {
			this.log.error("Cannot optimize index", e);
		} catch (IOException e) {
			this.log.error("Cannot connect to server", e);
		}
		return success;
	}

}
