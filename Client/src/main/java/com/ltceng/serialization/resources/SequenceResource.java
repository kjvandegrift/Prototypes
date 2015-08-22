package com.ltceng.serialization.resources;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.ltceng.serialization.config.ApplicationServerConfiguration;
import com.ltceng.serialization.core.Sequence;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class SequenceResource {
	private Client client;
	private ApplicationServerConfiguration applicationServerConfiguration;
	private static final String ALPHA_SEQUENCE_PATH = "alpha";
	private static final String DIGIT_SEQUENCE_PATH = "digit";

	public SequenceResource(Client client, ApplicationServerConfiguration applicationServerConfiguration) {
		this.client = client;
		this.applicationServerConfiguration = applicationServerConfiguration;
	}

	@GET
	@Path("getAlphaSequence")
	public String getAlphaSequence() {
		WebTarget target = client.target(getAppRootPath() + ALPHA_SEQUENCE_PATH);
		Sequence sequence = target.request().get(Sequence.class);
		return sequence.getSequence();
	}

	@GET
	@Path("getDigitSequence")
	public String getDigitSequence() {
		WebTarget target = client.target(getAppRootPath() + DIGIT_SEQUENCE_PATH);
		Sequence sequence = target.request().get(Sequence.class);
		return sequence.getSequence();
	}

	private String getAppRootPath() {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(applicationServerConfiguration.getHost());
		sb.append(":");
		sb.append(applicationServerConfiguration.getPort());
		sb.append("/sequences/");
		return sb.toString();
	}

}
