package com.clevel.selos.integration.ncb.nccrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("criteria")
public class CriteriaModel implements Serializable {
    private String enquirydatefrom = "19000101";
    private String enquirydateto ="";
    private String enquiryuser;
    private String enquirystatus = "IQ";
    private String registid;
    private String mediacode = "BB";
    private String memberreference;

    public CriteriaModel( String enquiryuser, String registid, String memberreference) {
//        this.enquirydatefrom = enquirydatefrom;
        this.enquiryuser = enquiryuser;
        this.registid = registid;
        this.memberreference = memberreference;
    }
}
