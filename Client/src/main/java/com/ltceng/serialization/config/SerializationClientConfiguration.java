package com.ltceng.serialization.config;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class SerializationClientConfiguration extends Configuration {
	@Valid
    @NotNull
    @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }
    
    @Valid
    @NotNull
    @JsonProperty("appName")
    private String appName;
    
    
    public String getAppName() {
    	return appName;
    }
    
    @Valid
    @Min(1)
    @Max(1000)
    @JsonProperty("iterations")
    private int iterations;
    
    public int getInterations() {
    	return iterations;
    }
    
    @Valid
    @Min(0)
    private int minPause;
    
    public int getMinPause() {
    	return minPause;
    }
    
    @Valid
    @Max(5000)
    private int maxPause;
    
    public int getMaxPause() {
    	return maxPause;
    }
       
    @Valid
    @NotNull
    @JsonProperty("applicationServer")
    private ApplicationServerConfiguration applicationServerConfiguration = new ApplicationServerConfiguration();
    
    
    public ApplicationServerConfiguration getApplicationServerConfiguration() {
    	return applicationServerConfiguration;
    }    
}
