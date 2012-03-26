package org.ilaborie.pineneedles.web.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.ilaborie.pineneedles.web.model.BaseSource;
import org.ilaborie.pineneedles.web.model.FolderSource;
import org.ilaborie.pineneedles.web.model.LinksSource;
import org.ilaborie.pineneedles.web.model.entity.ShelfEntity;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.model.entity.FolderSourceEntity;
import org.ilaborie.pineneedles.web.model.entity.LinksSourceEntity;
import org.ilaborie.pineneedles.web.util.ResponseBuilder;
import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * The Class ShelfResource.
 */
@Path("sources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class Sources {

	/**
	 * The Class EntityToPojo.
	 */
	private final class EntityToPojo implements Function<SourceEntity, BaseSource> {

		/**
		 * Apply.
		 *
		 * @param input the input
		 * @return the base source
		 */
		@Override
		public BaseSource apply(SourceEntity input) {
			BaseSource result = null;
			if (input instanceof FolderSourceEntity) {
				result = this.transform((FolderSourceEntity) input);
			} else if (input instanceof LinksSourceEntity) {
				result = this.transform((LinksSourceEntity) input);
			}
			return result;
		}

		/**
		 * Transform.
		 *
		 * @param input the input
		 * @return the base source
		 */
		private BaseSource transform(FolderSourceEntity input) {
			FolderSource result = new FolderSource();
			result.setId(input.getId());
			result.setName(input.getName());
			result.setDescription(input.getDescription());
			result.setPath(input.getFolder());
			result.setShelfId(input.getShelf().getId());
			return result;
		}

		/**
		 * Transform.
		 *
		 * @param input the input
		 * @return the base source
		 */
		private BaseSource transform(LinksSourceEntity input) {
			LinksSource result = new LinksSource();
			result.setId(input.getId());
			result.setName(input.getName());
			result.setDescription(input.getDescription());

			int size = input.getLinks().size();
			String lnk;
			switch (size) {
				case 0:
					lnk = "No link !";
					break;
				case 1:
					lnk = input.getLinks().iterator().next().getLink();
					break;
				default:
					lnk = String.format("%1$d link(s)", size);
					break;
			}

			result.setLinks(lnk);
			result.setShelfId(input.getShelf().getId());
			return result;
		}
	}

	/** The URI info. */
	@Context
	private UriInfo uriInfo;

	/** The entity manager. */
	@Inject
	private EntityManager em;

	/** The logger. */
	@Inject
	private Logger logger;

	/**
	 * Find all.
	 *
	 * @param shelfId the shelf id
	 * @return the list
	 */
	@GET
	@Path("shelf/{id}")
	public Response findByShelf(@PathParam("id") String shelfId) {
		logger.info("Sources#findByShelf() : {}", this.uriInfo.getAbsolutePath());

		TypedQuery<SourceEntity> query = this.em.createNamedQuery(SourceEntity.QUERY_FIND_BY_SHELF, SourceEntity.class);
		ShelfEntity shelf = this.em.find(ShelfEntity.class, shelfId);
		query.setParameter("shelf", shelf);

		List<SourceEntity> entities = query.getResultList();

		Function<SourceEntity, BaseSource> function = new EntityToPojo();
		return ResponseBuilder.ok(Lists.newArrayList(Iterables.transform(entities, function)));
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the source
	 */
	@GET
	@Path("{id}")
	public Response findById(@PathParam("id") String id) {
		logger.info("Sources#FindById() : {}", this.uriInfo.getAbsolutePath());

		SourceEntity source = this.em.find(SourceEntity.class, id);

		Response response;
		if (source == null) {
			response = ResponseBuilder.emptyResult();
		} else {
			response = ResponseBuilder.ok(source);
		}
		return response;
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the shelf
	 */
	@DELETE
	@Path("{id}")
	public Response deleteById(@PathParam("id") String id) {
		logger.info("Sources#deleteById() : {}", this.uriInfo.getAbsolutePath());

		try {
			SourceEntity entity = this.em.find(SourceEntity.class, id);
			if (entity == null) {
				return ResponseBuilder.notFound("source", id);
			}
			this.em.remove(entity);
			return ResponseBuilder.deleted("Source", id);
		} catch (Exception e) {
			return ResponseBuilder.fail(e);
		}
	}

}
