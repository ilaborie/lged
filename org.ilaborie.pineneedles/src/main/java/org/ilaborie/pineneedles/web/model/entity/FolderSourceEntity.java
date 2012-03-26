package org.ilaborie.pineneedles.web.model.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ShelfEntity.
 */
@Entity
@XmlRootElement(name = "folder")
public class FolderSourceEntity extends SourceEntity {

	/** The recursive. */
	@XmlAttribute
	private boolean recursive;

	/** The folder. */
	@XmlElement
	private String folder;

	/**
	 * Instantiates a new source folder.
	 */
	public FolderSourceEntity() {
		super();
	}

	/**
	 * Checks if is recursive.
	 *
	 * @return true, if is recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * Sets the recursive.
	 *
	 * @param recursive the new recursive
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

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

}
