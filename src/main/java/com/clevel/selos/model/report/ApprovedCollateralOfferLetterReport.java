package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApprovedCollateralOfferLetterReport extends ReportModel {

    private String path;
    private String subCollateralTypeName;
    private String titleDeed;
    private String collateralOwnerUW;
    private String mortgage;
    private BigDecimal mortgageValue;
    private String address;

    private List<ApprovedGuarantorOfferLetterReport> approvedGuarantorOfferLetterReport;

    public ApprovedCollateralOfferLetterReport() {
        subCollateralTypeName = getDefaultString();
        titleDeed = getDefaultString();
        collateralOwnerUW = getDefaultString();
        mortgage = getDefaultString();
        mortgageValue = getDefaultBigDecimal();
        address = getDefaultString();
        approvedGuarantorOfferLetterReport = new ArrayList<ApprovedGuarantorOfferLetterReport>();
    }

    public String getSubCollateralTypeName() {
        return subCollateralTypeName;
    }

    public void setSubCollateralTypeName(String subCollateralTypeName) {
        this.subCollateralTypeName = subCollateralTypeName;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralOwnerUW() {
        return collateralOwnerUW;
    }

    public void setCollateralOwnerUW(String collateralOwnerUW) {
        this.collateralOwnerUW = collateralOwnerUW;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ApprovedGuarantorOfferLetterReport> getApprovedGuarantorOfferLetterReport() {
        return approvedGuarantorOfferLetterReport;
    }

    public void setApprovedGuarantorOfferLetterReport(List<ApprovedGuarantorOfferLetterReport> approvedGuarantorOfferLetterReport) {
        this.approvedGuarantorOfferLetterReport = approvedGuarantorOfferLetterReport;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("path", path)
                .append("subCollateralTypeName", subCollateralTypeName)
                .append("titleDeed", titleDeed)
                .append("collateralOwnerUW", collateralOwnerUW)
                .append("mortgage", mortgage)
                .append("mortgageValue", mortgageValue)
                .append("address", address)
                .append("approvedGuarantorOfferLetterReport", approvedGuarantorOfferLetterReport)
                .toString();
    }
}
