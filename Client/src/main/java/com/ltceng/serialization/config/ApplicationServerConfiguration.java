package com.ltceng.serialization.config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationServerConfiguration {
    @NotEmpty
    @JsonProperty("host")
    private String host = "127.0.0.1";

    @Min(1)
    @Max(65535)
    @JsonProperty("port")
    private int port = 8080;

    @Min(1)
    @Max(65535)
    @JsonProperty("healthCheckPort")
    private int healthCheckPort = 8081;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    public int getHealthCheckPort() {
        return healthCheckPort;
    }
}
