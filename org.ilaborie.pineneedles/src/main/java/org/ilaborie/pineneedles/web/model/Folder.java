package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Folder.
 */
@XmlRootElement(name="folder")
public class Folder {

	/** The id. */
	@XmlID
	@XmlAttribute
	private String id;

	/** The shelf id. */
	@XmlAttribute
	private String shelfId;

	/** The name. */
	@XmlAttribute
	private String name;

	/** The description. */
	@XmlElement
	private String description;

	/** The recursive. */
	@XmlElement
	private boolean recursive;

	/** The path. */
	@XmlElement
	private String path;
	
	/**
	 * Instantiates a new folder.
	 */
	public Folder() {
		super();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.ISource#getId()
	 */
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the shelf id.
	 *
	 * @return the shelf id
	 */
	public String getShelfId() {
		return shelfId;
	}

	/**
	 * Sets the shelf id.
	 *
	 * @param shelfId the new shelf id
	 */
	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.ISource#getName()
	 */
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.ISource#getDescription()
	 */
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
