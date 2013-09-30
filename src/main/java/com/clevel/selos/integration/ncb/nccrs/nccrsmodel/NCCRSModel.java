package com.clevel.selos.integration.ncb.nccrs.nccrsmodel;


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
    private String trackingId;

    public NCCRSModel() {
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

    public void setRegistIdCompanyLimited() {
        registId = RegistType.CompanyLimited.value();
    }

    public void setRegistIdLimitedPartnership() {
        registId = RegistType.LimitedPartnership.value();
    }
    public void setRegistIdRegisteredOrdinaryPartnership() {
        registId = RegistType.RegisteredOrdinaryPartnership.value();
    }
    public void setRegistIdPublicCompanyLimited() {
        registId = RegistType.PublicCompanyLimited.value();
    }
    public void setRegistIdForeignRegistrationIdOrOthers() {
        registId = RegistType.ForeignRegistrationIdOrOthers.value();
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
