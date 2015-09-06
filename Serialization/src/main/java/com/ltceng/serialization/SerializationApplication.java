package com.ltceng.serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.ltceng.serialization.config.SerializationConfiguration;
import com.ltceng.serialization.db.SequenceDAO;
import com.ltceng.serialization.health.DatabaseHealthCheck;
import com.ltceng.serialization.resources.SequenceResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import oracle.kv.KVStore;

public class SerializationApplication extends Application<SerializationConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializationApplication.class);
	private MetricRegistry metrics;

	public static void main(final String[] args) throws Exception {
		new SerializationApplication().run(args);
	}

	@Override
	public void initialize(final Bootstrap<SerializationConfiguration> bootstrap) {
		metrics = bootstrap.getMetricRegistry();
	}

	@Override
	public void run(final SerializationConfiguration configuration, final Environment environment) {
		final KVStore store = configuration.getNoSqlStoreFactory().build(environment);
		final SequenceDAO sequenceDAO = new SequenceDAO(store, configuration.getNoSqlStoreFactory().getMaxTimeoutWait(),
				configuration.getNoSqlStoreFactory().getAttemptTimeoutWait(), metrics);
		environment.jersey().register(new SequenceResource(sequenceDAO));
		environment.healthChecks().register(configuration.getAppName(), new DatabaseHealthCheck(store));
		LOGGER.info("{} started.", configuration.getAppName());
	}

}
