package org.ilaborie.pineneedles.web.model.entity;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class LinkEntry.
 */
@Entity
@XmlRootElement(name = "link")
public class LinkEntry {

	/** The id. */
	@Id
	@XmlID
	@XmlAttribute
	private String id;

	/** The link. */
	@XmlAttribute
	private String link;

	/** The tags. */
	@XmlElement
	@ElementCollection
	private Set<String> tags;

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
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * Sets the tags.
	 *
	 * @param tags the new tags
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

}
