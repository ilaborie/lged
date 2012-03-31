package org.ilaborie.pineneedles.web.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.ilaborie.pineneedles.web.model.elements.Field;
import org.ilaborie.pineneedles.web.model.elements.IIndexableElement;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.service.IFieldProvider;
import org.ilaborie.pineneedles.web.service.IIndexElementProvider;
import org.ilaborie.pineneedles.web.util.Resources;
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

	@Inject
	private Client client;

	/**
	 * Clear.
	 *
	 * @return the response
	 */
	@GET
	@Path("clear")
	public Response clear() {
		logger.info("Index#clear() : {}", this.uriInfo.getAbsolutePath());

		DeleteMappingRequestBuilder resquest = this.client.admin().indices().prepareDeleteMapping(Resources.ES_INDEX).setType(Resources.ES_TYPE);

		DeleteMappingResponse response = resquest.execute().actionGet();
		logger.info("Deleted: {}", response);

		return Response.ok().build();
	}

	/**
	 * Synchronize.
	 *
	 * @return the response
	 */
	@GET
	@Path("synchronize")
	@Asynchronous
	public void synchronize() {
		logger.info("Index#synchronize()");

		TypedQuery<SourceEntity> query = this.em.createNamedQuery(SourceEntity.QUERY_FIND_ALL, SourceEntity.class);
		for (SourceEntity source : query.getResultList()) {
			for (IIndexElementProvider provider : this.elementProviders) {
				if (provider.canProcessSource(source)) {
					this.synchronize(provider, source);
					break;
				}
			}
		}
	}

	/**
	 * Synchronize.
	 *
	 * @return the response
	 */
	@GET
	@Path("synchronize/{id}")
	@Asynchronous
	public void synchronize(@PathParam("id") String sourceId) {
		logger.info("Index#synchronize()");

		SourceEntity source = this.em.find(SourceEntity.class, sourceId);
		if (source != null) {
			for (IIndexElementProvider provider : this.elementProviders) {
				if (provider.canProcessSource(source)) {
					this.synchronize(provider, source);
					break;
				}
			}
		}
	}

	/**
	 * Synchronize.
	 *
	 * @param provider the provider
	 * @param source the source
	 */
	private void synchronize(IIndexElementProvider provider, SourceEntity source) {
		IIndexableElement element;
		// TODO: Do a bulk index, see  org.elasticsearch.benchmark.search.facet.TermsFacetSearchBenchmark 
		for (Iterator<IIndexableElement> itElements = provider.processSource(source); itElements.hasNext();) {
			element = itElements.next();
			if (element.isActive()) {
				this.index(provider, source, element);
			} else {
				this.logger.warn("Element {}/{} in inactive", source, element);
			}
		}

		// Commit
		this.client.admin().indices().prepareRefresh().execute().actionGet();
	}

	/**
	 * Index.
	 *
	 * @param provider the provider
	 * @param source the source
	 * @param element the element
	 */
	private void index(IIndexElementProvider provider, final SourceEntity source, IIndexableElement element) {
		try {
			this.logger.info("Extract {}/{}", source, element);
			Multimap<Field, ?> fields = element.getIndexableFields(this.fieldProvider, source);

			// Put field into index
			String id = element.getId();

			IndexRequestBuilder request = this.client.prepareIndex(Resources.ES_INDEX, Resources.ES_TYPE, id)
			        .setSource(this.createIndexRequest(id, fields));

			IndexResponse response = request.execute().actionGet();

			this.logger.info("Indexed: {}/{}/{}", new Object[] { response.getIndex(), response.getType(), response.getId() });

		} catch (Exception e) {
			this.logger.warn("Error in indexing {}/{}", source, element);
			this.logger.warn("Detail: ", e);
		} finally {
			// update Title, Date, Tag
			this.logger.debug("Update the entity");
			provider.updateEntity(element, source);
		}
	}

	/**
	 * Creates the index request.
	 *
	 * @param id the id
	 * @param fields the fields
	 * @return the index request
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private XContentBuilder createIndexRequest(String id, Multimap<Field, ?> fields) throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
		builder.field("id", id);

		Collection<?> values;
		for (Field f : fields.keySet()) {
			values = fields.get(f);
			if (values != null) {
				if (values.size() == 1) {
					builder.field(f.getFieldName(), values.iterator().next());
				} else {
					builder.startArray(f.getFieldName());
					for (Object obj : values) {
						builder.value(obj);
						//						builder.field(f.getFieldName(), obj);
					}
					builder.endArray();
				}
			}
		}

		return builder;
	}

}
