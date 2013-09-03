package com.clevel.selos.integration.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("id")
public class TUEFEnquiryIdModel {
    
    @XStreamAlias("idtype")
    private String idtype;//2
    
    @XStreamAlias("idnumber")
    private String idnumber;//20
    
    @XStreamAlias("issuecountry")
    private String issuecountry;//2

    public TUEFEnquiryIdModel(String idtype, String idnumber, String issuecountry) {
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
