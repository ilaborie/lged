package org.ilaborie.lged.services.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.ilaborie.lged.commons.model.Shelf;

/**
 * The Class ShelfEntity.
 */
@Entity
@NamedQueries(
		@NamedQuery(name = ShelfEntity.QUERY_FIND_ALL, query = "From ShelfEntity"))
public class ShelfEntity implements Shelf, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5860875375890494738L;

	/** The Constant QUERY_FIND_ALL. */
	public static final String QUERY_FIND_ALL = "ShelfEntity.findAll";

	/** The uuid. */
	@Id
	private String uuid;

	/**
	 * Instantiates a new shelf entity.
	 */
	@Column(length = 255, nullable = false, unique = true)
	private String name;

	/**
	 * Instantiates a new shelf entity.
	 */
	public ShelfEntity() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Shelf#getId()
	 */
	@Override
	public String getId() {
		return this.uuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Shelf#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
