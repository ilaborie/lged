package org.ilaborie.lged.services.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.ilaborie.lged.commons.model.Source;
import org.ilaborie.search.commons.model.IIndexableElement;

/**
 * The Class SourceEntity.
 */
@Entity
public class SourceEntity implements Source {

	/** The uuid. */
	@Id
	private String uuid;

	/**
	 * Instantiates a new shelf entity.
	 */
	@Column(length = 1024, nullable = true, unique = false)
	private String comment;

	/** The shelf. */
	@ManyToOne
	@JoinColumn(name = "fk_shelf", nullable = false)
	private ShelfEntity shelf;

	/**
	 * Instantiates a new source entity.
	 */
	public SourceEntity() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.lged.commons.model.Source#getById(java.lang.String)
	 */
	@Override
	public IIndexableElement getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.lged.commons.model.Source#getAllElements()
	 */
	@Override
	public List<IIndexableElement> getAllElements() {
		// TODO Auto-generated method stub
		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Source#getShelf()
	 */
	@Override
	public ShelfEntity getShelf() {
		return shelf;
	}

	/**
	 * Sets the shelf.
	 *
	 * @param shelf the new shelf
	 */
	public void setShelf(ShelfEntity shelf) {
		this.shelf = shelf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.lged.commons.model.Source#getComment()
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
