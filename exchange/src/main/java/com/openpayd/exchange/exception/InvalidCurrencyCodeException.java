package com.openpayd.exchange.exception;

import com.openpayd.exchange.dto.ErrorCode;

public class InvalidCurrencyCodeException extends ExchangeException {
    private final String currencyCode;

    public InvalidCurrencyCodeException(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String getMessage() {
        return String.format("The currency code '%s' is not valid", currencyCode);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.INVALID_CURRENCY_CODE;
    }
}
