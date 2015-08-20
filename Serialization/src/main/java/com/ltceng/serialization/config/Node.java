package com.ltceng.serialization.config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {

	@NotEmpty
	@JsonProperty
	private String hostName;
	
	@Min(1)
	@Max(65535)
	@JsonProperty
	private int port = 5000;
	
	@JsonProperty
	public String getHostName() {
		return this.hostName;
	}
	
	@JsonProperty
	public int getPort() {
		return this.port;
	}
	
	public Node getNode() {
		return new Node();
	}
	
}
