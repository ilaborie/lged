package org.ilaborie.lged.services.utils.functions;

import java.util.Map.Entry;

import org.apache.solr.common.SolrInputDocument;
import org.ilaborie.lged.commons.model.IIndexableElement;
import org.ilaborie.lged.commons.utils.Fields;

import com.google.common.base.Function;

/**
 * The Class SolrElementToDocumentFunction.
 */
public class ElementToSolrDocumentFunction implements
		Function<IIndexableElement, SolrInputDocument> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public SolrInputDocument apply(IIndexableElement input) {
		SolrInputDocument doc = null;
		if (input != null) {
			doc = new SolrInputDocument();
			// Add all fields
			for (Entry<Fields, ?> entry : input.getIndexableFields().entries()) {
				doc.addField(entry.getKey().getField(), entry.getValue());
			}
			// Check id if not in fields
			if(!input.getIndexableFields().containsKey(Fields.ID)) {
				doc.addField(Fields.ID.getField(), input.getId());
			}
			// File
		}
		return doc;
	}

}
