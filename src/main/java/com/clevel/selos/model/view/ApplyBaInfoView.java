package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class ApplyBaInfoView implements Serializable{

    private boolean checked;
    private String cusName;
    private BigDecimal contactNumber;
    private String relationship;
    private String resultHealtcheck;
    private Date checkDate;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public BigDecimal getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(BigDecimal contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getResultHealtcheck() {
        return resultHealtcheck;
    }

    public void setResultHealtcheck(String resultHealtcheck) {
        this.resultHealtcheck = resultHealtcheck;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("checked", checked)
                .append("cusName", cusName)
                .append("contactNumber", contactNumber)
                .append("relationship", relationship)
                .append("resultHealtcheck", resultHealtcheck)
                .append("checkDate", checkDate)
                .toString();
    }
}
