package com.clevel.selos.integration.nccrs.models.request;
public class H2HRequestModel {
    private String registtype;
    private String registid;
    private String companyname;
    private String inqpurpose;
    private String producttype;
    private String memberref;
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