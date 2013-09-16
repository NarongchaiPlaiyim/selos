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

    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
    }
    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser, String enquirystatus) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
        this.enquirystatus = enquirystatus;
    }
    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser, String enquirystatus, String idtype) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
        this.enquirystatus = enquirystatus;
        this.idtype = idtype;
    }
    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser, String enquirystatus, String idtype, String idnumber) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
        this.enquirystatus = enquirystatus;
        this.idtype = idtype;
        this.idnumber = idnumber;
    }
    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser, String enquirystatus, String idtype, String idnumber, String consent) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
        this.enquirystatus = enquirystatus;
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.consent = consent;
    }
    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser, String enquirystatus, String idtype, String idnumber, String consent, String mediacode) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
        this.enquirystatus = enquirystatus;
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.consent = consent;
        this.mediacode = mediacode;
    }
    public CriteriaModel(String enquirydatefrom, String enquirydateto, String enquiryuser, String enquirystatus, String idtype, String idnumber, String consent, String mediacode, String memberreference) {
        this.enquirydatefrom = enquirydatefrom;
        this.enquirydateto = enquirydateto;
        this.enquiryuser = enquiryuser;
        this.enquirystatus = enquirystatus;
        this.idtype = idtype;
        this.idnumber = idnumber;
        this.consent = consent;
        this.mediacode = mediacode;
        this.memberreference = memberreference;
    }

}
