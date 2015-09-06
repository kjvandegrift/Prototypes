package com.ltceng.serialization.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;

import oracle.kv.KVStore;

public class DatabaseHealthCheck extends HealthCheck {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHealthCheck.class);
	private final KVStore store;

	public DatabaseHealthCheck(KVStore store) {
		super();
		this.store = store;
	}

	@Override
	protected Result check() {
		try {
			store.getStats(false).toString();
			LOGGER.info("Health check returned: {}", "true");
			return Result.healthy();
		} catch (Throwable error) {
			LOGGER.error("Health check returned: {}", error.getMessage());
			return Result.unhealthy(error);
		}
	}

}
