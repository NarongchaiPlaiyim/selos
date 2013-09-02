package com.clevel.selos.integration.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("name")
public class TUEFEnquiryNameModel {
    
    @XStreamAlias("familyname")
    private String familyname;//50
    
    @XStreamAlias("firstname")
    private String firstname;//30
    
    @XStreamAlias("middlename")
    private String middlename;//26
    
    @XStreamAlias("dateofbirth")
    private String dateofbirth;//8

    public TUEFEnquiryNameModel(String familyname, String firstname, String dateofbirth) {
        this.familyname = familyname;
        this.firstname = firstname;
        this.dateofbirth = dateofbirth;
    }

    public TUEFEnquiryNameModel(String familyname, String firstname, String middlename, String dateofbirth) {
        this.familyname = familyname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.dateofbirth = dateofbirth;
    }
    
}
