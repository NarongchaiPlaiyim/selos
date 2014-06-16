package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class ApprovedCreditOfferLetterReport extends ReportModel {

    private String productProgramName;
    private String creditTypeName;
    private BigDecimal limit;
    private String finalPriceLabel;
    private BigDecimal installment;
    private int tenor;
    private String purpose;
    private BigDecimal approveTotalCreditLimit;

    public ApprovedCreditOfferLetterReport() {
        productProgramName = getDefaultString();
        creditTypeName = getDefaultString();
        limit = getDefaultBigDecimal();
        finalPriceLabel = getDefaultString();
        installment = getDefaultBigDecimal();
        tenor = getDefaultInteger();
        purpose = getDefaultString();
        approveTotalCreditLimit = getDefaultBigDecimal();
    }

    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }

    public String getCreditTypeName() {
        return creditTypeName;
    }

    public void setCreditTypeName(String creditTypeName) {
        this.creditTypeName = creditTypeName;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getApproveTotalCreditLimit() {
        return approveTotalCreditLimit;
    }

    public void setApproveTotalCreditLimit(BigDecimal approveTotalCreditLimit) {
        this.approveTotalCreditLimit = approveTotalCreditLimit;
    }

    public String getFinalPriceLabel() {
        return finalPriceLabel;
    }

    public void setFinalPriceLabel(String finalPriceLabel) {
        this.finalPriceLabel = finalPriceLabel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgramName", productProgramName)
                .append("creditTypeName", creditTypeName)
                .append("limit", limit)
                .append("finalPriceLabel", finalPriceLabel)
                .append("installment", installment)
                .append("tenor", tenor)
                .append("purpose", purpose)
                .append("approveTotalCreditLimit", approveTotalCreditLimit)
                .toString();
    }
}
