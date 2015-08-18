package com.ltceng.serialization;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

public class SerializationClientConfiguration extends Configuration {
	@NotNull
	@Max(1000)
	private int iterations = 1;
	
	@JsonProperty
	public int getIterations() {
		return iterations;
	}
	
	@Valid
    @NotNull
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    
    @JsonProperty("httpClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }
    
    @Valid
    @NotNull
    @JsonProperty("clientName")
    private String clientName;
    
    
    public String getClientName() {
    	return clientName;
    }
       
    @Valid
    @NotNull
    @JsonProperty("applicationServer")
    private ApplicationServerConfiguration applicationServerConfiguration = new ApplicationServerConfiguration();
    
    
    public ApplicationServerConfiguration getApplicationServerConfiguration() {
    	return applicationServerConfiguration;
    }    
}
