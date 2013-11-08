package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    private String trackingid;
    private String user;
    private String transactiondate;
    private String mediacode;
    private String inquirypurpose;
    private String inquirystatus;
    private String confirmconsent;
    private String language;
    private String producttype;
    private String memberref;
    private String requestdate;
    private String responsedate;
    private H2HRequestModel h2hrequest;
    private AttributeModel attribute;
    private H2HResponseModel h2hresponse;
    private H2HErrorModel h2herror;

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

    public String getInquirypurpose() {
        return inquirypurpose;
    }

    public String getInquirystatus() {
        return inquirystatus;
    }

    public String getConfirmconsent() {
        return confirmconsent;
    }

    public String getLanguage() {
        return language;
    }

    public String getProducttype() {
        return producttype;
    }

    public String getMemberref() {
        return memberref;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public String getResponsedate() {
        return responsedate;
    }

    public H2HRequestModel getH2hrequest() {
        return h2hrequest;
    }

    public AttributeModel getAttribute() {
        return attribute;
    }

    public H2HResponseModel getH2hresponse() {
        return h2hresponse;
    }

    public H2HErrorModel getH2herror() {
        return h2herror;
    }
}
