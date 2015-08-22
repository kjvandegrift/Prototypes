package com.ltceng.serialization.resources;

import java.net.SocketTimeoutException;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ltceng.serialization.config.ApplicationServerConfiguration;
import com.ltceng.serialization.core.Sequence;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class SequenceResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceResource.class);
	private Client client;
	private int iterations;
	private int minPause;
	private int maxPause;
	private ApplicationServerConfiguration applicationServerConfiguration;
	private static final String ALPHA_SEQUENCE_PATH = "alpha";
	private static final String DIGIT_SEQUENCE_PATH = "digit";
	private static final String CONNECTION_ERROR = "CONNECTION_ERROR";

	public SequenceResource(Client client, int iterations, int minPause, int maxPause,
			ApplicationServerConfiguration applicationServerConfiguration) {
		this.client = client;
		this.iterations = iterations;
		this.minPause = minPause;
		this.maxPause = maxPause;
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

		Random rand = new Random();
		for (int i = 0; i < iterations; i++) {
			String sequenceVal = CONNECTION_ERROR;
			WebTarget webTarget = null;
			Sequence sequence = null;
			if (rand.nextBoolean()) {
				try {
					webTarget = client.target(getAppRootPath() + ALPHA_SEQUENCE_PATH);
					sequence = webTarget.request().get(Sequence.class);
					sequenceVal = sequence.getSequence();
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			} else {
				try {
					webTarget = client.target(getAppRootPath() + DIGIT_SEQUENCE_PATH);
					sequence = webTarget.request().get(Sequence.class);
					sequenceVal = String.valueOf(sequence.getSequence());
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}
			retValue.append(sequenceVal);
			retValue.append(System.getProperty("line.separator"));
			randomSleep();
		}
		return retValue.toString();

	}

	private void randomSleep() {
		Random rand = new Random();

		try {
			Thread.sleep(rand.nextInt(maxPause) + minPause);
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
