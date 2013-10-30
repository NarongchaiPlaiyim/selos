package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("responsedata")
public class Responsedata implements Serializable {

    @XStreamAlias("texttuefresponse")
    private String texttuefresponse;

    public Responsedata(String texttuefresponse) {
        this.texttuefresponse = texttuefresponse;
    }

    public String getTexttuefresponse() {
        return texttuefresponse;
    }

}
