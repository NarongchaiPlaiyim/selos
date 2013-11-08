package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class InqHistModel implements Serializable {
    private String inqdate;
    private String creditor;
    private String creditortype;
    private String inqpurpose;
    private String registid;
    private String companyname;

    public String getInqdate() {
        return inqdate;
    }

    public String getCreditor() {
        return creditor;
    }

    public String getCreditortype() {
        return creditortype;
    }

    public String getInqpurpose() {
        return inqpurpose;
    }

    public String getRegistid() {
        return registid;
    }

    public String getCompanyname() {
        return companyname;
    }
}
