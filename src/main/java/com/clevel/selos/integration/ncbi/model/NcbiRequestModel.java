package com.clevel.selos.integration.ncbi.model;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

@XStreamAlias("NCBIREQUEST")
public class NcbiRequestModel {
    @XStreamAlias("REQUEST")
    private RequestModel request;

    public NcbiRequestModel(RequestModel request) {
        this.request = request;
    }
}
