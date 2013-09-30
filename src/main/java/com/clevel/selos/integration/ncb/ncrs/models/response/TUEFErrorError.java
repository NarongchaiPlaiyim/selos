package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

public class TUEFErrorError implements Serializable {
    @XStreamImplicit(itemFieldName = "error")
    private ArrayList<ErrorModel> error = new ArrayList<ErrorModel>();

    public ArrayList<ErrorModel> getError() {
        return error;
    }
}
