package com.clevel.selos.integration.ncb.ncrs.models.response;

import java.io.Serializable;

public class NameModel implements Serializable {
    private String familyname;
    private String firstname;
    private String middlename;
    private String dateofbirth;

    public NameModel(String familyname, String firstname, String middlename, String dateofbirth) {
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
