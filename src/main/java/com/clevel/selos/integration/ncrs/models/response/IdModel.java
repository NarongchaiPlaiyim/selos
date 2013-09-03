package com.clevel.selos.integration.ncrs.models.response;
public class IdModel {
    private String idtype;
    private String idnumber;
    private String issuecountry;

    public IdModel(String idtype, String idnumber, String issuecountry) {
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.issuecountry = issuecountry;
    }

    public String getIdtype() {
        return idtype;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public String getIssuecountry() {
        return issuecountry;
    }
    
}
