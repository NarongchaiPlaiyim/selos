package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("body")
public class BodyModel {
    
    @XStreamAlias("transaction")
    private TransactionModel transaction;
    
    @XStreamAlias("errormsg")
    private String errormsg;

    //@XStreamAlias("trackingid")
    private String sTrackingid;
    
    //@XStreamAlias("result")
    private String sResult;
    
    //@XStreamImplicit(itemFieldName = "trackingid")
    private ArrayList<TrackingIdModel> trackingidd = new ArrayList<TrackingIdModel>();

    @XStreamImplicit(itemFieldName = "trackingid")//The response of TS01001
    private ArrayList<String> trackingid = new ArrayList<String>();
    
    
    @XStreamImplicit(itemFieldName = "result")
    private ArrayList<ResultModel> result = new ArrayList<ResultModel>();
    
    
    public BodyModel(TransactionModel transaction, String errormsg) {
        this.transaction = transaction;
        this.errormsg = errormsg;
    }

    public TransactionModel getTransaction() {
        return transaction;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public ArrayList<ResultModel> getResult() {
        return result;
    }

    public String getsTrackingid() {
        return sTrackingid;
    }

    public String getsResult() {
        return sResult;
    }

    public ArrayList<TrackingIdModel> getTrackingidd() {
        return trackingidd;
    }

    public ArrayList<String> getTrackingid() {
        return trackingid;
    }
    
    
}
