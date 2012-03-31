package org.ilaborie.pineneedles.web.model.search;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * The Class Facet.
 */
@XmlRootElement(name = "facet")
public class DocFacet {

	/** The facet. */
	@XmlAttribute
	private String name;

	/** The entries. */
	@XmlElement
	private List<FacetEntry> entries;

	public DocFacet() {
		super();
		this.entries = Lists.newArrayList();
    }
	
	public DocFacet(String name) {
		this();
		this.name = name;
    }

	/**
	 * Gets the facet.
	 *
	 * @return the facet
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the facet.
	 *
	 * @param facet the new facet
	 */
	public void setName(String facet) {
		this.name = facet;
	}

	/**
	 * Gets the entries.
	 *
	 * @return the entries
	 */
	public List<FacetEntry> getEntries() {
		return entries;
	}

	/**
	 * Sets the entries.
	 *
	 * @param entries the new entries
	 */
	public void setEntries(List<FacetEntry> entries) {
		this.entries = entries;
	}

}
