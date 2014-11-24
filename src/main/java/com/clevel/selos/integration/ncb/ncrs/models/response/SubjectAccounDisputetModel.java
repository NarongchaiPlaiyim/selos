package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("dispute")
public class SubjectAccounDisputetModel implements Serializable {

    @XStreamAlias("disputedate")
    private String disputedate;

    @XStreamAlias("disputetime")
    private String disputetime;

    @XStreamAlias("disputecode")
    private String disputecode;

    @XStreamAlias("disputecodedesc")
    private String disputecodedesc;

    @XStreamAlias("disputeremark")
    private String disputeremark;

    public SubjectAccounDisputetModel(String disputedate, String disputetime, String disputecode, String disputecodedesc, String disputeremark) {
        this.disputedate = disputedate;
        this.disputetime = disputetime;
        this.disputecode = disputecode;
        this.disputecodedesc = disputecodedesc;
        this.disputeremark = disputeremark;
    }

    public String getDisputedate() {
        return disputedate;
    }

    public String getDisputetime() {
        return disputetime;
    }

    public String getDisputecode() {
        return disputecode;
    }

    public String getDisputecodedesc() {
        return disputecodedesc;
    }

    public String getDisputeremark() {
        return disputeremark;
    }


}
