package com.openpayd.exchange.exception;

import com.openpayd.exchange.dto.ErrorCode;

public class InvalidRequestException extends ExchangeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.INVALID_REQUEST;
    }
}
