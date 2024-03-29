package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class H2HResponseHeaderModel implements Serializable {
    private String result;
    private String total;
    private String message;
    private String memberref;

    public String getResult() {
        return result;
    }

    public String getTotal() {
        return total;
    }

    public String getMessage() {
        return message;
    }

    public String getMemberref() {
        return memberref;
    }

}
