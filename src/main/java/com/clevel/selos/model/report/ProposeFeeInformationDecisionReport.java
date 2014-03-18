package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProposeFeeInformationDecisionReport extends ReportModel{

    private int count;
    private String productProgram;
    private BigDecimal standardFrontEndFee;
    private BigDecimal commitmentFee;
    private BigDecimal extensionFee;
    private BigDecimal prepaymentFee;
    private BigDecimal cancellationFee;
    private BigDecimal standardFeeYear;
    private BigDecimal commitmentFeeYear;
    private BigDecimal extensionFeeYear;
    private BigDecimal prepaymentFeeYear;
    private BigDecimal cancellationFeeYear;

    public ProposeFeeInformationDecisionReport() {
        count = getDefaultInteger();
        productProgram = getDefaultString();
        standardFrontEndFee = getDefaultBigDecimal();
        commitmentFee = getDefaultBigDecimal();
        extensionFee = getDefaultBigDecimal();
        prepaymentFee = getDefaultBigDecimal();
        cancellationFee = getDefaultBigDecimal();
        standardFeeYear = getDefaultBigDecimal();
        commitmentFeeYear = getDefaultBigDecimal();
        extensionFeeYear = getDefaultBigDecimal();
        prepaymentFeeYear = getDefaultBigDecimal();
        cancellationFeeYear = getDefaultBigDecimal();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public BigDecimal getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(BigDecimal cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public BigDecimal getCommitmentFee() {
        return commitmentFee;
    }

    public void setCommitmentFee(BigDecimal commitmentFee) {
        this.commitmentFee = commitmentFee;
    }

    public BigDecimal getExtensionFee() {
        return extensionFee;
    }

    public void setExtensionFee(BigDecimal extensionFee) {
        this.extensionFee = extensionFee;
    }

    public BigDecimal getPrepaymentFee() {
        return prepaymentFee;
    }

    public void setPrepaymentFee(BigDecimal prepaymentFee) {
        this.prepaymentFee = prepaymentFee;
    }

    public BigDecimal getStandardFrontEndFee() {
        return standardFrontEndFee;
    }

    public void setStandardFrontEndFee(BigDecimal standardFrontEndFee) {
        this.standardFrontEndFee = standardFrontEndFee;
    }

    public BigDecimal getCancellationFeeYear() {
        return cancellationFeeYear;
    }

    public void setCancellationFeeYear(BigDecimal cancellationFeeYear) {
        this.cancellationFeeYear = cancellationFeeYear;
    }

    public BigDecimal getCommitmentFeeYear() {
        return commitmentFeeYear;
    }

    public void setCommitmentFeeYear(BigDecimal commitmentFeeYear) {
        this.commitmentFeeYear = commitmentFeeYear;
    }

    public BigDecimal getExtensionFeeYear() {
        return extensionFeeYear;
    }

    public void setExtensionFeeYear(BigDecimal extensionFeeYear) {
        this.extensionFeeYear = extensionFeeYear;
    }

    public BigDecimal getPrepaymentFeeYear() {
        return prepaymentFeeYear;
    }

    public void setPrepaymentFeeYear(BigDecimal prepaymentFeeYear) {
        this.prepaymentFeeYear = prepaymentFeeYear;
    }

    public BigDecimal getStandardFeeYear() {
        return standardFeeYear;
    }

    public void setStandardFeeYear(BigDecimal standardFeeYear) {
        this.standardFeeYear = standardFeeYear;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", productProgram)
                .append("standardFrontEndFee", standardFrontEndFee)
                .append("commitmentFee", commitmentFee)
                .append("extensionFee", extensionFee)
                .append("prepaymentFee", prepaymentFee)
                .append("cancellationFee", cancellationFee)
                .append("standardFeeYear", standardFeeYear)
                .append("commitmentFeeYear", commitmentFeeYear)
                .append("extensionFeeYear", extensionFeeYear)
                .append("prepaymentFeeYear", prepaymentFeeYear)
                .append("cancellationFeeYear", cancellationFeeYear)
                .toString();
    }
}
