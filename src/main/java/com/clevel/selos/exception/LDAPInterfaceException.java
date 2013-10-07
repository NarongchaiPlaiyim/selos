package com.clevel.selos.exception;

public class LDAPInterfaceException extends ApplicationRuntimeException {
    public LDAPInterfaceException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }
}
