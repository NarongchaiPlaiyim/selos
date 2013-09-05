package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("transaction")
public class TransactionModel {
    
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
    
    @XStreamAlias("id")
    private IdModel id;
    
    @XStreamAlias("name")
    private NameModel name;
    
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

    public TransactionModel(String trackingid, String user, String transactiondate, String mediacode, String disputeenquiry, IdModel id, NameModel name, String enquirypurpose, String enquirymount, String consent, String memberref, String enquirydate, String responsedate, TUEFEnquiryModel tuefenquiry, TUEFResponseModel tuefresponse) {
        this.trackingid = trackingid;
        this.user = user;
        this.transactiondate = transactiondate;
        this.mediacode = mediacode;
        this.disputeenquiry = disputeenquiry;
        this.id = id;
        this.name = name;
        this.enquirypurpose = enquirypurpose;
        this.enquiryamount = enquirymount;
        this.consent = consent;
        this.memberref = memberref;
        this.enquirydate = enquirydate;
        this.responsedate = responsedate;
        this.tuefenquiry = tuefenquiry;
        this.tuefresponse = tuefresponse;
    }

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

    public IdModel getId() {
        return id;
    }

    public NameModel getName() {
        return name;
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
    
}
