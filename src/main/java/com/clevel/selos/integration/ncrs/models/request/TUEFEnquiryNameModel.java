package com.clevel.selos.integration.ncrs.models.request;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.util.ValidationUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XStreamAlias("name")
public class TUEFEnquiryNameModel{
    
    @XStreamAlias("familyname")
    private String familyname;
    
    @XStreamAlias("firstname")
    private String firstname;
    
    @XStreamAlias("middlename")
    private String middlename;
    
    @XStreamAlias("dateofbirth")
    private String dateofbirth;

    public TUEFEnquiryNameModel(String familyname, String firstname, String dateofbirth) {
        this.familyname = familyname;
        this.firstname = firstname;
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
