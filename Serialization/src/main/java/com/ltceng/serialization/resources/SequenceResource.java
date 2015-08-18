package com.ltceng.serialization.resources;

import java.util.concurrent.TimeoutException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.jersey.params.NonEmptyStringParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.ltceng.serialization.core.Sequence;
import com.ltceng.serialization.db.SequenceDAO;

@Path(value = "/sequences/{type}")
@Produces(MediaType.APPLICATION_JSON)
public class SequenceResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceResource.class);
	private final SequenceDAO sequenceDAO;
	
	
    public SequenceResource(SequenceDAO sequenceDAO) {
        this.sequenceDAO = sequenceDAO;
    }
    
    @GET
    @Timed
    @ExceptionMetered
    public Sequence updateSequence(@PathParam("type") NonEmptyStringParam type) {
    	LOGGER.trace("SequenceResource Type: {}",type.get().orNull());
    	try {
			return sequenceDAO.updateSequence(type.get().orNull());
		} catch (TimeoutException e) {
			LOGGER.error("updateSequence failed to complete due to timeout error: {}", e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		}
    }	
	
}
