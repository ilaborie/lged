/*
 * 
 */
package org.ilaborie.lged.commons.utils.functions;

import java.io.File;

import org.ilaborie.lged.commons.model.IndexableFile;

import com.google.common.base.Function;

/**
 * The Class FileToIndexableFile.
 */
public class FileToIndexableFile implements Function<File, IndexableFile> {

	/* (non-Javadoc)
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public IndexableFile apply(File input) {
		IndexableFile file = null;
		if (input != null) {
			file = new IndexableFile(input);
		}
		return file;
	}

}
