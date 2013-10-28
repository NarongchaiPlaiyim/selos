package com.clevel.selos.integration.ncb.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@XStreamAlias("id")
public class TUEFEnquiryIdModel implements Serializable {

    @XStreamAlias("idtype")
    private String idtype;

    @XStreamAlias("idnumber")
    private String idnumber;

    @XStreamAlias("issuecountry")
    private String issuecountry;

    public TUEFEnquiryIdModel(String idtype, String idnumber) {
        this.idtype = idtype;
        this.idnumber = idnumber;
    }

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

    public void setIssuecountry(String issuecountry) {
        this.issuecountry = issuecountry;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("idtype", idtype)
                .append("idnumber", idnumber)
                .append("issuecountry", issuecountry)
                .toString();
    }
}
