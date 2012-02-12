package org.ilaborie.search.services.solr.ejb;

import java.io.File;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.ilaborie.search.commons.model.IIndexableElement;
import org.ilaborie.search.commons.model.file.IndexableFile;
import org.ilaborie.search.commons.services.IElementService;

@Stateless
@Local(IElementService.class)
public class IndexableFileService implements IElementService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.services.IElementService#getById(java.lang.
	 * String)
	 */
	@Override
	public IIndexableElement getById(String id) {
		File file = new File(id);
		// TODO how could I retrieve categories ???
		return new IndexableFile(file, null);
	}

}
