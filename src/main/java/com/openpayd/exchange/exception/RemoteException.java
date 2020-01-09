package com.openpayd.exchange.exception;

import com.openpayd.exchange.dto.ErrorCode;

public class RemoteException extends ExchangeException {
    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(Throwable t) {
        super(t);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.REMOTE_EXCEPTION;
    }
}
