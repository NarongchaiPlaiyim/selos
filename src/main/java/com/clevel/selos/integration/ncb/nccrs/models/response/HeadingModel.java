package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class HeadingModel implements Serializable {
    private String productref;
    private String producttype;
    private String issueddate;
    private String inqpurpose;
    private String subjectreturncode;

    public String getProductref() {
        return productref;
    }

    public String getProducttype() {
        return producttype;
    }

    public String getIssuedate() {
        return issueddate;
    }

    public String getInqpurpose() {
        return inqpurpose;
    }

    public String getSubjectreturncode() {
        return subjectreturncode;
    }
}
