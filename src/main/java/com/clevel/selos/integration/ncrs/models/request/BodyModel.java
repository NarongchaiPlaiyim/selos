package com.clevel.selos.integration.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("body")
public class BodyModel {
    
    @XStreamAlias("tuefenquiry")
    private TUEFEnquiryModel tuefenquiry;
    
    public BodyModel(TUEFEnquiryModel tuefenquiry) {
        this.tuefenquiry = tuefenquiry;
    }
    
    
}
