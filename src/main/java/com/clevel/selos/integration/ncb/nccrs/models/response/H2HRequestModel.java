package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class H2HRequestModel implements Serializable {
    private String registtype;
    private String registid;
    private String companyname;
    private String inqpurpose;
    private String producttype;
    private String memberref;
    private String confirmconsent;
    private String language;

    public String getRegisttype() {
        return registtype;
    }

    public String getRegistid() {
        return registid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getInqpurpose() {
        return inqpurpose;
    }

    public String getProducttype() {
        return producttype;
    }

    public String getMemberref() {
        return memberref;
    }

    public String getConfirmconsent() {
        return confirmconsent;
    }

    public String getLanguage() {
        return language;
    }

}
