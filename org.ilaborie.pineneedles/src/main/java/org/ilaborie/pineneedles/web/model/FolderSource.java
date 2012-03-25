package org.ilaborie.pineneedles.web.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Folder.
 */
@XmlRootElement(name = "folder")
public class FolderSource extends BaseSource {

	/** The recursive. */
	@XmlElement
	private boolean recursive;

	/** The path. */
	@XmlElement
	private String path;

	/**
	 * Instantiates a new folder.
	 */
	public FolderSource() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.model.BaseSource#getDetail()
	 */
	@Override
	public String getDetail() {
	    return this.getPath();
	}
	
	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.model.BaseSource#getKind()
	 */
	@Override
	public String getKind() {
	    return "warning";
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.pineneedles.web.model.BaseSource#getKindLabel()
	 */
	@Override
	public String getKindLabel() {
	    return "Folder";
	}

	/**
	 * Checks if is recursive.
	 *
	 * @return true, if is recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * Sets the recursive.
	 *
	 * @param recursive the new recursive
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
