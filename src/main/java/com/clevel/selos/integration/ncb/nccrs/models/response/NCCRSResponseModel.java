package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("ncrsresponse")
public class NCCRSResponseModel implements Serializable {

    @XStreamAlias("header")
    private HeaderModel header;

    @XStreamAlias("body")
    private BodyModel body;

    public HeaderModel getHeader() {
        return header;
    }

    public BodyModel getBody() {
        return body;
    }

}
