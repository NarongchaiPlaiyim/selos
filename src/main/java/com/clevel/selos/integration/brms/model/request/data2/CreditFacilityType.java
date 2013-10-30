package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditFacilityType implements Serializable {
    private String creditFacilityType;  // todo : to be enum
    private BigDecimal creditLimit;
    private int tenor;
    private String guaranteeType;       // todo : to be enum
    private String loanPurpose;         // todo : to be enum

    public CreditFacilityType() {
    }

    public CreditFacilityType(String creditFacilityType, BigDecimal creditLimit, int tenor, String guaranteeType, String loanPurpose) {
        this.creditFacilityType = creditFacilityType;
        this.creditLimit = creditLimit;
        this.tenor = tenor;
        this.guaranteeType = guaranteeType;
        this.loanPurpose = loanPurpose;
    }

    public String getCreditFacilityType() {
        return creditFacilityType;
    }

    public void setCreditFacilityType(String creditFacilityType) {
        this.creditFacilityType = creditFacilityType;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public String getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("creditFacilityType", creditFacilityType)
                .append("creditLimit", creditLimit)
                .append("tenor", tenor)
                .append("guaranteeType", guaranteeType)
                .append("loanPurpose", loanPurpose)
                .toString();
    }
}
