package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Document.
 */
@XmlRootElement(name = "document")
public class Document {

	/** The id. */
	@XmlAttribute
	@XmlID
	private String id;

	/** The title. */
	@XmlElement
	private String title;

	/** The location. */
	@XmlElement
	private String location;

	/** The teaser. */
	@XmlElement
	private String teaser;

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
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the teaser.
	 *
	 * @return the teaser
	 */
	public String getTeaser() {
		return teaser;
	}

	/**
	 * Sets the teaser.
	 *
	 * @param teaser the new teaser
	 */
	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}

}
