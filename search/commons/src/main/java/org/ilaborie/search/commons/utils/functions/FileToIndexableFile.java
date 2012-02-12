/*
 * 
 */
package org.ilaborie.search.commons.utils.functions;

import java.io.File;
import java.util.List;

import org.ilaborie.search.commons.model.file.IndexableFile;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * The Class FileToIndexableFile.
 */
public class FileToIndexableFile implements Function<File, IndexableFile> {

	/** The categories. */
	private final List<String> categories;

	/**
	 * Instantiates a new file to indexable file.
	 *
	 * @param categories the categories
	 */
	public FileToIndexableFile(String... categories) {
		super();
		this.categories = Lists.newArrayList(categories);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public IndexableFile apply(File input) {
		IndexableFile file = null;
		if (input != null) {
			file = new IndexableFile(input, this.categories);
		}
		return file;
	}

}
