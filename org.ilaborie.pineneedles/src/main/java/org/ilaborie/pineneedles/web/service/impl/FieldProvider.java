/*
 * 
 */
package org.ilaborie.pineneedles.web.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.IPTC;
import org.apache.tika.metadata.MSOffice;
import org.ilaborie.pineneedles.web.model.elements.BasicField;
import org.ilaborie.pineneedles.web.model.elements.DynamicField;
import org.ilaborie.pineneedles.web.model.elements.Field;
import org.ilaborie.pineneedles.web.service.IFieldProvider;
import org.slf4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * The Class FieldProvider.
 */
@Stateless
@Local(IFieldProvider.class)
public class FieldProvider implements IFieldProvider {

	/** The fields. */
	private final Map<String, Field> fields;

	/** The logger. */
	@Inject
	private Logger logger;

	/**
	 * Instantiates a new field provider.
	 */
	public FieldProvider() {
		super();
		this.fields = Maps.newHashMap();
	}

	/**
	 * Initialize fields.
	 */
	@PostConstruct
	protected void initializeFields() {
		this.fields.put(DublinCore.TITLE, BasicField.TITLE);
		this.fields.put(IPTC.TITLE_DCPROPERTY.getName(), BasicField.TITLE);
		this.fields.put("dc.title", BasicField.CREATE_DATE);

		this.fields.put(MSOffice.AUTHOR, BasicField.AUTHOR);
		this.fields.put("author", BasicField.AUTHOR);
		this.fields.put("dc.creator", BasicField.AUTHOR);

		this.fields.put(DublinCore.DATE.getName(), BasicField.CREATE_DATE);
		this.fields.put(IPTC.DATE_CREATED.getName(), BasicField.CREATE_DATE);
		this.fields.put(IPTC.DATE_CREATED.getName(), BasicField.CREATE_DATE);
		this.fields.put(MSOffice.CREATION_DATE.getName(), BasicField.CREATE_DATE);
		this.fields.put("dc.date", BasicField.CREATE_DATE);
		this.fields.put("Date", BasicField.CREATE_DATE);

		this.fields.put(MSOffice.KEYWORDS, BasicField.TAG);
		this.fields.put("keywords", BasicField.TAG);
		this.fields.put(IPTC.KEYWORDS_DCPROPERTY.getName(), BasicField.TAG);

		this.fields.put(DublinCore.TYPE, BasicField.TYPE);
		this.fields.put(HttpHeaders.CONTENT_TYPE, BasicField.TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getFields()
	 */
	@Override
	public Iterable<Field> getFields() {
		return Sets.newHashSet(fields.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getFieldByMetadata(java.lang.String)
	 */
	@Override
	public Field getFieldByMetadata(String name) {
		Field field = this.fields.get(name);
		if (field == null) {
			this.logger.warn("Key '{}' not found, create a dynamic field", name);
			field = new DynamicField(name);
			this.fields.put(name, field);
		}
		return field;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getFullTextField()
	 */
	@Override
	public Field getFullTextField() {
		return BasicField.FULL_TEXT;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getTagField()
	 */
	@Override
	public Field getTagField() {
		return BasicField.TAG;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getSourceField()
	 */
	@Override
	public Field getSourceField() {
		return BasicField.SOURCE;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getShelfField()
	 */
	@Override
	public Field getShelfField() {
		return BasicField.SHELF;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getSha1Field()
	 */
	@Override
	public Field getChecksumField() {
		return BasicField.CHECKSUM;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.service.IFieldProvider#getTypeField()
	 */
	@Override
	public Field getTypeField() {
		return BasicField.TYPE;
	}

}
