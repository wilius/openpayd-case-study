package com.openpayd.exchange.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;

public class ExchangeDto {
    private final long id;
    private final ZonedDateTime exchangeDate;
    private final Currency sourceCurrency;
    private final Currency targetCurrency;
    private final BigDecimal rate;
    private final BigDecimal calculated;

    public ExchangeDto(long id,
                       ZonedDateTime exchangeDate,
                       Currency sourceCurrency,
                       Currency targetCurrency,
                       BigDecimal rate,
                       BigDecimal calculated) {

        this.id = id;
        this.exchangeDate = exchangeDate;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.calculated = calculated;
    }

    public long getId() {
        return id;
    }

    public ZonedDateTime getExchangeDate() {
        return exchangeDate;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getCalculated() {
        return calculated;
    }
}
