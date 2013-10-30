package com.clevel.selos.integration.ncb.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("ncrsrequest")
public class NCRSRequestModel implements Serializable {

    @XStreamAlias("header")
    private HeaderModel header;

    @XStreamAlias("body")
    private BodyModel body;

    public NCRSRequestModel(HeaderModel header, BodyModel body) {
        this.header = header;
        this.body = body;
    }
}
