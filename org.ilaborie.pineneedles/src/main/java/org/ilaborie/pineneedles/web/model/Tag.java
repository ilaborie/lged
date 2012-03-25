package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ComparisonChain;

/**
 * The Class Tag.
 */
@XmlRootElement(name = "tag")
public class Tag implements Comparable<Tag> {

	/** The tag. */
	@XmlAttribute
	private String tag;

	/** The count. */
	@XmlAttribute
	private int count;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Tag o) {
		return ComparisonChain.start()
		        .compare(this.count, o.count)
		        .compare(this.tag, o.tag)
		        .result();
	}

	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 *
	 * @param tag the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
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
