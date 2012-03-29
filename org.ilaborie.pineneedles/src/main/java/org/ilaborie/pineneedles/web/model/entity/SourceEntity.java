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

/**
 * The Class ShelfEntity.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement(name = "source")
@NamedQueries({
		@NamedQuery(name = SourceEntity.QUERY_FIND_ALL, query = "From SourceEntity"),
		@NamedQuery(name = SourceEntity.QUERY_FIND_BY_SHELF, query = "From SourceEntity s Where s.shelf = :shelf"),
		@NamedQuery(name = SourceEntity.QUERY_DELETE_BY_SHELF, query = "Delete From SourceEntity s Where s.shelf = :shelf")})
public class SourceEntity {

	/** The Constant QUERY_FIND_ALL. */
	public static final String QUERY_FIND_ALL = "Source.all";
	
	/** The Constant QUERY_FIND_ALL. */
	public static final String QUERY_FIND_BY_SHELF = "Source.byShelf";
	
	/** The Constant QUERY_DELETE_BY_SHELF. */
	public static final String QUERY_DELETE_BY_SHELF = "Source.deleteByShelf";

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
	private ShelfEntity shelf;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return this.name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		SourceEntity other = (SourceEntity) obj;
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
		return this.id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
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
	public ShelfEntity getShelf() {
		return this.shelf;
	}

	/**
	 * Sets the shelf.
	 *
	 * @param shelf the new shelf
	 */
	public void setShelf(ShelfEntity shelf) {
		this.shelf = shelf;
	}

}
