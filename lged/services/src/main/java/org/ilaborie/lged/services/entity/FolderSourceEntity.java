package org.ilaborie.lged.services.entity;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.ilaborie.lged.services.functions.StringToPattern;
import org.ilaborie.search.commons.model.IIndexableElement;

import com.google.common.collect.Lists;

/**
 * The Class FolderSourceEntity.
 */
@Entity
public class FolderSourceEntity extends SourceEntity {

	/** The folder. */
	@Column(length = 1024, nullable = false, unique = false)
	private String folder;

	/** The recursive. */
	private boolean recurcive;

	/** The excludes. */
	@ElementCollection
	@CollectionTable(name="folder_exclude")
	private List<String> excludes;

	/** The includes. */
	@ElementCollection
	@CollectionTable(name="folder_include")
	private List<String> includes;

	/** The exclude patterns. */
	@Transient
	private List<Pattern> excludesPatterns;

	/** The include patterns. */
	@Transient
	private List<Pattern> includesPatterns;

	/**
	 * Instantiates a new folder source entity.
	 */
	public FolderSourceEntity() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return String.format("[Folder] %s", this.folder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Source#getById(java.lang.String)
	 */
	@Override
	public IIndexableElement getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Source#getAllElements()
	 */
	@Override
	public List<IIndexableElement> getAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the exclude patterns.
	 *
	 * @return the exclude patterns
	 */
	public synchronized List<Pattern> getExcludePatterns() {
		if (this.excludesPatterns==null) {
			this.excludesPatterns = Lists.transform(this.excludes, new StringToPattern());
		}
		return this.excludesPatterns;
	}

	/**
	 * Gets the include patterns.
	 *
	 * @return the include patterns
	 */
	public synchronized List<Pattern> getIncludePatterns() {
		if (this.includesPatterns==null) {
			this.includesPatterns = Lists.transform(this.includes, new StringToPattern());
		}
		return includesPatterns;
	}

	// Getter & Setters

	/**
	 * Gets the folder.
	 *
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Sets the folder.
	 *
	 * @param folder the new folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Checks if is recurcive.
	 *
	 * @return true, if is recurcive
	 */
	public boolean isRecurcive() {
		return recurcive;
	}

	/**
	 * Sets the recurcive.
	 *
	 * @param recurcive the new recurcive
	 */
	public void setRecurcive(boolean recurcive) {
		this.recurcive = recurcive;
	}

	/**
	 * Gets the excludes.
	 *
	 * @return the excludes
	 */
	public List<String> getExcludes() {
		return excludes;
	}

	/**
	 * Sets the excludes.
	 *
	 * @param excludes the new excludes
	 */
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	/**
	 * Gets the includes.
	 *
	 * @return the includes
	 */
	public List<String> getIncludes() {
		return includes;
	}

	/**
	 * Sets the includes.
	 *
	 * @param includes the new includes
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}
}
