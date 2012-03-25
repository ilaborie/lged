/*
 * 
 */
package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Folder.
 */
@XmlRootElement(name = "links")
public class LinksSource extends BaseSource {

	/** The links. */
	@XmlElement
	private String links;

	/**
	 * Instantiates a new folder.
	 */
	public LinksSource() {
		super();
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public String getLinks() {
		return links;
	}

	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(String links) {
		this.links = links;
	}

}
