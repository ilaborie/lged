package org.ilaborie.lged.services.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.ilaborie.lged.commons.model.Shelf;
import org.ilaborie.lged.commons.model.Source;

/**
 * The Class ShelfEntity.
 */
@Entity
public class ShelfEntity implements Shelf, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5860875375890494738L;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Shelf#getSources()
	 */
	@Override
	public List<Source> getSources() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setName(String name) {
		this.name = name;
	}

}
