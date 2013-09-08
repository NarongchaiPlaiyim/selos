package com.clevel.selos.integration.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

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
