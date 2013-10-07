package com.clevel.selos.exception;

public class SafeWatchInterfaceException extends ApplicationRuntimeException {
    public SafeWatchInterfaceException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }
}
