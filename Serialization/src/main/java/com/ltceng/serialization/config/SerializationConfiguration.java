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
    private StoreFactory noSqlStoreFactory = new StoreFactory();

    @Valid
    @NotNull
    @JsonProperty("httpClient")
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    
	@Valid
    @NotNull
    @JsonProperty("appName")
    private String appName;
        
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

	public StoreFactory getNoSqlStoreFactory() {
		return noSqlStoreFactory;
	}
	
	public String getAppName() {
		return appName;
	}
	
}
