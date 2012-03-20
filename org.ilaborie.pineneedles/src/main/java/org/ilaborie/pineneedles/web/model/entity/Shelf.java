package org.ilaborie.pineneedles.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.ilaborie.pineneedles.web.model.IShelf;

/**
 * The Class ShelfEntity.
 */
@Entity
@XmlRootElement(name = "shelf")
@NamedQueries(@NamedQuery(name = Shelf.QUERY_FIND_ALL, query = "From Shelf"))
public class Shelf implements IShelf {
	
	/** The Constant QUERY_FIND_ALL. */
	public static final String QUERY_FIND_ALL = "Shelf.all";

	/** The id. */
	@Id
	@XmlID
	@XmlAttribute
	private String id;

	/** The name. */
	@XmlAttribute
	private String name;

	/** The description. */
	@XmlElement
	private String description;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.Shelf#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.Shelf#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.Shelf#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
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
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
