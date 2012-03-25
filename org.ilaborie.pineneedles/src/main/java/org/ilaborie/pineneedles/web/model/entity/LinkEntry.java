package org.ilaborie.pineneedles.web.model.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> tags;

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		Iterable<String> split = Splitter.on("/").omitEmptyStrings().trimResults().split(this.link);
		String site = Iterables.get(split, 1);
		String page = Iterables.getLast(split);

		// Handle site
		List<String> sites = Lists.newArrayList(Splitter.on('.').split(site));
		switch (sites.size()) {
			case 2:
				site = sites.get(0);
				break;
			case 3:
				site = sites.get(1);
				break;
			default:
				break;
		}
		
		// Handle last segment
		page = Joiner.on(' ').join(Splitter.on('-').split(page));
		if (page.length()>1) {
			page = page.substring(0, 1).toUpperCase() + page.substring(1).toLowerCase();
		}
		return String.format("[%1$s] %2$s", site, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkEntry other = (LinkEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
