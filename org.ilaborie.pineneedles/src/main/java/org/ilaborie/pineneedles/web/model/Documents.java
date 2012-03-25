package org.ilaborie.pineneedles.web.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Documents.
 */
@XmlRootElement(name = "documents")
public class Documents {

	/** The results. */
	@XmlAttribute
	private int results;

	/** The page. */
	@XmlAttribute
	private int page;

	/** The time. */
	@XmlAttribute
	private long time;

	/** The docs. */
	@XmlElement
	private List<Document> docs;

	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public int getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results the new results
	 */
	public void setResults(int results) {
		this.results = results;
	}

	/**
	 * Gets the page.
	 *
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets the page.
	 *
	 * @param page the new page
	 */
	public void setPage(int page) {
		this.page = page;
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

}
