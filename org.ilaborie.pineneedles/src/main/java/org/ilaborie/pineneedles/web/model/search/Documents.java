package org.ilaborie.pineneedles.web.model.search;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ilaborie.pineneedles.web.util.TimeUtil;

/**
 * The Class Documents.
 */
@XmlRootElement(name = "documents")
public class Documents {

	/** The results. */
	@XmlAttribute
	private long results;

	/** The size. */
	@XmlAttribute
	private int size;
	
	/** The from. */
	@XmlAttribute
	private int from;

	/** The time in nanos. */
	@XmlAttribute
	private long time;

	/** The docs. */
	@XmlElement
	private List<Document> docs;
	
	/** The facets. */
	@XmlElement
	private List<DocFacet> facets;

	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public long getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results the new results
	 */
	public void setResults(long results) {
		this.results = results;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 *
	 * @param time the new time
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * Gets the display time.
	 *
	 * @return the display time
	 */
	public String getDisplayTime() {
		return TimeUtil.formatTime(this.time,TimeUnit.NANOSECONDS);
	}

	/**
	 * Gets the docs.
	 *
	 * @return the docs
	 */
	public List<Document> getDocs() {
		return docs;
	}

	/**
	 * Sets the docs.
	 *
	 * @param docs the new docs
	 */
	public void setDocs(List<Document> docs) {
		this.docs = docs;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
    	return size;
    }

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size) {
    	this.size = size;
    }

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public int getFrom() {
    	return from;
    }

	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(int from) {
    	this.from = from;
    }

	/**
	 * Gets the facets.
	 *
	 * @return the facets
	 */
	public List<DocFacet> getFacets() {
    	return facets;
    }

	/**
	 * Sets the facets.
	 *
	 * @param facets the facets
	 */
	public void setFacets(List<DocFacet> facets) {
    	this.facets = facets;
    }

}
