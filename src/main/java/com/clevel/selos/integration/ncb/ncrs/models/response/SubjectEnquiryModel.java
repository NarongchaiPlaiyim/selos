package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

public class SubjectEnquiryModel implements Serializable {

    private String idtype;
    private String idnumber;
    private String issuecountry;
    @XStreamImplicit(itemFieldName = "dispute")
    private ArrayList<SubjectIdDisputeModel> dispute = new ArrayList<SubjectIdDisputeModel>();

    public SubjectEnquiryModel(String idtype, String idnumber, String issuecountry, ArrayList<SubjectIdDisputeModel> dispute) {
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.issuecountry = issuecountry;
        this.dispute = dispute;
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

    public ArrayList<SubjectIdDisputeModel> getDispute() {
        return dispute;
    }


}
