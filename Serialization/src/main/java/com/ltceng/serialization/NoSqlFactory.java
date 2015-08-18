package com.ltceng.serialization;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

import org.hyperdex.client.Client;

public class NoSqlFactory {
    @NotEmpty
    private String host;

    @Min(1)
    @Max(65535)
    private int port = 1982;

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }
    
    public Client build(Environment environment) {
    	Client client = new Client(getHost(), getPort());
    	environment.lifecycle().manage(new Managed() {
    		@Override
    		public void start() {
    			
    		}
    		
    		@Override
    		public void stop() {
    			
    		}
    	});
		return client;
    }
}
