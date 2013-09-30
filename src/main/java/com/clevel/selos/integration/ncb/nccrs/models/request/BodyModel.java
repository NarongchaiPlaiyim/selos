package com.clevel.selos.integration.ncb.nccrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("body")
public class BodyModel implements Serializable {
    
    @XStreamAlias("h2hrequest")
    private H2HRequestModel h2hrequest;
    
    @XStreamAlias("h2hrequest")
    private AttributeModel attribute;

    @XStreamAlias("criteria")
    private CriteriaModel criteria;

    @XStreamAlias("trackingid")
    private String trackingid;

    public BodyModel(H2HRequestModel h2hrequest, AttributeModel attribute) {
        this.h2hrequest = h2hrequest;
        this.attribute = attribute;
    }

    public BodyModel(CriteriaModel criteria) {
        this.criteria = criteria;
    }

    public BodyModel(String trackingid) {
        this.trackingid = trackingid;
    }
}
