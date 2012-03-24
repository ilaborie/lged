package org.ilaborie.pineneedles.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.ilaborie.pineneedles.web.model.IShelf;
import org.ilaborie.pineneedles.web.model.ISource;

/**
 * The Class ShelfEntity.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement(name = "source")
@NamedQueries(@NamedQuery(name = Source.QUERY_FIND_BY_SHELF, query = "From Source s Where s.shelf = :shelf"))
public class Source implements ISource {

	/** The Constant QUERY_FIND_ALL. */
	public static final String QUERY_FIND_BY_SHELF = "Source.byShelf";

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

	/** The shelf. */
	@ManyToOne
	@XmlIDREF
	private Shelf shelf;

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

	/**
	 * Gets the shelf.
	 *
	 * @return the shelf
	 */
	@Override
	public IShelf getShelf() {
		return this.shelf;
	}

	/**
	 * Sets the shelf.
	 *
	 * @param shelf the new shelf
	 */
	public void setShelf(Shelf shelf) {
		this.shelf = shelf;
	}

}
