package org.ilaborie.lged.commons.utils.predicates;

import org.ilaborie.lged.commons.model.IIndexableElement;

import com.google.common.base.Predicate;

/**
 * The Class IndexableElementWithFile.
 */
public class IndexableElementWithFile implements Predicate<IIndexableElement> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(IIndexableElement input) {
		return (input != null) && (input.getFile() != null);
	}

}
