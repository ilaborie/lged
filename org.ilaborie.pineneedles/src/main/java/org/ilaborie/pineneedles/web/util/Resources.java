package org.ilaborie.pineneedles.web.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.network.NetworkUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Resources.
 */
@Singleton
public class Resources {

	/** The Constant ES_INDEX. */
	public static final String ES_INDEX = "pineneedles";

	/** The Constant ES_TYPE. */
	public static final String ES_TYPE = "doc";

	/** The entity manager. */
	@PersistenceContext
	private EntityManager em;

	/** The node. */
	private Node node;

	/** The client. */
	private Client client;

	/**
	 * Instantiates a new resources.
	 */
	public Resources() {
		super();
	}

	/**
	 * Startup.
	 */
	@PostConstruct
	public void startup() {
		// Do nothing 
	}

	/**
	 * Shutdown.
	 */
	@PreDestroy
	public void shutdown() {
		// Close client
		if (this.client != null) {
			this.client.close();
			this.client = null;
		}
		// Close node
		if (this.node != null) {
			this.node.close();
			this.node = null;
		}
	}

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	@Produces
	public EntityManager getEntityManager() {
		return this.em;
	}

	/**
	 * Gets the elastic search node.
	 *
	 * @return the elastic search node
	 */
	@Produces
	public Client createElasticSearchNode() {
		if (this.client == null) {
			// Settings
			Settings defaultSettings = ImmutableSettings.settingsBuilder()
			        .put("cluster.name", "pineneedles-" + NetworkUtils.getLocalAddress().getHostName())
			        .build();

			// Create node
			this.node = NodeBuilder.nodeBuilder()
			        .local(true)
			        .data(true)
			        .settings(defaultSettings)
			        .build();

			// Start the node
			this.node.start();

			// Get client
			this.client = this.node.client();

			// Create Index
			if (!this.client.admin().indices().prepareExists(ES_INDEX)
			        .execute().actionGet().exists()) {
				this.client.admin().indices().prepareCreate(ES_INDEX)
				        .execute().actionGet();
			}

			this.client.admin()
			        .cluster()
			        .prepareHealth(ES_INDEX)
			        .setWaitForYellowStatus()
			        .execute()
			        .actionGet();

			// TODO Mapping
			// Create Teaser from FULL_TEXT
			// Copy TITLE, TAG, FULL_TEXT to CONTENTS
			// CONTENTS analyzer
		}
		return this.client;
	}

	/**
	 * Produce log.
	 *
	 * @param injectionPoint the injection point
	 * @return the logger
	 */
	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
}