package org.ilaborie.lged.services.utils;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LoggerProducer.
 */
public class LoggerProducer {

	/**
	 * Creates the logger.
	 *
	 * @param ip the injection point
	 * @return the logger
	 */
	@Produces
	public Logger createLogger(InjectionPoint ip) {
		Class<?> clazz = ip.getMember().getClass();
		Logger logger =  LoggerFactory.getLogger(clazz);
		
		assert logger!=null;
		return logger;
	}
}
