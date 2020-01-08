package com.openpayd.exchange.gateway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

public class ExchangeRateResponse {
    private final String base;

    private final Map<Currency, BigDecimal> rates;

    private final LocalDate date;

    @JsonCreator
    public ExchangeRateResponse(@JsonProperty("base") String base,
                                @JsonProperty("rates") Map<Currency, BigDecimal> rates,
                                @JsonProperty("date") LocalDate date) {
        this.base = base;
        this.rates = rates;
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public Map<Currency, BigDecimal> getRates() {
        return rates;
    }

    public LocalDate getDate() {
        return date;
    }
}
