package com.clevel.selos.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class ApplicationRuntimeException extends RuntimeException {
    String code;
    String message;

    public ApplicationRuntimeException(Throwable cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    protected ApplicationRuntimeException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    protected ApplicationRuntimeException(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("code", code).
                append("message", message).
                toString();
    }
}
