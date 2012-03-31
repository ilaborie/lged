package org.ilaborie.pineneedles.web.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ilaborie.pineneedles.web.model.Message;

/**
 * The Class ResponseBuilder.
 */
public final class ResponseBuilder {

	/**
	 * Instantiates a new response builder.
	 */
	private ResponseBuilder() {
		super();
	}

	/**
	 * Null argument.
	 *
	 * @param argName the arg name
	 * @return the response
	 */
	public static final Response nullArgument(String argName) {
		Message msg = new Message(String.format("'%1$s' parameter must not be null !", argName));
		return Response.status(Status.BAD_REQUEST)
		        .entity(msg)
		        .build();
	}

	/**
	 * Not found.
	 *
	 * @param eltName the elt name
	 * @param id the id
	 * @return the response
	 */
	public static final Response notFound(String eltName, String id) {
		Message msg = new Message(String.format("The %1$s with identifier %2$s was not found !", eltName, id));
		return Response.status(Status.BAD_REQUEST)
		        .entity(msg)
		        .build();
	}

	/**
	 * Fail.
	 *
	 * @param e the e
	 * @return the response
	 */
	public static final Response fail(Exception e) {
		Message msg = new Message(e.getLocalizedMessage());
		msg.setCause(e);
		return Response.status(Status.INTERNAL_SERVER_ERROR)
		        .entity(msg)
		        .build();
	}

	/**
	 * Created.
	 *
	 * @param entity the entity
	 * @return the response
	 */
	public static final Response created(Object entity) {
		return Response.status(Status.CREATED)
		        .entity(entity)
		        .build();
	}

	/**
	 * Created.
	 *
	 * @param eltName the elt name
	 * @param id the id
	 * @return the response
	 */
	public static final Response deleted(String eltName, String id) {
		 Message msg = new Message(String.format("%1$s deleted: %2$s",eltName, id));
		return Response.ok(msg)
		        .build();
	}

	/**
	 * Empty result.
	 *
	 * @return the response
	 */
	public static final Response emptyResult() {
		return Response.status(Status.NO_CONTENT)
		        .build();
	}

	/**
	 * Ok.
	 *
	 * @param entity the entity
	 * @return the response
	 */
	public static final Response ok(Object entity) {
		return Response.ok(entity)
		        .build();
	}

}
