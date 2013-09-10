package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 5/9/2556
 * Time: 11:56 น.
 * To change this template use File | Settings | File Templates.
 */
public class Stakeholder {

    private BigDecimal no;
    private String name;
    private String contactName;
    private String phoneNo;
    private String contactYear;
    private BigDecimal percentSalesVolumn;
    private BigDecimal percentCash;
    private BigDecimal percentCredit;
    private BigDecimal creditTerm;

    public BigDecimal getNo() {
        return no;
    }

    public void setNo(BigDecimal no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactYear() {
        return contactYear;
    }

    public void setContactYear(String contactYear) {
        this.contactYear = contactYear;
    }

    public BigDecimal getPercentSalesVolumn() {
        return percentSalesVolumn;
    }

    public void setPercentSalesVolumn(BigDecimal percentSalesVolumn) {
        this.percentSalesVolumn = percentSalesVolumn;
    }

    public BigDecimal getPercentCash() {
        return percentCash;
    }

    public void setPercentCash(BigDecimal percentCash) {
        this.percentCash = percentCash;
    }

    public BigDecimal getPercentCredit() {
        return percentCredit;
    }

    public void setPercentCredit(BigDecimal percentCredit) {
        this.percentCredit = percentCredit;
    }

    public BigDecimal getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(BigDecimal creditTerm) {
        this.creditTerm = creditTerm;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("name", name)
                .append("contactName", contactName)
                .append("phoneNo", phoneNo)
                .append("contactYear", contactYear)
                .append("percentSalesVolumn", percentSalesVolumn)
                .append("percentCash", percentCash)
                .append("percentCredit", percentCredit)
                .append("creditTerm", creditTerm)
                .toString();
    }
}
