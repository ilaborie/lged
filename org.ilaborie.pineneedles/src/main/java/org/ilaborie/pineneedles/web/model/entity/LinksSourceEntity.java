package org.ilaborie.pineneedles.web.model.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ShelfEntity.
 */
@Entity
@XmlRootElement(name = "links")
@NamedQueries(@NamedQuery(name = LinksSourceEntity.QUERY_FIND_CONTAINS_LINK, query = "From LinksSourceEntity s Where :link Member Of s.links"))
public class LinksSourceEntity extends SourceEntity {
	/** The Constant QUERY_FIND_CONTAINS_LINK. */
	public static final String QUERY_FIND_CONTAINS_LINK = "Source.withLink";

	/** The links. */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@OrderBy("title DESC")
	private Set<LinkEntry> links;

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public Set<LinkEntry> getLinks() {
		return links;
	}

	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(Set<LinkEntry> links) {
		this.links = links;
	}

}
