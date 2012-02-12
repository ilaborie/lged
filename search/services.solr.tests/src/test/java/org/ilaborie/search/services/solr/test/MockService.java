package org.ilaborie.search.services.solr.test;

import java.util.Map;

import javax.enterprise.inject.Typed;
import javax.inject.Singleton;

import org.ilaborie.search.commons.model.IIndexableElement;
import org.ilaborie.search.commons.services.IElementService;

import com.google.common.collect.Maps;

/**
 * The Class MockService.
 */
@Singleton
@Typed(IElementService.class)
public class MockService implements IElementService {
	
	/** The map. */
	protected static final Map<String,MockIndexableElement> MAP = Maps.newHashMap();

	/* (non-Javadoc)
	 * @see org.ilaborie.lged.commons.services.IElementService#getById(java.lang.String)
	 */
	@Override
	public IIndexableElement getById(String id) {
		return MAP.get(id);
	}

}
