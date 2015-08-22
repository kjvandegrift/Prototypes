package com.ltceng.serialization;

import javax.ws.rs.client.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ltceng.serialization.config.SerializationClientConfiguration;
import com.ltceng.serialization.health.ConnectionHealthCheck;
import com.ltceng.serialization.resources.SequenceResource;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SerializationClientApplication extends Application<SerializationClientConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializationClientApplication.class);

	public static void main(final String[] args) throws Exception {
		new SerializationClientApplication().run(args);
	}

	@Override
	public void initialize(final Bootstrap<SerializationClientConfiguration> bootstrap) {
	}

	@Override
	public void run(final SerializationClientConfiguration configuration, final Environment environment) {
		final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
				.build(configuration.getClientName());
		environment.jersey()
				.register(new SequenceResource(client, configuration.getInterations(), configuration.getMinPause(),
						configuration.getMaxPause(), configuration.getApplicationServerConfiguration()));
		environment.healthChecks().register(configuration.getClientName(),
				new ConnectionHealthCheck(client, configuration.getApplicationServerConfiguration()));
		LOGGER.info("{} started.", configuration.getClientName());
	}
}
