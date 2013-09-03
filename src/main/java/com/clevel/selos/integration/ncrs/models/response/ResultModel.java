package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class ResultModel {
    
    @XStreamAlias("result")
    private String result;

    public ResultModel(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
    
    
}
