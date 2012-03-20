package org.ilaborie.pineneedles.web.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Resources.
 */
public class Resources {
	
	/** The entity manager. */
	@SuppressWarnings("unused")
	@Produces
	@PersistenceContext
	private EntityManager em;

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