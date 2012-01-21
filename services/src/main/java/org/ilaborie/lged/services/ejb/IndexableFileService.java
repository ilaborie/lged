package org.ilaborie.lged.services.ejb;

import java.io.File;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.ilaborie.lged.commons.model.IIndexableElement;
import org.ilaborie.lged.commons.model.IndexableFile;
import org.ilaborie.lged.commons.services.IElementService;

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
		return new IndexableFile(file);
	}

}
