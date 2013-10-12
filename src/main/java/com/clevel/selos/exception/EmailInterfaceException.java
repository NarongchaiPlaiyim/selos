package com.clevel.selos.exception;

import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;

import javax.inject.Inject;

public class EmailInterfaceException extends ApplicationRuntimeException {
    public EmailInterfaceException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }

    public EmailInterfaceException(String code, String message) {
        super(code, message);
    }

}
