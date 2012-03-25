package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class BaseSource.
 */
@XmlRootElement(name = "source")
public abstract class BaseSource {
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
	
	/**
	 * Gets the kind.
	 *
	 * @return the kind
	 */
	public abstract String getKind();
	
	/**
	 * Gets the kind label.
	 *
	 * @return the kind label
	 */
	public abstract String getKindLabel();
	
	/**
	 * Gets the detail.
	 *
	 * @return the detail
	 */
	public abstract String getDetail();

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

}
