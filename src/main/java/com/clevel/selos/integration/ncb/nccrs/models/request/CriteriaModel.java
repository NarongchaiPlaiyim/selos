package com.clevel.selos.integration.ncb.nccrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("criteria")
public class CriteriaModel implements Serializable {
    private String enquirydatefrom;
    private String enquirydateto;
    private String enquiryuser;
    private String enquirystatus;
    private String registid;
    private String mediacode;
    private String memberreference;

    public CriteriaModel(String enquirydatefrom, String enquiryuser, String registid) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquiryuser = enquiryuser;
        this.registid = registid;
    }
}
