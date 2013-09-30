package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

public class TUEFErrorModel implements Serializable {
    @XStreamAlias("error")
    private TUEFErrorError error;

    @XStreamAlias("responsedata")
    private ResponseDataModel responsedata;

    public TUEFErrorError getError() {
        return error;
    }

    public ResponseDataModel getResponsedata() {
        return responsedata;
    }
}
