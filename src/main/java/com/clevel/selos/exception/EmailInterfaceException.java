package com.clevel.selos.exception;

public class EmailInterfaceException extends ApplicationRuntimeException {
    public EmailInterfaceException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }
}
