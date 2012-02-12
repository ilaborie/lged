package org.ilaborie.search.commons.utils.functions;

import org.ilaborie.search.commons.model.IIndexableElement;
import org.ilaborie.search.commons.services.IElementService;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

/**
 * The Class StringToElement.
 */
public class StringToElement implements Function<String, IIndexableElement> {
	
	/** The service. */
	private final IElementService service;

	/**
	 * Instantiates a new string to element.
	 *
	 * @param eltService the element service
	 */
	public StringToElement(IElementService eltService) {
		super();
		Preconditions.checkNotNull(eltService);
		this.service = eltService;
	}

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public IIndexableElement apply(String id) {
		IIndexableElement elt = null;
		if (id!=null) {
			elt = this.service.getById(id); 
		}
		return elt;
	}

}
