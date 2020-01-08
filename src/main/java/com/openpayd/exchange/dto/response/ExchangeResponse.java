package com.openpayd.exchange.dto.response;

import java.math.BigDecimal;

public class ExchangeResponse {
    private final long transactionId;
    private final BigDecimal exchange;

    public ExchangeResponse(long transactionId,
                            BigDecimal exchange) {
        this.transactionId = transactionId;
        this.exchange = exchange;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public BigDecimal getExchange() {
        return exchange;
    }
}
