package com.clevel.selos.integration.ncb.ncrs.models.response;

import java.io.Serializable;

public class ErrorModel implements Serializable {
    private String value;
    private String description;
    private String code;

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

}
