package com.clevel.selos.integration.rlos.appin.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditDetail implements Serializable {
    private String productCode;
    private String projectCode;
    private BigDecimal interestRate;
    private BigDecimal requestTenor;
    private BigDecimal requestLimit;
    private BigDecimal finalTenors;
    private BigDecimal finalLimit;
    private BigDecimal finalInstallment;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getRequestTenor() {
        return requestTenor;
    }

    public void setRequestTenor(BigDecimal requestTenor) {
        this.requestTenor = requestTenor;
    }

    public BigDecimal getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(BigDecimal requestLimit) {
        this.requestLimit = requestLimit;
    }

    public BigDecimal getFinalTenors() {
        return finalTenors;
    }

    public void setFinalTenors(BigDecimal finalTenors) {
        this.finalTenors = finalTenors;
    }

    public BigDecimal getFinalLimit() {
        return finalLimit;
    }

    public void setFinalLimit(BigDecimal finalLimit) {
        this.finalLimit = finalLimit;
    }

    public BigDecimal getFinalInstallment() {
        return finalInstallment;
    }

    public void setFinalInstallment(BigDecimal finalInstallment) {
        this.finalInstallment = finalInstallment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("interestRate", interestRate)
                .append("requestTenor", requestTenor)
                .append("requestLimit", requestLimit)
                .append("finalTenors", finalTenors)
                .append("finalLimit", finalLimit)
                .append("finalInstallment", finalInstallment)
                .toString();
    }
}
