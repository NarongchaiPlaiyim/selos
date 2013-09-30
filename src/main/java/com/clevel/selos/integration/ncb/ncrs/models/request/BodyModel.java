package com.clevel.selos.integration.ncb.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("body")
public class BodyModel implements Serializable{
    
    @XStreamAlias("tuefenquiry")
    private TUEFEnquiryModel tuefenquiry;

    @XStreamAlias("criteria")
    private CriteriaModel criteria;

    @XStreamAlias("trackingid")
    private String trackingid;

    public BodyModel(TUEFEnquiryModel tuefenquiry) {
        this.tuefenquiry = tuefenquiry;
    }

    public BodyModel(CriteriaModel criteria) {
        this.criteria = criteria;
    }

    public BodyModel(String trackingid) {
        this.trackingid = trackingid;
    }
}
