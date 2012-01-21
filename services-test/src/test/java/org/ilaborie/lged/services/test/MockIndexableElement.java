package org.ilaborie.lged.services.test;

import java.io.File;

import org.ilaborie.lged.commons.model.IIndexableElement;
import org.ilaborie.lged.commons.utils.Fields;
import org.ilaborie.lged.commons.utils.ShaUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * The Class MockIndexableElement.
 */
public class MockIndexableElement implements IIndexableElement {

	/** The name. */
	private final String name;

	/** The id. */
	private final String id;

	/**
	 * Instantiates a new mock solr indexable element.
	 * 
	 * @param name
	 *            the name
	 */
	public MockIndexableElement(String name) {
		super();
		Preconditions.checkNotNull(name);
		assert name != null;
		this.name = name;
		// Create SHA-1
		this.id = ShaUtils.sha(name);

		// XXX ugly registering
		MockService.MAP.put(this.id, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.ISolrIndexableElement#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.model.IIndexableElement#getIndexableFields()
	 */
	@Override
	public Multimap<Fields, ?> getIndexableFields() {
		Multimap<Fields, String> map = HashMultimap.create();
		map.put(Fields.ID, this.id);
		map.put(Fields.NAME, this.name);
		return map;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.lged.commons.model.IIndexableElement#getFile()
	 */
	@Override
	public File getFile() {
		return null;
	}

}
