package org.ilaborie.pineneedles.web.model;

/**
 * The Class Message.
 */
public class Message {
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new message.
	 */
	public Message() {
		super();
    }
	
	/**
	 * Instantiates a new message.
	 *
	 * @param msg the msg
	 */
	public Message(String msg) {
		this();
		this.message = msg;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
    	return message;
    }

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
    	this.message = message;
    }
	
	

}
