package com.clevel.selos.integration.ncb.nccrs.models.request;

import java.io.Serializable;

public class H2HRequestModel implements Serializable {
    private String registtype;
    private String registid;
    private String companyname;
    private String inqpurpose;
    private String producttype;
    private String memberref;
    private String displayinqhist = "1";
    private String confirmconsent;
    private String language;

    public H2HRequestModel(String registtype, String registid, String companyname, String inqpurpose, String producttype, String memberref, String confirmconsent, String language) {
        this.registtype = registtype;
        this.registid = registid;
        this.companyname = companyname;
        this.inqpurpose = inqpurpose;
        this.producttype = producttype;
        this.memberref = memberref;
        this.confirmconsent = confirmconsent;
        this.language = language;
    }

}
