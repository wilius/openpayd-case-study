package com.openpayd.exchange.exception;

import com.openpayd.exchange.dto.ErrorCode;

public abstract class ExchangeException extends RuntimeException {
    public ExchangeException() {
    }

    public ExchangeException(String message) {
        super(message);
    }

    public ExchangeException(Throwable cause) {
        super(cause);
    }

    public abstract ErrorCode getErrorCode();
}
