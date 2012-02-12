package org.ilaborie.search.services.solr.utils.functions;

import org.apache.solr.client.solrj.response.TermsResponse.Term;

import com.google.common.base.Function;

/**
 * The Class TermToString.
 */
public class TermToString implements Function<Term, String> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public String apply(Term input) {
		String term = null;
		if (input != null) {
			term = input.getTerm();
		}
		return term;
	}

}
