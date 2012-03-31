package org.ilaborie.pineneedles.web.model.elements;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.parser.html.HtmlParser;
import org.ilaborie.pineneedles.web.model.entity.LinkEntry;
import org.ilaborie.pineneedles.web.model.entity.SourceEntity;
import org.ilaborie.pineneedles.web.service.IFieldProvider;
import org.ilaborie.pineneedles.web.util.Sha1Utils;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Closeables;

import de.l3s.boilerpipe.sax.BoilerpipeHTMLContentHandler;

/**
 * The Class LinkElement.
 */
public class LinkElement implements IIndexableElement {

	/** The Constant PARSER. */
	private static final Parser PARSER = new HtmlParser();

	/** The link. */
	private final LinkEntry link;

	/** The values. */
	private Multimap<Field, Object> values;

	/**
	 * Instantiates a new link element.
	 *
	 * @param link the link
	 */
	public LinkElement(LinkEntry link) {
		super();
		this.link = link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.link.toString();
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public LinkEntry getLink() {
		return link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.IIndexableElement#getId()
	 */
	@Override
	public String getId() {
		return this.link.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.IIndexableElement#isActive()
	 */
	@Override
	public boolean isActive() {
		return this.link.isActive();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.pineneedles.web.model.elements.IIndexableElement#getIndexableFields()
	 */
	@Override
	public Multimap<Field, ?> getIndexableFields(IFieldProvider fieldProvider, SourceEntity source) {
		if (this.values == null) {
			this.values = LinkedListMultimap.create();

			InputStream stream = null;
			try {
				URL url = new URL(this.link.getLink());
				stream = url.openStream();

				Metadata metadata = new Metadata();
				StringWriter sw = new StringWriter();
				BoilerpipeHTMLContentHandler handler = new BoilerpipeContentHandler(sw);
				ParseContext context = new ParseContext();
				PARSER.parse(stream, handler, metadata, context);
				String plainText = sw.toString();
				Closeables.closeQuietly(sw);

				// Handle metadata
				Field field;
				for (String name : metadata.names()) {
					field = fieldProvider.getFieldByMetadata(name);
					this.values.putAll(field, this.extractValues(name, metadata, field));
				}

				// Handle Full Text
				this.values.put(BasicField.FULL_TEXT, plainText);

				// PineNeedles Fields
				this.addBasicFields(source, plainText);

			} catch (Exception e) {
				this.link.setActive(false);
				throw new RuntimeException(e);
			} finally {
				Closeables.closeQuietly(stream);
			}
		}
		return this.values;
	}

	/**
	 * Adds the basic fields.
	 *
	 * @param source the source
	 * @param plainText the plain text
	 */
	private void addBasicFields(SourceEntity source, String plainText) {
	    // Location
	    this.values.put(BasicField.LOCATION, this.link.getLink());

	    // Teaser
	    this.values.put(BasicField.TEASER, this.getTeaser(plainText));

	    // Tag
	    for (String tag : this.link.getTags()) {
	    	this.values.put(BasicField.TAG, tag);
	    }

	    // Source
	    this.values.put(BasicField.SOURCE, source.getId());

	    // Shelf
	    this.values.put(BasicField.SHELF, source.getShelf().getId());

	    // Type
	    this.values.put(BasicField.TYPE, Field.TYPE_URL);

	    // Checksum
	    this.values.put(BasicField.CHECKSUM, Sha1Utils.sha1(plainText));
    }

	/**
	 * Extract values.
	 *
	 * @param name the name
	 * @param metadata the metadata
	 * @param field the field
	 * @return the list
	 */
	private List<Object> extractValues(String name, Metadata metadata, Field field) {
		List<Object> lst = Lists.newArrayList();
		String value;
		if (metadata.isMultiValued(name)) {
			for (String val : metadata.getValues(name)) {
				lst.addAll(field.getValues(val));
			}
		} else {
			value = metadata.get(name);
			lst.addAll(field.getValues(value));
		}

		return lst;
	}

	/**
	 * Gets the teaser.
	 *
	 * @param plainText the plain text
	 * @return the teaser
	 */
	private String getTeaser(String plainText) {
		String teaser = null;
		if (plainText != null) {
			if (plainText.length() > Field.TEASER_LENGTH) {
				teaser = plainText.substring(0, Field.TEASER_LENGTH) + "...";
			} else {
				teaser = plainText;
			}
		}
		return teaser;
	}
}
