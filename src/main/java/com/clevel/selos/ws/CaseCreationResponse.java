package com.clevel.selos.ws;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CaseCreationResponse implements Serializable {
    int code;
    String message;
    String appRefNumber;

    public CaseCreationResponse() {
    }

    public CaseCreationResponse(int code, String message, String appRefNumber) {
        this.code = code;
        this.message = message;
        this.appRefNumber = appRefNumber;
    }

    public void setValue(WSResponse wsResponse, String message, String appRefNumber) {
        setCode(wsResponse.code());
        setMessage(message);
        setAppRefNumber(appRefNumber);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public void setAppRefNumber(String appRefNumber) {
        this.appRefNumber = appRefNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("code", code).
                append("message", message).
                append("appRefNumber", appRefNumber).
                toString();
    }
}
