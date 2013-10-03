package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BizStakeHolderDetailView implements Serializable {

    private long no;
    private String stakeHolderType;
    private String name;
    private String contactName;
    private String phoneNo;
    private String contactYear;
    private BigDecimal percentSalesVolume;
    private BigDecimal percentCash;
    private BigDecimal percentCredit;
    private BigDecimal creditTerm;

    public String getStakeHolderType() {
        return stakeHolderType;
    }

    public void setStakeHolderType(String stakeHolderType) {
        this.stakeHolderType = stakeHolderType;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
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

    public BigDecimal getPercentSalesVolume() {
        return percentSalesVolume;
    }

    public void setPercentSalesVolume(BigDecimal percentSalesVolume) {
        this.percentSalesVolume = percentSalesVolume;
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
                .append("stakeHolderType", stakeHolderType)
                .append("name", name)
                .append("contactName", contactName)
                .append("phoneNo", phoneNo)
                .append("contactYear", contactYear)
                .append("percentSalesVolume", percentSalesVolume)
                .append("percentCash", percentCash)
                .append("percentCredit", percentCredit)
                .append("creditTerm", creditTerm)
                .toString();
    }
}
