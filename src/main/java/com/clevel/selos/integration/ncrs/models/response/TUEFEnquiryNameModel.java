package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("name")
public class TUEFEnquiryNameModel {
    
    @XStreamAlias("familyname")
    private String familyname;
    
    @XStreamAlias("firstname")
    private String firstname;
    
    @XStreamAlias("middlename")
    private String middlename;
    
    @XStreamAlias("dateofbirth")
    private String dateofbirth;

    public TUEFEnquiryNameModel(String familyname, String firstname, String middlename, String dateofbirth) {
        this.familyname = familyname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.dateofbirth = dateofbirth;
    }

    public String getFamilyname() {
        return familyname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }
    
    
}
