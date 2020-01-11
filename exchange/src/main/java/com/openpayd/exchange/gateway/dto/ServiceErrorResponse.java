package com.openpayd.exchange.gateway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceErrorResponse {
    private final String error;

    @JsonCreator
    public ServiceErrorResponse(@JsonProperty("error") String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
