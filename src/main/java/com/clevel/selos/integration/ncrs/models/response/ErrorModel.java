package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

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
