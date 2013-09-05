package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("responsedata")
public class Responsedata {
    
    @XStreamAlias("texttuefresponse")
    private String texttuefresponse;

    public Responsedata(String texttuefresponse) {
        this.texttuefresponse = texttuefresponse;
    }

    public String getTexttuefresponse() {
        return texttuefresponse;
    }
    
}
