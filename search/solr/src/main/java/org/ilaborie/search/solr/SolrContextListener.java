package org.ilaborie.search.solr;

import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initialize Solr
 * 
 * @see ServletContextListener
 */
public class SolrContextListener implements ServletContextListener {

	/** The logger. */
	private static Logger logger = LoggerFactory
			.getLogger(SolrContextListener.class);

	/**
	 * Context initialized.
	 * 
	 * @param event
	 *            the arg0 {@inheritDoc}
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("Initialize SolR webapp");
		// Load properties
		ResourceBundle bundle = ResourceBundle.getBundle("solr");

		// Configure SolR
		String solrHome = event.getServletContext().getRealPath("WEB-INF/classes/");
		logger.info("Set SolR Home to {}", solrHome);
		System.setProperty("solr.solr.home", solrHome);
		String solrDataDir = bundle.getString("solr.data");
		logger.info("Set SolR Data Directory to {}", solrDataDir);
		System.setProperty("solr.data.dir", solrDataDir);
	}

	/**
	 * Context destroyed.
	 * 
	 * @param event
	 *            the arg0 {@inheritDoc}
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Destroy SolR webapp");
	}

}
