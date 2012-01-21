package org.ilaborie.lged.commons.utils;

/**
 * The Enum Fields.
 */
public enum Fields {

	/** The identifier field. */
	ID("id"),

	/** The SHA-1 field */
	SHA1("sha1"),

	/** The name field. */
	NAME("name"),

	/** The full text field. */
	FULL_TEXT("text"),

	/** The category field. */
	CATEGORY("category"),

	/** The title field. */
	TITLE("title"),

	/** The subject field. */
	SUBJECT("subject"),

	/** The description field. */
	DESCRIPTION("description"),

	/** The comments field. */
	COMMENTS("comments"),

	/** The author field. */
	AUTHOR("author"),

	/** The keywords field. */
	KEYWORDS("keywords"),

	/** The content type field. */
	CONTENT_TYPE("content_type"),

	/** The last modified field. */
	LAST_MODIFIED("last_modified"),

	/** The links field. */
	LINKS("links");

	/** The field. */
	private final String field;

	/**
	 * Instantiates a new fields.
	 * 
	 * @param field
	 *            the field
	 */
	private Fields(String field) {
		this.field = field;
	}

	/**
	 * Gets the field.
	 * 
	 * @return the field
	 */
	public String getField() {
		return field;
	}

}
