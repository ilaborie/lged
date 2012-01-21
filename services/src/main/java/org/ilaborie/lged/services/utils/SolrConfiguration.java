package org.ilaborie.lged.services.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.slf4j.Logger;

/**
 * The Class Configuration.
 */
@Singleton
public class SolrConfiguration {

	private static final String SOLR_URL_PROPERTY = "solr.url";
	private static final String SOLR_URL_DEFAULT = "http://localhost:8080/solr";

	/** The log. */
	@Inject
	private Logger log;

	/** The SolR server URL. */
	private String url;

	/** The SolR server. */
	private SolrServer server;

	@PostConstruct
	public void initialize() {
		log.info("Initialize");

		// Configure SolR URL
		log.trace("Configure SolR URL");

		this.url = System.getProperty(SOLR_URL_PROPERTY);
		// Lookup a solr.properties file
		try {
			ResourceBundle solrBundle = ResourceBundle.getBundle("solr");

			// Retrieve Solr URL
			if (this.url == null && solrBundle.containsKey(SOLR_URL_PROPERTY)) {
				this.url = solrBundle.getString(SOLR_URL_PROPERTY);
			}
		} catch (MissingResourceException e) {
			log.warn("Could not find the solr.properties file ! Using default values...");
		} finally {
			// Default Values
			if (url == null) {
				url = SOLR_URL_DEFAULT;
			}

			log.trace("SolR URL: {}", url);
		}
		assert this.url != null;
	}

	/**
	 * Gets the server.
	 * 
	 * @return the server
	 */
	@Produces
	public synchronized SolrServer getServer() {
		if (this.server == null) {
			this.createSolrServer();
		}

		assert this.server != null;
		return server;
	}

	/**
	 * Creates the solr server.
	 */
	private void createSolrServer() {
		// Create SolR server
		log.trace("Create SolR server");
		try {
			this.server = new CommonsHttpSolrServer(this.url);
			this.server.ping();
			log.info("SolR Server Created");
		} catch (Exception e) {
			this.log.error("Could no create the SolR Server :'(", e);
			throw new IllegalStateException("Could no create the SolR Server",
					e);
		}

		assert this.server != null;
	}

}
