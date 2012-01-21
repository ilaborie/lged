package org.ilaborie.lged.commons.model;

import java.io.File;
import java.io.IOException;

import org.ilaborie.lged.commons.utils.Fields;
import org.ilaborie.lged.commons.utils.ShaUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * The Class IndexableFile.
 */
public class IndexableFile implements IIndexableElement {

	/** The file. */
	private final File file;

	/** The id. */
	private String id;

	/** The SHA-1. */
	private String sha;

	/**
	 * Instantiates a new indexable file.
	 * 
	 * @param file
	 *            the file
	 */
	public IndexableFile(File file) {
		super();
		Preconditions.checkNotNull(file);
		Preconditions.checkArgument(file.exists(), "File " + file
				+ " does not exists !");
		Preconditions.checkArgument(file.canRead(), "File " + file
				+ " is not readable !");
		this.file = file;
	}

	/**
	 * Gets the file.
	 * 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.IIndexableElement#getId()
	 */
	@Override
	public synchronized String getId() {
		if (this.id == null) {
			try {
				this.id = this.file.getCanonicalPath();
			} catch (IOException e) {
				throw new RuntimeException("Cannot get file canonical path", e);
			}
		}
		return this.id;
	}

	/**
	 * Gets the SHA-1.
	 * 
	 * @return the SHA-1
	 */
	public synchronized String getSha() {
		if (this.sha == null) {
			this.sha = ShaUtils.sha(this.file);
		}
		return sha;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.model.IIndexableElement#getIndexableFields()
	 */
	@Override
	public Multimap<Fields, ?> getIndexableFields() {
		Multimap<Fields, Object> map = HashMultimap.create();
		map.put(Fields.ID, this.getId());
		map.put(Fields.SHA1, this.getSha());
		
		return map;
	}

}
