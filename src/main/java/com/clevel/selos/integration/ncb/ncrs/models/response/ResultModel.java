package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("result")
public class ResultModel  implements Serializable {
    
    @XStreamAlias("result")
    private String result;

    public ResultModel(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
    
    
}
