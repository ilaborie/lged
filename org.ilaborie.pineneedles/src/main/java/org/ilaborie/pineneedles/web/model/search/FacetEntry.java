package org.ilaborie.pineneedles.web.model.search;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ComparisonChain;

/**
 * The Class FacetEntry.
 */
@XmlRootElement(name = "entry")
public class FacetEntry implements Comparable<FacetEntry> {

	/** The value. */
	@XmlAttribute
	private String value;

	/** The count. */
	@XmlAttribute
	private int count;

	/**
	 * Instantiates a new facet entry.
	 */
	public FacetEntry() {
		super();
	}
	
	/**
	 * Instantiates a new facet entry.
	 *
	 * @param value the value
	 * @param count the count
	 */
	public FacetEntry(String value, int count) {
		this();
		this.value = value;
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(FacetEntry o) {
		return ComparisonChain.start()
		        .compare(this.count, o.count)
		        .compare(this.value, o.value)
		        .result();
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
