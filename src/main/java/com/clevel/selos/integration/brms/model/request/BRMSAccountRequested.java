package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSAccountRequested implements Serializable{
    private String productProgram;
    private String proposeId;
    private String creditType;
    private BigDecimal limit;
    private int tenors;
    private String loanPurpose;
    private BigDecimal fontEndFeeDiscountRate;

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getProposeId() {
        return proposeId;
    }

    public void setProposeId(String proposeId) {
        this.proposeId = proposeId;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public int getTenors() {
        return tenors;
    }

    public void setTenors(int tenors) {
        this.tenors = tenors;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public BigDecimal getFontEndFeeDiscountRate() {
        return fontEndFeeDiscountRate;
    }

    public void setFontEndFeeDiscountRate(BigDecimal fontEndFeeDiscountRate) {
        this.fontEndFeeDiscountRate = fontEndFeeDiscountRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", productProgram)
                .append("proposeId", proposeId)
                .append("creditType", creditType)
                .append("limit", limit)
                .append("tenors", tenors)
                .append("loanPurpose", loanPurpose)
                .append("fontEndFeeDiscountRate", fontEndFeeDiscountRate)
                .toString();
    }
}
