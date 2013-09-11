package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dispute")
public class SubjectNameDisputeModel {
    
    @XStreamAlias("disputedate")
    private String disputedate;
    
    @XStreamAlias("disputetime")
    private String disputetime;
    
    @XStreamAlias("disputecode")
    private String disputecode;
    
    @XStreamAlias("disputecodedesc")
    private String disputecodedesc;
    
    @XStreamAlias("disputecoderemark")
    private String disputecoderemark;

    public SubjectNameDisputeModel(String disputedate, String disputetime, String disputecode, String disputecodedesc, String disputecoderemark) {
        this.disputedate = disputedate;
        this.disputetime = disputetime;
        this.disputecode = disputecode;
        this.disputecodedesc = disputecodedesc;
        this.disputecoderemark = disputecoderemark;
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

    public String getDisputecoderemark() {
        return disputecoderemark;
    }
    
    
    
}