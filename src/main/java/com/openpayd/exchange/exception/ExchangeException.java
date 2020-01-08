package com.openpayd.exchange.exception;

import com.openpayd.exchange.dto.ErrorCode;

public abstract class ExchangeException extends RuntimeException {
    public abstract ErrorCode getErrorCode();
}
