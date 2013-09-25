package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("history")
public class HistoryModel implements Serializable {
    
    @XStreamAlias("asofdate")
    private String asofdate;
    
    @XStreamAlias("overduemonth")
    private String overduemonth;
    
    @XStreamAlias("creditlimit")
    private String creditlimit;
    
    @XStreamAlias("amountowed")
    private String amountowed;

    public HistoryModel(String asofdate, String overduemonth, String creditlimit, String amountowed) {
        this.asofdate = asofdate;
        this.overduemonth = overduemonth;
        this.creditlimit = creditlimit;
        this.amountowed = amountowed;
    }

    public String getAsofdate() {
        return asofdate;
    }

    public String getOverduemonth() {
        return overduemonth;
    }

    public String getCreditlimit() {
        return creditlimit;
    }

    public String getAmountowed() {
        return amountowed;
    }
    
    
}
