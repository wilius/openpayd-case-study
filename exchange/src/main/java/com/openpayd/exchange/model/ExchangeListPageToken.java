package com.openpayd.exchange.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ExchangeListPageToken extends PageToken {
    private final long maxId;
    private final LocalDate date;

    @JsonCreator
    public ExchangeListPageToken(@JsonProperty("maxId") long maxId,
                                 @JsonProperty("date") LocalDate date,
                                 @JsonProperty("pageNumber") int pageNumber,
                                 @JsonProperty("pageSize") int pageSize) {
        super(pageNumber, pageSize);
        this.maxId = maxId;
        this.date = date;
    }

    public long getMaxId() {
        return maxId;
    }

    public LocalDate getDate() {
        return date;
    }
}
