package com.clevel.selos.integration.ncrs.models.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("criteria")
public class CriteriaModel implements Serializable{
    private String enquirydatefrom;
    private String enquirydateto;
    private String enquiryuser;
    private String enquirystatus;
    private String idtype;
    private String idnumber;
    private String consent;
    private String mediacode;
    private String memberreference;

    public CriteriaModel(String enquirydateto, String enquiryuser) {
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
    }

    public CriteriaModel(String enquirydatefrom, String idtype, String idnumber, String enquiryuser) {
        this.enquirydatefrom = enquirydatefrom;
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.enquiryuser = enquiryuser;
    }
}
