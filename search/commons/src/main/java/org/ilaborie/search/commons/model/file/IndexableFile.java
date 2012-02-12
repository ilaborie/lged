package org.ilaborie.search.commons.model.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.ilaborie.search.commons.model.IIndexableElement;
import org.ilaborie.search.commons.utils.Fields;
import org.ilaborie.search.commons.utils.ShaUtils;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Closeables;

/**
 * The Class IndexableFile.
 */
public class IndexableFile implements IIndexableElement {

	@Inject
	private Logger log;

	/** The file. */
	private final File file;

	/** The categories. */
	private final List<String> categories;

	/** The id. */
	private String id;

	/** The SHA-1. */
	private String sha;

	/** The fields. */
	private Multimap<Fields, Object> fields;

	/**
	 * Instantiates a new indexable file.
	 * 
	 * @param file
	 *            the file
	 * @param categories
	 */
	public IndexableFile(File file, List<String> categories) {
		super();
		Preconditions.checkNotNull(file);
		Preconditions.checkArgument(file.exists(), "File " + file
				+ " does not exists !");
		Preconditions.checkArgument(file.canRead(), "File " + file
				+ " is not readable !");
		this.categories = Lists.newArrayList(categories);
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

	/**
	 * Gets the categories.
	 * 
	 * @return the categories
	 */
	public List<String> getCategories() {
		return ImmutableList.copyOf(this.categories);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ilaborie.lged.commons.model.IIndexableElement#getIndexableFields()
	 */
	@Override
	public synchronized Multimap<Fields, ?> getIndexableFields() {
		if (this.fields == null) {
			this.fields = HashMultimap.create();
			this.fields.put(Fields.ID, this.getId());
			this.fields.put(Fields.SHA1, this.getSha());

			// Categories
			this.fields.putAll(Fields.CATEGORY, this.categories);

			// Add metadata with Tika
			this.addTikaMetadata();
		}
		return this.fields;
	}

	/**
	 * Adds the tika metadata.
	 */
	private void addTikaMetadata() {
		assert this.fields != null;

		Tika tika = new Tika();
		tika.setMaxStringLength(-1);

		Metadata metadata = new Metadata();
		String plainText = "";

		FileInputStream stream = null;
		try {
			stream = new FileInputStream(this.file);
			plainText = tika.parseToString(stream, metadata);

			// Full Text
			this.fields.put(Fields.FULL_TEXT, plainText);

			// Metadata
			Fields field;
			String value;
			for (String name : metadata.names()) {
				try {
					field = Fields.fromString(name);
					value = metadata.get(name);
					if (Strings.isNullOrEmpty(value)) {
						this.fields.put(field, name);
					}
				} catch (IllegalArgumentException e) {
					log.warn(e.getLocalizedMessage());
				}
			}
		} catch (IOException e) {
			log.error("I/O error", e);
		} catch (TikaException e) {
			log.error("Tika error", e);
		} finally {
			Closeables.closeQuietly(stream);
		}
	}

}
