package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ExistingCreditTypeDetailReport {

    private String creditType;
    private String productProgramNameAndCreditFacility;
    private BigDecimal limit;
    private String guaranteeAmount;

    public ExistingCreditTypeDetailReport() {
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getProductProgramNameAndCreditFacility() {
        return productProgramNameAndCreditFacility;
    }

    public void setProductProgramNameAndCreditFacility(String productProgramNameAndCreditFacility) {
        this.productProgramNameAndCreditFacility = productProgramNameAndCreditFacility;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(String guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("creditType", creditType)
                .append("productProgramNameAndCreditFacility", productProgramNameAndCreditFacility)
                .append("limit", limit)
                .append("guaranteeAmount", guaranteeAmount)
                .toString();
    }
}
