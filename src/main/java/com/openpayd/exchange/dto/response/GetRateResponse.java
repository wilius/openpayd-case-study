package com.openpayd.exchange.dto.response;

import java.math.BigDecimal;

public class GetRateResponse {
    private final BigDecimal rate;

    public GetRateResponse(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
