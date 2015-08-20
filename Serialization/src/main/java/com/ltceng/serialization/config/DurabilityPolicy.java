package com.ltceng.serialization.config;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DurabilityPolicy {

	@Pattern(regexp = "NO_SYNC|WRITE_NO_SYNC|SYNC")
	@JsonProperty("masterSyncPolicy")
	private String masterSyncPolicy;
	
	@Pattern(regexp = "NO_SYNC|WRITE_NO_SYNC|SYNC")
	@JsonProperty("replicaSyncPolicy")
	private String replicaSyncPolicy;

	@Pattern(regexp = "NONE|SIMPLE_MAJORITY|ALL")
	@JsonProperty("replicaAckPolicy")
	private String replicaAckPolicy;

	public String getMasterSyncPolicy() {
		return masterSyncPolicy;
	}
	
	public String getReplicaSyncPolicy() {
		return replicaSyncPolicy;
	}

	public String getReplicaAckPolicy() {
		return replicaAckPolicy;
	}

}
