package com.openpayd.exchange.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.openpayd.exchange.gateway.jackson.serdes.CurrencyDeserializer;

import javax.validation.constraints.NotNull;
import java.util.Currency;

public class GetRateRequest {
    @NotNull
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency source;

    @NotNull
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency target;

    public Currency getSource() {
        return source;
    }

    public Currency getTarget() {
        return target;
    }
}
