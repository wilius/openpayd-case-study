package com.openpayd.exchange.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.openpayd.exchange.gateway.jackson.serdes.CurrencyDeserializer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

public class ExchangeRequest {

    @NotNull
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency source;

    @NotNull
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Currency target;

    @NotNull
    @Min(0)
    private BigDecimal amount;

    public Currency getSource() {
        return source;
    }

    public Currency getTarget() {
        return target;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
