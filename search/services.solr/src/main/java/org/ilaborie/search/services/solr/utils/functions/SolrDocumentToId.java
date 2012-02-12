package org.ilaborie.search.services.solr.utils.functions;

import org.apache.solr.common.SolrDocument;
import org.ilaborie.search.commons.utils.Fields;

import com.google.common.base.Function;

/**
 * The Class SolrDocumentToId.
 */
public class SolrDocumentToId implements Function<SolrDocument, String> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(SolrDocument input) {
		String id = null;
		if (input != null) {
			id = (String) input.getFirstValue(Fields.ID.getField());
		}
		return id;
	}

}
