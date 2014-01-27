package com.clevel.selos.ws;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CheckCriteriaResponse {
    String code;
    String message;

    public CheckCriteriaResponse(){

    }

    public CheckCriteriaResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setValue(WSBRMSResponse wsResponse, String message) {
        setCode(wsResponse.code());
        setMessage(message);
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("code", code)
                .append("message", message)
                .toString();
    }
}
