package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ncrsresponse")
public class NCCRSResponseModel {

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
