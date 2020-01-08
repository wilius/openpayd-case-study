package com.openpayd.exchange.dto.request;

import java.util.Currency;

public class GetExchangeRateRequest {
    private Currency source;
    private Currency target;

    public Currency getSource() {
        return source;
    }

    public void setSource(Currency source) {
        this.source = source;
    }

    public Currency getTarget() {
        return target;
    }

    public void setTarget(Currency target) {
        this.target = target;
    }
}
