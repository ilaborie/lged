package org.ilaborie.pineneedles.web.rest;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.elements.Field;
import org.ilaborie.pineneedles.web.model.elements.IIndexableElement;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.service.IFieldProvider;
import org.ilaborie.pineneedles.web.service.IIndexElementProvider;
import org.slf4j.Logger;

import com.google.common.collect.Multimap;

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

	/** The element providers. */
	@Inject
	private Instance<IIndexElementProvider> elementProviders;

	/** The field provider. */
	@Inject
	private IFieldProvider fieldProvider;

	/** The entity manager. */
	@Inject
	private EntityManager em;

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

		TypedQuery<SourceEntity> query = this.em.createNamedQuery(SourceEntity.QUERY_FIND_ALL, SourceEntity.class);
		for (SourceEntity source : query.getResultList()) {
			for (IIndexElementProvider provider : this.elementProviders) {
				if (provider.canProcessSource(source)) {
					this.synchronize(provider, source);
					break;
				}
			}
		}

		return Response.ok().build();
	}

	/**
	 * Synchronize.
	 *
	 * @param provider the provider
	 * @param source the source
	 */
	private void synchronize(IIndexElementProvider provider, SourceEntity source) {
		IIndexableElement element;
		for (Iterator<IIndexableElement> itElements = provider.processSource(source); itElements.hasNext();) {
			element = itElements.next();
			if (element.isActive()) {
				this.index(provider, source, element);
			} else {
				this.logger.warn("Element {}/{} in inactive", source, element);
			}
		}
	}

	/**
	 * Index.
	 *
	 * @param provider the provider
	 * @param source the source
	 * @param element the element
	 */
	private void index(IIndexElementProvider provider, SourceEntity source, IIndexableElement element) {
		try {
			this.logger.info("Extract {}/{}", source, element);
			Multimap<Field, ?> fields = element.getIndexableFields(this.fieldProvider, source);
			// TODO index
		} catch (Exception e) {
			this.logger.warn("Error in indexing {}/{}", source, element);
			this.logger.warn("Detail: ", e);
		} finally {
			// update Title, Date, Tag
			this.logger.debug("Update the entity");
			provider.updateEntity(element, source);
		}
	}
}
