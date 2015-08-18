package com.ltceng.serialization.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ltceng.serialization.ApplicationServerConfiguration;
import com.ltceng.serialization.core.Sequence;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class SequenceResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceResource.class);
	private int iterations;
	private Client client;
	private ApplicationServerConfiguration applicationServerConfiguration;
	private static final String ALPHA_SEQUENCE_PATH = "alpha";
	private static final String DIGIT_SEQUENCE_PATH = "digit";

	public SequenceResource(Client client, int iterations,
			ApplicationServerConfiguration applicationServerConfiguration) {
		this.client = client;
		this.iterations = iterations;
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

	@GET
	@Path("startSimulator")
	public String startSimulator() {
		StringBuilder retValue = new StringBuilder();
		WebTarget alphaTarget = client.target(getAppRootPath() + ALPHA_SEQUENCE_PATH);
		WebTarget digitTarget = client.target(getAppRootPath() + DIGIT_SEQUENCE_PATH);
		Random rand = new Random();
		for (int i = 0; i < iterations; i++) {
			String sequence;
			if (rand.nextBoolean()) {
				Sequence alphaSequence = alphaTarget.request().get(Sequence.class);
				sequence = alphaSequence.getSequence();
			} else {
				Sequence digitSequence = digitTarget.request().get(Sequence.class);
				sequence = String.valueOf(digitSequence.getSequence());
			}
			retValue.append(sequence);
			retValue.append(System.getProperty("line.separator"));
			randomSleep(1000);
		}
		return retValue.toString();

	}

	private void randomSleep(int maxMilliSeconds) {
		Random rand = new Random();

		try {
			Thread.sleep(rand.nextInt(maxMilliSeconds));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
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
