package com.clevel.selos.integration.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("body")
public class BodyModel {
    
    @XStreamAlias("transaction")
    private TransactionModel transaction;

    @XStreamAlias("trackingid")
    private String trackingid;

    @XStreamAlias("result")
    private String result;

    @XStreamAlias("errormsg")
    private String errormsg;

    public TransactionModel getTransaction() {
        return transaction;
    }

    public String getTrackingid() {
        return trackingid;
    }

    public String getResult() {
        return result;
    }

    public String getErrormsg() {
        return errormsg;
    }
}
