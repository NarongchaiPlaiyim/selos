package com.clevel.selos.integration.ncb.nccrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("ncrsrequest")
public class NCCRSRequestModel implements Serializable {

    @XStreamAlias("header")
    private HeaderModel header;

    @XStreamAlias("body")
    private BodyModel body;

    public NCCRSRequestModel(HeaderModel header, BodyModel body) {
        this.header = header;
        this.body = body;
    }
}
