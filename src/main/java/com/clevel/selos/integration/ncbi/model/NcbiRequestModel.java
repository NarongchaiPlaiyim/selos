package com.clevel.selos.integration.ncbi.model;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;

@XStreamAlias("NCBIREQUEST")
public class NcbiRequestModel {

    @XStreamImplicit(itemFieldName = "REQUEST")
    private ArrayList<RequestModel> REQUEST = new ArrayList<RequestModel>();

    public NcbiRequestModel(ArrayList<RequestModel> REQUEST) {
        this.REQUEST = REQUEST;
    }
}
