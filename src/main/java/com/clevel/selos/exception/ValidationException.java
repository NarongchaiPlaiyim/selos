package com.clevel.selos.exception;

public class ValidationException extends ApplicationRuntimeException {
    public ValidationException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }

    public ValidationException(String code, String message) {
        super(code, message);
    }

    public ValidationException(String message) {
        super(message);
    }
}
