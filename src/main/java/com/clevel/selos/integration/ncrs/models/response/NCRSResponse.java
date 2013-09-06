package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XStreamAlias("ncrsresponse")
public class NCRSResponse {

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

