package com.ltceng.serialization.config;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsistencyPolicy {

	@Pattern(regexp = "NONE_REQUIRED_NO_MASTER|NONE_REQUIRED|TIME|ABSOLUTE")
    @JsonProperty("type")
    private String type;

	@JsonProperty
	private long permissibleLag;
	
	@Pattern(regexp = "DAYS|HOURS|MICROSECONDS|MILLISECONDS|MINUTES|NANOSECONDS|SECONDS")
	@JsonProperty
	private String permissibleLagUnit;
	
	@JsonProperty
	private long timeout = 0L;
	
	@Pattern(regexp = "DAYS|HOURS|MICROSECONDS|MILLISECONDS|MINUTES|NANOSECONDS|SECONDS")
	@JsonProperty
	private String timeoutUnit;

	public String getType() {
		return type;
	}
	
	public long getPermissibleLag() {
		return permissibleLag;
	}

	public String getPermissibleLagUnit() {
		return permissibleLagUnit;
	}

	public long getTimeout() {
		return timeout;
	}

	public String getTimeoutUnit() {
		return timeoutUnit;
	}

}
