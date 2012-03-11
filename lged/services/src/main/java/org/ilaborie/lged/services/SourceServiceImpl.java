package org.ilaborie.lged.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.ilaborie.lged.commons.model.Shelf;
import org.ilaborie.lged.commons.model.Source;
import org.ilaborie.lged.commons.service.ISourceService;
import org.ilaborie.lged.services.entity.SourceEntity;
import org.ilaborie.search.commons.model.IIndexableElement;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * The Class SourceServiceImpl.
 */
@Stateless
public class SourceServiceImpl implements ISourceService {

	/** The log. */
	@Inject
	private Logger log;

	/** The entity manager. */
	@PersistenceContext
	private EntityManager em;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.service.ISourceService#findSourcesByShelf(org.ilaborie.lged.commons.model.Shelf)
	 */
	@Override
	public List<? extends Source> findSourcesByShelf(Shelf shelf) {
		Preconditions.checkNotNull(shelf);

		this.log.debug("Get Source by Shelf: {}", shelf);
		TypedQuery<SourceEntity> query = this.em.createNamedQuery(SourceEntity.QUERY_FIND_BY_SHELF, SourceEntity.class);
		query.setParameter("shelf", shelf);

		List<SourceEntity> result = query.getResultList();
		this.log.trace("Sources by [{}] : ", shelf, result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.service.ISourceService#fetch(org.ilaborie.lged.commons.model.Source)
	 */
	@Override
	public List<IIndexableElement> fetch(Source source) {
		return source.getAllElements();
	}

}
