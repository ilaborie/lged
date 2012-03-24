package org.ilaborie.pineneedles.web.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ShelfEntity.
 */
@Entity
@XmlRootElement(name = "links")
public class SourceLinks extends Source {

	/** The links. */
	@OneToMany
	private List<LinkEntry> links;

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public List<LinkEntry> getLinks() {
		return links;
	}

	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(List<LinkEntry> links) {
		this.links = links;
	}

}
