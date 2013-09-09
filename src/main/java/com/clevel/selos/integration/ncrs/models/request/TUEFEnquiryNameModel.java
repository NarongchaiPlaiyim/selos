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

    public TUEFEnquiryNameModel(String familyname, String firstname, String middlename, String dateofbirth) {
        this.familyname = familyname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.dateofbirth = dateofbirth;
    }

    public void validation()throws Exception{
        if(ValidationUtil.isNull(familyname))throw new ValidationException("familyname is null");
        if(ValidationUtil.isGreaterThan(50, familyname))throw new ValidationException("Length of familyname is more than 50");

        if(ValidationUtil.isNull(firstname))throw new ValidationException("firstname is null");
        if(ValidationUtil.isGreaterThan(30, firstname))throw new ValidationException("Length of firstname is more than 30");

        if(!ValidationUtil.isNull(middlename) && ValidationUtil.isGreaterThan(26, middlename))throw new ValidationException("Length of middlename is more than 26");

        if(ValidationUtil.isNull(dateofbirth))throw new ValidationException("dateofbirth is null");
        if(ValidationUtil.isGreaterThan(8, dateofbirth))throw new ValidationException("Length of dateofbirth is more than 8");

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
