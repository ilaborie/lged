package org.ilaborie.pineneedles.web.model.entity;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.ilaborie.pineneedles.web.util.func.Capitalize;

import com.google.common.base.Splitter;
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
	
	/** The host. */
	@XmlElement
	private String host;
	
	/** The title. */
	@XmlElement
	private String title;
	
	/** The date. */
	@XmlElement
	@Temporal(TemporalType.DATE)
	private Calendar date;

	/** The tags. */
	@XmlElement
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="tag")
	private Set<String> tags;

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayHost() {
		// Handle site
		String site;
		List<String> sites = Lists.newArrayList(Splitter.on('.').split(this.host));
		switch (sites.size()) {
			case 2:
				site = Capitalize.FUNCTION.apply(sites.get(0));
				break;
			case 3:
				site = Capitalize.FUNCTION.apply(sites.get(1));
				break;
			default:
				site = this.host;
				break;
		}
		return site;
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

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
    	return host;
    }

	/**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
	public void setHost(String host) {
    	this.host = host;
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
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Calendar getDate() {
    	return date;
    }

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Calendar date) {
    	this.date = date;
    }

}
