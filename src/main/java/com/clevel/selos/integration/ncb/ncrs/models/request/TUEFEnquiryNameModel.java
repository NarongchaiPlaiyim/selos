package com.clevel.selos.integration.ncb.ncrs.models.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@XStreamAlias("name")
public class TUEFEnquiryNameModel implements Serializable {

    @XStreamAlias("familyname")
    private String familyname;

    @XStreamAlias("firstname")
    private String firstname;

    @XStreamAlias("middlename")
    private String middlename;

    @XStreamAlias("dateofbirth")
    private String dateofbirth;

    public TUEFEnquiryNameModel(String familyname, String firstname) {
        this.familyname = familyname;
        this.firstname = firstname;
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

    public TUEFEnquiryNameModel(String familyname, String firstname, String middlename, String dateofbirth) {
        this.familyname = familyname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.dateofbirth = dateofbirth;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("familyname", familyname)
                .append("firstname", firstname)
                .append("middlename", middlename)
                .append("dateofbirth", dateofbirth)
                .toString();
    }
}
