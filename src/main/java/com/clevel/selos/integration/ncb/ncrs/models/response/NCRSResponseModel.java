package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("ncrsresponse")
public class NCRSResponseModel implements Serializable {

    @XStreamAlias("header")
    private HeaderModel header;

    @XStreamAlias("body")
    private BodyModel body;

    public HeaderModel getHeaderModel() {
        return header;
    }

    public BodyModel getBodyModel() {
        return body;
    }
}

