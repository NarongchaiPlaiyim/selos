package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class CollateralAndGuarantorOfferLetterReport extends ReportModel {

    private String subCollateralTypeName;
    private String titleDeed;
    private String collateralOwnerUW;
    private String mortgageList;
    private BigDecimal mortgageValue;
    private String address;

    public CollateralAndGuarantorOfferLetterReport() {
        subCollateralTypeName = getDefaultString();
        titleDeed = getDefaultString();
        collateralOwnerUW = getDefaultString();
        mortgageList = getDefaultString();
        mortgageValue = getDefaultBigDecimal();
        address = getDefaultString();
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

    public String getMortgageList() {
        return mortgageList;
    }

    public void setMortgageList(String mortgageList) {
        this.mortgageList = mortgageList;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("subCollateralTypeName", subCollateralTypeName)
                .append("titleDeed", titleDeed)
                .append("collateralOwnerUW", collateralOwnerUW)
                .append("mortgageList", mortgageList)
                .append("mortgageValue", mortgageValue)
                .append("address", address)
                .toString();
    }
}
