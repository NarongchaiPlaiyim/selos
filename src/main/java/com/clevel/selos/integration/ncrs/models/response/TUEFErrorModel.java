package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

public class TUEFErrorModel {
    @XStreamImplicit(itemFieldName = "error")
    private ArrayList<ErrorModel> error = new ArrayList<ErrorModel>();

    @XStreamAlias("responsedata")
    private ResponseDataModel responsedata;

    public ArrayList<ErrorModel> getError() {
        return error;
    }

    public ResponseDataModel getResponsedata() {
        return responsedata;
    }
}
