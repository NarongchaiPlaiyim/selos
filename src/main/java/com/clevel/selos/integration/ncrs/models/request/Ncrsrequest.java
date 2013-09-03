package com.clevel.selos.integration.ncrs.models.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XStreamAlias("ncrsrequest")
public class Ncrsrequest {
    
    @XStreamAlias("header")
    private HeaderModel header;
    
    @XStreamAlias("body")
    private BodyModel body;

    public Ncrsrequest(HeaderModel header, BodyModel body){
        this.header =  header;
        this.body = body;
    }
}
