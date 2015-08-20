package com.ltceng.serialization;

import javax.ws.rs.client.Client;

import com.ltceng.serialization.config.SerializationClientConfiguration;
import com.ltceng.serialization.resources.SequenceResource;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SerializationClientApplication extends Application<SerializationClientConfiguration> {
	public static void main(final String[] args) throws Exception {
		new SerializationClientApplication().run(args);
	}

	@Override
	public String getName() {
		return "SerializationClient1Application";
	}

	@Override
	public void initialize(final Bootstrap<SerializationClientConfiguration> bootstrap) {
	}

	@Override
	public void run(final SerializationClientConfiguration configuration, final Environment environment) {
		final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
				.build(configuration.getClientName());
		environment.jersey().register(new SequenceResource(client, configuration.getIterations(), configuration.getApplicationServerConfiguration()));
	}

}
