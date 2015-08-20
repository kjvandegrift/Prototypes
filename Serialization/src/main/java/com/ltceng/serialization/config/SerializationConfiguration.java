package com.ltceng.serialization.config;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class SerializationConfiguration extends Configuration {
    
	@Valid
    @NotNull
    @JsonProperty("noSqlStore")
    private NoSqlStoreFactory noSqlStoreFactory = new NoSqlStoreFactory();

    @Valid
    @NotNull
    @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    
	@Valid
    @NotNull
    @JsonProperty("serverName")
    private String serverName;
        
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

	public NoSqlStoreFactory getNoSqlStoreFactory() {
		return noSqlStoreFactory;
	}
	
	public String getServerName() {
		return serverName;
	}
	
}
