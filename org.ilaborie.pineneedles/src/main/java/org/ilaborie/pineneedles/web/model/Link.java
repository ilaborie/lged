package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Link.
 */
@XmlRootElement(name = "link")
public class Link {

	/** The id. */
	@XmlAttribute
	private String id;

	/** The source id. */
	@XmlAttribute
	private String sourceId;

	/** The link. */
	@XmlAttribute
	private String link;

	/** The tags. */
	@XmlElement
	private String tags;

	/**
	 * Gets the source id.
	 *
	 * @return the source id
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * Sets the source id.
	 *
	 * @param sourceId the new source id
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets the link.
	 *
	 * @param link the new link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * Sets the tags.
	 *
	 * @param tags the new tags
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

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

}
