package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

@XStreamAlias("body")
public class BodyModel {

    @XStreamAlias("transaction")
    private TransactionModel transaction;

    @XStreamImplicit(itemFieldName = "trackingid")//The response of TS01001
    private ArrayList<String> trackingid = new ArrayList<String>();

    @XStreamAlias("result")
    private String result;

    @XStreamAlias("errormsg")
    private String errormsg;

    public TransactionModel getTransaction() {
        return transaction;
    }

    public String getResult() {
        return result;
    }

    public ArrayList<String> getTrackingid() {
        return trackingid;
    }

    public String getErrormsg() {
        return errormsg;
    }
}
