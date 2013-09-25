package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

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
