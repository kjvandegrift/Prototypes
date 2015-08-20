package com.ltceng.serialization.health;

import com.codahale.metrics.health.HealthCheck;

import oracle.kv.KVStore;

public class DatabaseHealthCheck extends HealthCheck {
	private final KVStore store;
	
	public DatabaseHealthCheck(KVStore store) {
		super();
		this.store = store;
	}
	
	@Override
	protected Result check() {
		try {		
			return Result.healthy(store.getStats(false).toString());
		}
		catch (Throwable error) {
			return Result.unhealthy(error);
		}
	}

}
