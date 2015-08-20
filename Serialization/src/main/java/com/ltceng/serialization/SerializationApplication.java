package com.ltceng.serialization;

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
	private static MetricRegistry metrics;

	public static void main(final String[] args) throws Exception {
		new SerializationApplication().run(args);
	}

	@Override
	public void initialize(final Bootstrap<SerializationConfiguration> bootstrap) {
		metrics = bootstrap.getMetricRegistry();	
	}

	@Override
	public void run(final SerializationConfiguration configuration, final Environment environment) {
		KVStore store = configuration.getNoSqlStoreFactory().build(environment);
		final SequenceDAO sequenceDAO = new SequenceDAO(store, metrics);
		environment.jersey().register(new SequenceResource(sequenceDAO));
		environment.healthChecks().register(configuration.getServerName(), new DatabaseHealthCheck(store));
	}

}
