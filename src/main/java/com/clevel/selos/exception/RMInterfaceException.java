package com.clevel.selos.exception;

public class RMInterfaceException extends ApplicationRuntimeException {
    public RMInterfaceException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }

    public RMInterfaceException(String code, String message) {
        super(code, message);
    }
}
