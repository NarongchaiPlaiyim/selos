package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("id")
public class TUEFEnquiryIdModel implements Serializable {
    
    @XStreamAlias("idtype")
    private String idtype;
    
    @XStreamAlias("idnumber")
    private String idnumber;
    
    @XStreamAlias("issuecountry")
    private String issuecountry;

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
