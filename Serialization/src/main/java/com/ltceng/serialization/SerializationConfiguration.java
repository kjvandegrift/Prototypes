package com.ltceng.serialization;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class SerializationConfiguration extends Configuration {
    
	@Valid
    @NotNull
    @JsonProperty("hyperDexCoordinator")
    private NoSqlFactory noSqlFactory = new NoSqlFactory();

    @Valid
    @NotNull
    @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
   
    public NoSqlFactory getHyperdexFactory() {
    	return noSqlFactory;
    }
        
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }
}
