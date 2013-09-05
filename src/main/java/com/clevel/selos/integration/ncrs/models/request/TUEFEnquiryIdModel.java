package com.clevel.selos.integration.ncrs.models.request;

import com.clevel.selos.integration.ncrs.exception.ValidationException;
import com.clevel.selos.util.ValidationUtil;
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

    public void validation()throws Exception{
        if(ValidationUtil.isNull(idtype))throw new ValidationException("idtype is null");
        if(ValidationUtil.isGreaterThan(2, idtype))throw new ValidationException("Length of idtype is more than 2");

        if(ValidationUtil.isNull(idnumber))throw new ValidationException("idnumber is null");
        if(ValidationUtil.isGreaterThan(20, idnumber))throw new ValidationException("Length of idnumber is more than 20");

        if(!ValidationUtil.isNull(issuecountry) && ValidationUtil.isGreaterThan(2, issuecountry))throw new ValidationException("Length of issuecountry is more than 2");

    }
    
}
