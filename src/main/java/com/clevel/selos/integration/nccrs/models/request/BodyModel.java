package com.clevel.selos.integration.nccrs.models.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("body")
public class BodyModel {
    
    @XStreamAlias("h2hrequest")
    private H2HRequestModel h2hrequest;
    
    @XStreamAlias("h2hrequest")
    private AttributeModel attribute;

    public BodyModel(H2HRequestModel h2hrequest, AttributeModel attribute) {
        this.h2hrequest = h2hrequest;
        this.attribute = attribute;
    }
}
