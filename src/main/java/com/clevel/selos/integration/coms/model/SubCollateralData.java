package com.clevel.selos.integration.coms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubCollateralData implements Serializable {
    private String collId;
    private String headCollId;
    private String landOffice;
    private long runningNumber;
    private String headCollType;
    private String subCollType;
    private String titleDeed;
    private String collateralOwnerId; //TMB_CUSTID
    private String collateralOwner;
    private BigDecimal appraisalValue;
    private String usage;
    private String typeOfUsage;
    private String address;

    public String getCollId() {
        return collId;
    }

    public void setCollId(String collId) {
        this.collId = collId;
    }

    public String getHeadCollId() {
        return headCollId;
    }

    public void setHeadCollId(String headCollId) {
        this.headCollId = headCollId;
    }

    public long getRunningNumber() {
        return runningNumber;
    }

    public void setRunningNumber(long runningNumber) {
        this.runningNumber = runningNumber;
    }

    public String getHeadCollType() {
        return headCollType;
    }

    public void setHeadCollType(String headCollType) {
        this.headCollType = headCollType;
    }

    public String getSubCollType() {
        return subCollType;
    }

    public void setSubCollType(String subCollType) {
        this.subCollType = subCollType;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralOwnerId() {
        return collateralOwnerId;
    }

    public void setCollateralOwnerId(String collateralOwnerId) {
        this.collateralOwnerId = collateralOwnerId;
    }

    public String getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(String collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    public void setTypeOfUsage(String typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandOffice() {
        return landOffice;
    }

    public void setLandOffice(String landOffice) {
        this.landOffice = landOffice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collId", collId)
                .append("headCollId", headCollId)
                .append("landOffice", landOffice)
                .append("runningNumber", runningNumber)
                .append("headCollType", headCollType)
                .append("subCollType", subCollType)
                .append("titleDeed", titleDeed)
                .append("collateralOwnerId", collateralOwnerId)
                .append("collateralOwner", collateralOwner)
                .append("appraisalValue", appraisalValue)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
                .append("address", address)
                .toString();
    }
}
