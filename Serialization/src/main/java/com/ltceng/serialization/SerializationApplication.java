package com.ltceng.serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.ltceng.serialization.db.SequenceDAO;
import com.ltceng.serialization.resources.SequenceResource;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.hyperdex.client.Client;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SerializationApplication extends Application<SerializationConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializationApplication.class);
	private static MetricRegistry metrics;

	public static void main(final String[] args) throws Exception {
		new SerializationApplication().run(args);
	}

	@Override
	public String getName() {
		return "Serialization1";
	}

	@Override
	public void initialize(final Bootstrap<SerializationConfiguration> bootstrap) {
		metrics = bootstrap.getMetricRegistry();	
	}

	@Override
	public void run(final SerializationConfiguration configuration, final Environment environment) {
		Client client = configuration.getHyperdexFactory().build(environment);
		final SequenceDAO sequenceDAO = new SequenceDAO(client, metrics);
		environment.jersey().register(new SequenceResource(sequenceDAO));
	}

}
