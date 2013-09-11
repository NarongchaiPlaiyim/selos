package com.clevel.selos.integration.nccrs.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NCCRSModel implements Serializable {
    private String registType;
    private String registId;
    private String companyName;
    private String inqPurose;
    private String productType;
    private String memberRef;
    private String confirmConsent;
    private String language;
    private String historicalBalanceReport;

    public NCCRSModel() {
    }

    public NCCRSModel(String registType, String registId, String companyName, String inqPurose, String productType, String memberRef, String confirmConsent, String language, String historicalBalanceReport) {
        this.registType = registType;
        this.registId = registId;
        this.companyName = companyName;
        this.inqPurose = inqPurose;
        this.productType = productType;
        this.memberRef = memberRef;
        this.confirmConsent = confirmConsent;
        this.language = language;
        this.historicalBalanceReport = historicalBalanceReport;
    }

    public String getRegistType() {
        return registType;
    }

    public void setRegistType(String registType) {
        this.registType = registType;
    }

    public String getRegistId() {
        return registId;
    }

    public void setRegistId(String registId) {
        this.registId = registId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInqPurose() {
        return inqPurose;
    }

    public void setInqPurose(String inqPurose) {
        this.inqPurose = inqPurose;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getMemberRef() {
        return memberRef;
    }

    public void setMemberRef(String memberRef) {
        this.memberRef = memberRef;
    }

    public String getConfirmConsent() {
        return confirmConsent;
    }

    public void setConfirmConsent(String confirmConsent) {
        this.confirmConsent = confirmConsent;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHistoricalBalanceReport() {
        return historicalBalanceReport;
    }

    public void setHistoricalBalanceReport(String historicalBalanceReport) {
        this.historicalBalanceReport = historicalBalanceReport;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("registType", registType)
                .append("registId", registId)
                .append("companyName", companyName)
                .append("inqPurose", inqPurose)
                .append("productType", productType)
                .append("memberRef", memberRef)
                .append("confirmConsent", confirmConsent)
                .append("language", language)
                .append("historicalBalanceReport", historicalBalanceReport)
                .toString();
    }
}
