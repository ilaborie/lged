package org.ilaborie.pineneedles.web.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.ilaborie.pineneedles.web.model.elements.BasicField;
import org.ilaborie.pineneedles.web.model.elements.Field;
import org.ilaborie.pineneedles.web.model.elements.IIndexableElement;
import org.ilaborie.pineneedles.web.model.elements.LinkElement;
import org.ilaborie.pineneedles.web.model.entity.LinkEntry;
import org.ilaborie.pineneedles.web.model.entity.LinksSourceEntity;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.service.IFieldProvider;
import org.ilaborie.pineneedles.web.service.IIndexElementProvider;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;

/**
 * The Class LinkElementProvider.
 */
@Stateless
@Local(IIndexElementProvider.class)
public class LinkElementProvider implements IIndexElementProvider {

	/**
	 * The Class EntityToElement.
	 */
	private final class EntityToElement implements Function<LinkEntry, IIndexableElement> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public IIndexableElement apply(LinkEntry input) {
			return input != null ? new LinkElement(input) : null;
		}
	}

	/** The field provider. */
	@Inject
	private IFieldProvider fieldProvider;

	/** The entity manager. */
	@Inject
	private EntityManager em;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.service.IIndexElementProvider#canProcessSource(org.ilaborie.pineneedles.web.model.entity.SourceEntity)
	 */
	@Override
	public boolean canProcessSource(SourceEntity source) {
		return (source != null) && (source instanceof LinksSourceEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.service.IIndexElementProvider#processSource(org.ilaborie.pineneedles.web.model.entity.SourceEntity)
	 */
	@Override
	public Iterator<IIndexableElement> processSource(SourceEntity source) {
		assert source != null;
		assert source instanceof LinksSourceEntity;

		LinksSourceEntity links = (LinksSourceEntity) source;

		Function<LinkEntry, IIndexableElement> function = new EntityToElement();
		return Iterators.transform(links.getLinks().iterator(), function);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.service.IIndexElementProvider#updateEntity(org.ilaborie.pineneedles.web.model.elements.IIndexableElement,
	 * org.ilaborie.pineneedles.web.model.entity.SourceEntity)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateEntity(IIndexableElement element, SourceEntity source) {
		assert element != null;
		assert element instanceof LinkElement;

		LinkElement linkElt = (LinkElement) element;
		LinkEntry entity = linkElt.getLink();

		Multimap<Field, ?> fields = element.getIndexableFields(this.fieldProvider, source);

		// Title
		Collection<?> titles = fields.get(BasicField.TITLE);
		if (titles != null && !titles.isEmpty()) {
			String title = (String) titles.iterator().next();
			if (Strings.isNullOrEmpty(title)) {
				entity.setTitle(title);
			}
		}

		// Update Date
		Collection<?> dates = fields.get(BasicField.CREATE_DATE);
		if (dates != null && !dates.isEmpty()) {
			Date date = (Date) dates.iterator().next();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			entity.setDate(cal);
		}

		// Update Tags
		Collection<?> tags = fields.get(BasicField.TAG);
		if (tags != null && !tags.isEmpty()) {
			Set<String> set = entity.getTags();
			set.clear();
			for (Object obj : tags) {
				set.add((String) obj);
			}
		}

		// Update
		this.em.merge(entity);

	}
}
