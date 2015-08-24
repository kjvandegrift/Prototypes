package com.ltceng.serialization.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ltceng.serialization.core.Sequence;
import com.ltceng.serialization.db.SequenceDAO;

import io.dropwizard.jersey.params.NonEmptyStringParam;

@Path(value = "/sequences")
@Produces(MediaType.APPLICATION_JSON)
public class SequenceResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceResource.class);
	private static final String TYPE_ALPHA = "alpha";
	private static final String TYPE_DIGIT = "digit";
	private final SequenceDAO sequenceDAO;

	public SequenceResource(SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	@POST
	@Timed
	@ExceptionMetered
	@Path("/{type}")
	public Sequence initSequence(@PathParam("type") NonEmptyStringParam type) {
		try {
			LOGGER.trace("Init SequenceResource Type: {}", type.get().orNull());
			if (type.get().orNull().equals(TYPE_ALPHA)) {
				return sequenceDAO.initAlphaSequence();
			} else if (type.get().orNull().equals(TYPE_DIGIT)) {
				return sequenceDAO.initDigitSequence();
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.error("initSequence failed to complete due to error: {}", e.getMessage());
			throw new WebApplicationException(Status.NOT_MODIFIED);
		}
	}

	@GET
	@Timed
	@ExceptionMetered
	@Path("/{type}")
	public Sequence updateSequence(@PathParam("type") NonEmptyStringParam type) {
		LOGGER.trace("Update SequenceResource Type: {}", type.get().orNull());
		try {
			if (type.get().orNull().equals(TYPE_ALPHA)) {
				return sequenceDAO.getNextAlphaSequence();
			} else if (type.get().orNull().equals(TYPE_DIGIT)) {
				return sequenceDAO.getNextDigitSequence();
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.error("updateSequence failed to complete due to error: {}", e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

}
