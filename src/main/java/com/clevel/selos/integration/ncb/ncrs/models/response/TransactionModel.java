package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("transaction")
public class TransactionModel implements Serializable {

    @XStreamAlias("trackingid")
    private String trackingid;

    @XStreamAlias("user")
    private String user;

    @XStreamAlias("transactiondate")
    private String transactiondate;

    @XStreamAlias("mediacode")
    private String mediacode;

    @XStreamAlias("disputeenquiry")
    private String disputeenquiry;

    @XStreamImplicit(itemFieldName = "id")
    private ArrayList<IdModel> id = new ArrayList<IdModel>();

    @XStreamImplicit(itemFieldName = "name")
    private ArrayList<NameModel> name = new ArrayList<NameModel>();

    @XStreamAlias("enquirypurpose")
    private String enquirypurpose;

    @XStreamAlias("enquiryamount")
    private String enquiryamount;

    @XStreamAlias("consent")
    private String consent;

    @XStreamAlias("memberref")
    private String memberref;

    @XStreamAlias("enquirydate")
    private String enquirydate;

    @XStreamAlias("enquirystatus")
    private String enquirystatus;

    @XStreamAlias("responsedate")
    private String responsedate;

    @XStreamAlias("tuefenquiry")
    private TUEFEnquiryModel tuefenquiry;

    @XStreamAlias("tuefresponse")
    private TUEFResponseModel tuefresponse;

    @XStreamAlias("tueferror")
    private TUEFErrorModel tueferror;

    public String getTrackingid() {
        return trackingid;
    }

    public String getUser() {
        return user;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public String getMediacode() {
        return mediacode;
    }

    public String getDisputeenquiry() {
        return disputeenquiry;
    }

    public ArrayList<IdModel> getId() {
        return id;
    }

    public ArrayList<NameModel> getName() {
        return name;
    }

    public String getEnquiryamount() {
        return enquiryamount;
    }

    public String getEnquirypurpose() {
        return enquirypurpose;
    }

    public String getEnquirymount() {
        return enquiryamount;
    }

    public String getConsent() {
        return consent;
    }

    public String getMemberref() {
        return memberref;
    }

    public String getEnquirydate() {
        return enquirydate;
    }

    public String getResponsedate() {
        return responsedate;
    }

    public TUEFEnquiryModel getTuefenquiry() {
        return tuefenquiry;
    }

    public TUEFResponseModel getTuefresponse() {
        return tuefresponse;
    }

    public String getEnquirystatus() {
        return enquirystatus;
    }

    public TUEFErrorModel getTueferror() {
        return tueferror;
    }

    public void setTueferror(TUEFErrorModel tueferror) {
        this.tueferror = tueferror;
    }
}
