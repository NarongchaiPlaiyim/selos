package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class ExSumCreditRiskInfoView {

    private String riskCusType;
    private String BOTClass;
    private String reason;
    private Date lastReviewDate;
    private Date nextReviewDate;
    private Date extendedReviewDate;
    private String indirectCountryName;
    private BigDecimal percentExport;

    public ExSumCreditRiskInfoView() {
        reset();
    }

    public void reset() {
        this.riskCusType = "";
        this.BOTClass = "";
        this.reason = "";
        this.lastReviewDate = new Date();
        this.nextReviewDate = new Date();
        this.extendedReviewDate = new Date();
        this.indirectCountryName = "";
        this.percentExport = BigDecimal.ZERO;
    }

    public String getRiskCusType() {
        return riskCusType;
    }

    public void setRiskCusType(String riskCusType) {
        this.riskCusType = riskCusType;
    }

    public String getBOTClass() {
        return BOTClass;
    }

    public void setBOTClass(String BOTClass) {
        this.BOTClass = BOTClass;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
    }

    public String getIndirectCountryName() {
        return indirectCountryName;
    }

    public void setIndirectCountryName(String indirectCountryName) {
        this.indirectCountryName = indirectCountryName;
    }

    public BigDecimal getPercentExport() {
        return percentExport;
    }

    public void setPercentExport(BigDecimal percentExport) {
        this.percentExport = percentExport;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("riskCusType", riskCusType).
                append("BOTClass", BOTClass).
                append("reason", reason).
                append("lastReviewDate", lastReviewDate).
                append("nextReviewDate", nextReviewDate).
                append("extendedReviewDate", extendedReviewDate).
                append("indirectCountryName", indirectCountryName).
                append("percentExport", percentExport).
                toString();
    }
}
