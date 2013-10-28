package com.clevel.selos.integration.ncb.nccrs.nccrsmodel;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NCCRSModel implements Serializable {
    private String registType;
    private String registId;
    private String companyName;
    private String inqPurose = "1170001";
    private String productType = "2030001";
    private String memberRef;
    private String confirmConsent = "Y";
    /*
    2060001 = English
    2060002 = Thai
    */
    private String language = "2060002";
    private String historicalBalanceReport = "Y";
    private String trackingId;
    private String juristicType;

    public NCCRSModel() {
    }

    public String getRegistId() {
        return registId;
    }

    public void setRegistId(String registId) {
        this.registId = registId;
    }

    public String getRegistType() {
        return registType;
    }

    public void setRegistType(RegistType registType) {
        this.registType = registType.value();
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

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getJuristicType() {
        if ("1140001".equals(registType)) {
            return juristicType = "01";
        } else if ("1140002".equals(registType)) {
            return juristicType = "02";
        } else if ("1140003".equals(registType)) {
            return juristicType = "03";
        } else if ("1140004".equals(registType)) {
            return juristicType = "04";
        } else if ("1140005".equals(registType)) {
            return juristicType = "05";
        } else {
            return juristicType = "01";
        }
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
                .append("trackingId", trackingId)
                .toString();
    }
}
