package com.ltceng.serialization.health;

import javax.ws.rs.client.Client;

import com.codahale.metrics.health.HealthCheck;
import com.ltceng.serialization.config.ApplicationServerConfiguration;

public class ConnectionHealthCheck extends HealthCheck {

	private Client client;
	private ApplicationServerConfiguration applicationServerConfiguration;

	public ConnectionHealthCheck(Client client, ApplicationServerConfiguration applicationServerConfiguration) {
		this.client = client;
		this.applicationServerConfiguration = applicationServerConfiguration;
	}

	@Override
	protected Result check() {
		try {
			int status = client.target(getAppRootPath()).request().get().getStatus();
			if (status == 200) {
				return Result.healthy();
			} else
				return Result.unhealthy("Response Status: " + status);
		} catch (Throwable error) {
			return Result.unhealthy(error);
		}
	}

	private String getAppRootPath() {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(applicationServerConfiguration.getHost());
		sb.append(":");
		sb.append(applicationServerConfiguration.getHealthCheckPort());
		sb.append("/ping");
		return sb.toString();
	}

}
