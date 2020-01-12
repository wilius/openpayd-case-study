package com.openpayd.tools.healthchecker.actuator.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Health {
    private final Status status;
    private final Map<String, Object> details;

    @JsonCreator
    public Health(@JsonProperty("status") Status status,
                  @JsonProperty("details") Map<String, Object> details) {
        this.status = status;
        this.details = details;
    }

    public enum Status {
        UNKNOWN,
        UP,
        DOWN,
        OUT_OF_SERVICE
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
